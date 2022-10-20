package com.truestyle.service.user;

import com.truestyle.config.jwt.JwtUtils;
import com.truestyle.entity.user.PasswordResetToken;
import com.truestyle.entity.user.User;
import com.truestyle.pojo.NewToken;
import com.truestyle.repository.user.PasswordTokenRepository;
import com.truestyle.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class SecurityService {

    private final UserRepository userRepository;

    private final MailSender mailSender;

    private final PasswordEncoder passwordEncoder;

    private final PasswordTokenRepository passwordTokenRepository;

    private final JwtUtils jwtUtils;

    Authentication auth;
    public User getAuthUser(){
        auth = SecurityContextHolder.getContext().getAuthentication();
        return userRepository.findByUsername(auth.getName()).orElseThrow(() -> new RuntimeException("Error, User is not found, но аутентифицирован!))"));
    }


    // Выслать письмо с токеном для обновления пароля + сохраняем токен
    public List<String> resetPassword(String userEmail) {

        if (!userRepository.existsByEmail(userEmail)){
            return Arrays.asList("bad", "Error, Email isn't correct");
        }

        User user = userRepository.findByEmail(userEmail).orElseThrow(() -> new RuntimeException("User is not found"));

        String token = UUID.randomUUID().toString();
        createPasswordResetTokenForUser(user, token);

        String message = String.format(
                "Hello, %s!\n" +
                        "We have sent you a code for change your password: " +
                        token,
                user.getUsername()
        );

        mailSender.send(userEmail, "Code for update password", message);
        return Arrays.asList("good", "The code has been sent to the user");
    }

    // Генерируем токен для отправки
    public void createPasswordResetTokenForUser(User user, String token) {
        final Date expiry = Date.from(LocalDateTime.now().plusDays(PasswordResetToken.EXPIRATION).atZone(ZoneId.systemDefault()).toInstant());
        PasswordResetToken myToken = new PasswordResetToken(token, user, expiry);
        passwordTokenRepository.save(myToken);
    }

    // Валидный ли токен?
    public String validatePasswordResetToken(PasswordResetToken token) {

        return isTokenExpired(token) ? "expired"
                : null;
    }

    // Проверка даты токена
    private boolean isTokenExpired(PasswordResetToken passToken) {
        final LocalDateTime now = LocalDateTime.now();
        return passToken.getExpiryDate().before(Date.from(now.atZone(ZoneId.systemDefault()).toInstant()));
    }

    // Проверка ввода токена из письма
    public List<String> isCorrectTokenForChangePassword(String token) {
        if (!passwordTokenRepository.existsByToken(token)){
            return Arrays.asList("bad", "The code is incorrect");
        }
        final PasswordResetToken passToken = passwordTokenRepository.findByToken(token);
        String result = validatePasswordResetToken(passToken);
        if (result != null){
            return Arrays.asList(result, "Error, Code is incorrect");
        }
        return Arrays.asList("good", "The code is correct");
    }

    // Смена пароля
    public List<String> changePassword(String password, String token){

        // Проверка валидности токена
        List<String> result = isCorrectTokenForChangePassword(token);
        if (!"good".equals(result.get(0))){
            return Arrays.asList("bad", "Error, Code isn't correct");
        }

        PasswordResetToken userId = passwordTokenRepository.findByToken(token);
        User user = userId.getUser();
        if(user!=null) {
//            passwordTokenRepository.deleteByToken(token);
            changeUserPassword(user, password);
            return Arrays.asList("good", "The Password changed");
        } else {
            return Arrays.asList("bad", "Error, User isn't found");
        }
    }

    public void changeUserPassword(User user, String password) {
        user.setPassword(passwordEncoder.encode(password));
        userRepository.save(user);
    }

    // Смена имени пользователя
    public NewToken changeUsername(String newUsername){
        if (userRepository.existsByUsername(newUsername)){
            return null;
        }

        Authentication auth;
        auth = SecurityContextHolder.getContext().getAuthentication();

        User user = userRepository.findByUsername(auth.getName()).orElseThrow(() -> new RuntimeException("User is not found"));
        user.setUsername(newUsername);
        userRepository.save(user);
        String jwt = jwtUtils.generateJwtToken(user.getUsername());
        return new NewToken(
                jwt,
                user.getUsername()
        );
    }

}
