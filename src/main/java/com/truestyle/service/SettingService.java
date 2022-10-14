package com.truestyle.service;

import com.truestyle.entity.user.StyleUser;
import com.truestyle.entity.user.User;
import com.truestyle.pojo.SettingRequest;
import com.truestyle.pojo.UserInfo;
import com.truestyle.repository.user.GenderRepository;
import com.truestyle.repository.user.StyleUserRepository;
import com.truestyle.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class SettingService {

    private final UserRepository userRepository;

    private final GenderRepository genderRepository;

    private final StyleUserRepository styleUserRepo;

    private final SecurityService auth;

    // Получить все цитатки
    public List<StyleUser> getAllStyleUser(){
        return styleUserRepo.findAll();
    }

    // Получить цитату пользователя
    public StyleUser getStyleUser(){
        User user = auth.getAuthUser();
        return user.getStyleUser();
    }

    // Сохранить цитатку пользователя
    public List<String> saveStyleUser(Long idPhrase){

        User user = auth.getAuthUser();
        if (!styleUserRepo.existsById(idPhrase)){
            return Arrays.asList("bad", "Error: styleUser isn't exist");
        }
        StyleUser styleUser = styleUserRepo.findById(idPhrase).orElseThrow(() -> new RuntimeException("Error, Phrase is not found"));
        user.setStyleUser(styleUser);
        userRepository.save(user);
        return Arrays.asList("ok", "User phrase UPDATED");
    }

    public List<String> saveUserSettings(SettingRequest userData){
        User user = auth.getAuthUser();
        if (!genderRepository.existsById(userData.getGender())){
            return Arrays.asList("bad", "Error: Email isn't exist");
        }
        user.setGender(genderRepository.findById(userData.getGender()).orElse(null));
        user.setCountry(userData.getCountry());
        user.setPhotoUrl(userData.getPhotoUrl());
        user.setFullNumber(userData.getFullNumber());
        userRepository.save(user);
        return Arrays.asList("ok", "User UPDATED");
    }

    public UserInfo getUserSetting(){
        User user = auth.getAuthUser();
        UserInfo userInfo = new UserInfo();

        userInfo.setUsername(user.getUsername());
        userInfo.setEmail(user.getEmail());
        userInfo.setFullNumber(user.getFullNumber());
        userInfo.setGender(user.getGender());
        userInfo.setCountry(user.getCountry());
        userInfo.setPhotoUrl(user.getPhotoUrl());
        userInfo.setRoles(user.getRoles());

        return userInfo;

    }
}
