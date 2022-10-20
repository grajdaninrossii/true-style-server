package com.truestyle.controller;


import com.truestyle.entity.user.StyleUser;
import com.truestyle.pojo.*;
import com.truestyle.service.user.SecurityService;
import com.truestyle.service.user.SettingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user")
@CrossOrigin(origins = "*", maxAge = 3600) // Работа с безопасностью браузера
@RequiredArgsConstructor
public class UserController {

    private final SettingService settingService;

    private final SecurityService securityService;

    /** Обновление полей пользователя(без email, password, username)
     *
     * @param settingRequest - JSON(Объект SettingRequest) вида {
     *     "fullNumber": "8**********",
     *     "gender": Interger(id гендера),
     *     "country": String,
     *     "photoUrl": String
     * }
     * @return - вернет сообщение об успешном добавление
     * иначе сообщение об ошибке
     */
    @PostMapping("/set/setting")
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    public ResponseEntity<?> setDetailsUser(@RequestBody SettingRequest settingRequest){
        List<String> result = settingService.saveUserSettings(settingRequest);
        MessageResponse message = new MessageResponse(result.get(1));
        if ("bad".equals(result.get(0))){
            return ResponseEntity.badRequest().body(message);
        }
        return ResponseEntity.ok(message);
    }


    /** Получить настройки пользователя
     *
     * @return - вернет настройки пользователя
     */
    @GetMapping("/get/setting")
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    public ResponseEntity<?> getDetailsUser(){
        UserInfo userInfo = settingService.getUserSetting();
        return ResponseEntity.ok(userInfo);
    }

    /** Получить цитату пользоватля
     *
     * @return цитату пользователя, если не установлена, то ничего не вернет
     */
    @GetMapping("/get/styleuser")
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    public ResponseEntity<?> getStyleUserForUser(){
         StyleUser styleUser = settingService.getStyleUser();
         if (styleUser != null){
             return ResponseEntity.ok(styleUser);
         }
         return ResponseEntity.badRequest().body("User phrases is not exist");
    }

    /** Получить цитаты для пользователя (все)
     *
     * @return - вернет цитаты для пользователя
     */
    @GetMapping("/get/allphrases")
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    public ResponseEntity<?> getStyleUser(){
        List<StyleUser> stylesUser = settingService.getAllStyleUser();
        return ResponseEntity.ok(stylesUser);
    }

    /** Установить цитату для пользователя
     *
     * @return - вернет результат добавления
     */
    @PostMapping("/set/styleuser")
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    public ResponseEntity<?> setStyleUser(@RequestParam("id") Long idPhrase){
        List<String> stylesUser = settingService.saveStyleUser(idPhrase);
        return ResponseEntity.ok(stylesUser);
    }

    @PostMapping("/changeUsername")
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    public ResponseEntity<?> changeUsername(@RequestParam("username") String newUsername){
        NewToken result = securityService.changeUsername(newUsername);
        if (result != null){
            return ResponseEntity.ok(result);
        }
        return ResponseEntity.badRequest().body(new MessageResponse("Error: Username is exist"));
    }

    /** Запрос на смену пароля
     *
     * @param userEmail - почта пользователя
     * @return все отлично, если найдет почту и отправит на нее письмо с токеном
     * иначе, скажет что не нашел почту
     */
    @PostMapping("/resetPassword")
    public ResponseEntity<?> resetPassword(@RequestParam("email") String userEmail) {

        List<String> result = securityService.resetPassword(userEmail);
        MessageResponse message = new MessageResponse(result.get(1));
        if ("bad".equals(result.get(0))){
            return ResponseEntity.badRequest().body(message);
        }
        return ResponseEntity.ok(message);
    }

    /** Проверка валидности токена
     *
     * @param token - токен из письма
     * @return вернет сообщение с результатом проверки токена
     */
    @GetMapping("/changePassword")
    public ResponseEntity<?> changePassword(@RequestParam("token") String token) {

        List<String> result = securityService.isCorrectTokenForChangePassword(token);
        MessageResponse message = new MessageResponse(result.get(1));
        if (!"good".equals(result.get(0))){
            return ResponseEntity.badRequest().body(message);
        }
        return ResponseEntity.ok(message);
    }

    /** Установка нового пароля
     *
     * @param newPassword - JSON(Объект NewPasswordRequest) вида {
     *     "token":String,
     *     "password": String
     * }
     * @return - вернет сообщение об успешном обновлении,
     * иначе может поругаться на токен
     */
    @PostMapping("/savePassword")
    public ResponseEntity<?> saveNewPassword(@RequestBody NewPasswordRequest newPassword){
        List<String> result = securityService.changePassword(newPassword.getPassword(), newPassword.getToken());
        MessageResponse message = new MessageResponse(result.get(1));
        if (!"good".equals(result.get(0))){
            return ResponseEntity.badRequest().body(message);
        }
        return ResponseEntity.ok(message);
    }
}
