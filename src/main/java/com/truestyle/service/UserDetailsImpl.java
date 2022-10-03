package com.truestyle.service;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.truestyle.entity.user.Gender;
import com.truestyle.entity.user.Role;
import com.truestyle.entity.user.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;


// Выделение бизнес-логики
// Сериализатор для нашего пользователя
@Data
//@Getter
@AllArgsConstructor
public class UserDetailsImpl implements UserDetails {

//    @Serial
    private static final long serialVersionUID = 1L;

    // id пользователя
    private Long id;
    private String username;
    private String email;
    @JsonIgnore
    private String password;
    private String code_number1;
    private String code_number2;
    private String number;
    private String full_number1;
    private Gender gender;
    private String country;
    private String photo_url;
    private Set<Role> roles = new HashSet<>();

    private Collection<? extends GrantedAuthority> authorities;

    public static UserDetailsImpl build(User user){
        List<GrantedAuthority> authorities = user.getRoles().stream()
                .map(role -> new SimpleGrantedAuthority(role.getName().name()))
                .collect(Collectors.toList());

        return new UserDetailsImpl(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getPassword(),
                user.getCode_number1(),
                user.getCode_number2(),
                user.getNumber(),
                user.getFullNumber(),
                user.getGender(),
                user.getCountry(),
                user.getPhotoUrl(),
                user.getRoles(),
                authorities);
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        UserDetailsImpl other = (UserDetailsImpl) obj;
        if (id == null) {
            return other.id == null;
        } else return id.equals(other.id);
    }
}
