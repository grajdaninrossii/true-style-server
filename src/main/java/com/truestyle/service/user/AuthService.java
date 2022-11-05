package com.truestyle.service.user;

import com.truestyle.config.jwt.JwtUtils;
import com.truestyle.entity.user.ERole;
import com.truestyle.entity.user.Role;
import com.truestyle.entity.user.User;
import com.truestyle.pojo.JwtResponse;
import com.truestyle.pojo.LoginRequest;
import com.truestyle.pojo.SignupRequest;
import com.truestyle.repository.user.RoleRepository;
import com.truestyle.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class AuthService {
    private final UserRepository userRepository;

    private final RoleRepository roleRepository;

    private final PasswordEncoder passwordEncoder;

    private final AuthenticationManager authenticationManager;

    private final JwtUtils jwtUtils;


    public JwtResponse getUserData(LoginRequest loginRequest) {

        String usernameOrEmail = loginRequest.getUsername().split(" ")[0];

        // Если пользователь есть в базе, то возвращаем пустой JWTResponse
        if (!userRepository.existsByLoginOrEmail(usernameOrEmail, usernameOrEmail)){
            return new JwtResponse(null,
                    null,
                    null,
                    null,
                    null);
        }

        User user = userRepository.findByLogin(usernameOrEmail, usernameOrEmail)
                    .orElseThrow(()-> new RuntimeException("Error, user is not found"));

        // Менеджер аутентификации, передаем в конструктор токен аутентификации, в котором имя пользователя и пароль
        Authentication authentication = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(
                        user.getUsername(),
                        loginRequest.getPassword()));

        // Устанавливаем аутентификацию
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String username = authentication.getName();
        String jwt = jwtUtils.generateJwtToken(username); // генерируем токен

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        List<String> roles = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());

        return new JwtResponse(jwt,
                userDetails.getId(),
                userDetails.getUsername(),
                userDetails.getEmail(),
                roles);
    }

    public List<String> addUser(SignupRequest signupRequest){
        // Если пользователь есть в базе, то возвращаем сообщение об ошибке
        if (userRepository.existsByUsername(signupRequest.getUsername())) {
            return Arrays.asList("bad", "Error: Username is exist");
        }

        if (userRepository.existsByEmail(signupRequest.getEmail())) {
            return Arrays.asList("bad", "Error: Email is exist");
        }

        User user = new User(signupRequest.getUsername(),
                signupRequest.getEmail(),
                passwordEncoder.encode(signupRequest.getPassword()));

        Set<String> reqRoles = signupRequest.getRoles();
        Set<Role> roles = new HashSet<>();

//        System.out.println(reqRoles);

        if (reqRoles == null) {
            Role userRole = roleRepository
                    .findByName(ERole.ROLE_USER)
                    .orElseThrow(() -> new RuntimeException("Error, Role USER is not found"));
            roles.add(userRole);
        } else {
            reqRoles.forEach(r -> {
                switch (r) {
                    case "admin": {
                        Role adminRole = roleRepository
                                .findByName(ERole.ROLE_ADMIN)
                                .orElseThrow(() -> new RuntimeException("Error, Role ADMIN is not found"));
                        roles.add(adminRole);
                        break;
                    }
                    case "mod": {
                        Role modRole = roleRepository
                                .findByName(ERole.ROLE_MODERATOR)
                                .orElseThrow(() -> new RuntimeException("Error, Role MODERATOR is not found"));
                        roles.add(modRole);
                        break;
                    }

                    // По дефолту добавляем роль User
                    default: {
                        Role userRole = roleRepository
                                .findByName(ERole.ROLE_USER)
                                .orElseThrow(() -> new RuntimeException("Error, Role USER is not found"));
                        roles.add(userRole);
                        break;
                    }
                }
            });
        }
        // Устанавливаем роли нашему пользователю (код сверху это жесть, нужно переписать)
        user.setRoles(roles);
        userRepository.save(user); // сохраняем пользователя в бд
        return Arrays.asList("good", "User CREATED");
    }

    public List<String> checkUsername(String username){
        if (userRepository.existsByUsername(username)){
            return Arrays.asList("bad", "Error: Username is exist");
        }
        return Arrays.asList("good", "Username isn't exist yet");
    }

    public List<String> checkEmail(String email){
        if (userRepository.existsByEmail(email)){
            return Arrays.asList("bad", "Error: Email is exist");
        }
        return Arrays.asList("good", "Email isn't exist yet");
    }

}