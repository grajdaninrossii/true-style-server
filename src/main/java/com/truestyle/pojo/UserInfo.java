package com.truestyle.pojo;

import com.truestyle.entity.user.Gender;
import com.truestyle.entity.user.Role;
import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
public class UserInfo {

    private String username;

    // email пользователя
    private String email;

    // Ссылка на пароли
    private String password;

    // Хранение номера телефона
    private String code_number1;
    private String code_number2;
    private String number;
    private String fullNumber;

    private Gender gender;
    private String country;
    private String photoUrl;
    private Set<Role> roles = new HashSet<>();
//    private Set<Stuff> likesStuff = new HashSet<>();
//    private Set<Stuff> wardrobe = new HashSet<>();
}
