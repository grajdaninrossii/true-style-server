package com.truestyle.controller;

import com.truestyle.entity.stuff.ShopStuff;
import com.truestyle.entity.stuff.UserStuff;
import com.truestyle.pojo.MessageResponse;
import com.truestyle.pojo.WardrobeResponse;
import com.truestyle.service.StuffService;
import com.truestyle.service.WardrobeService;
import javax.validation.Valid;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/wardrobe")
@CrossOrigin(origins = "*", maxAge = 3600)
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class WardrobeController {

    private final WardrobeService wardrobeService;

    private final StuffService stuffService;

    @GetMapping(value = "/get/userstuff")
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    public ResponseEntity<UserStuff> getUserStuff(@RequestParam("id") Long stuffId){
         return ResponseEntity.ok(wardrobeService.getUserStuff(stuffId));
    }

    @GetMapping(value = "/get/shopstuff")
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    public ResponseEntity<ShopStuff> getShopStuff(@RequestParam("id") Long stuffId){
        return ResponseEntity.ok(wardrobeService.getShopStuff(stuffId));
    }

    @GetMapping(value = "/img/{url}")
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    public ResponseEntity<?> getUserStuffImg(@RequestParam("type") String type,
            @PathVariable("url") String url) throws IOException {
        if (!type.equals("shop") && !type.equals("user")){
            return ResponseEntity.badRequest().body("Request param is not found!");
        }
        return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(wardrobeService.getImage(type, url));
    }

    /** Получить одежду гардероба пользователя
     *
     * нужен токен!
     * @return если все чик пук, то вернет шмотьё, иначе пустой список
     */
    @GetMapping("/")
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    public ResponseEntity<WardrobeResponse> getWardrobeBySeason(){
        return ResponseEntity.ok(wardrobeService.getWardrobe());
    }

    /** Получить одежду гардероба пользователя по выбранному сезону
     *
     * @param season - String (Название сезона) + нужен токен!
     * @return если все чик пук, то вернет шмотьё, иначе пустой список
     * ВАЖНО: обработки на валидность сезона пока нет! Если ввести рандомное слово, то тоже вернет пустой список!!!
     */
    @GetMapping("/{season}")
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    public ResponseEntity<WardrobeResponse> getWardrobeBySeason(@PathVariable String season){
        return ResponseEntity.ok(wardrobeService.getWardrobeBySeason(season));
    }

    /**
     * Проверить, есть ли шмотка у пользователя
     *
     * @param type - тип хранения("shop" или др) + нужен токен!
     * @param id- шмотки в бд
     * @return выдать рез-тат проверки
     */
    @GetMapping("/check/{type}")
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    public ResponseEntity<?> checkStuffInWardrobe(@PathVariable String type, @RequestParam Long id){
         Boolean result = wardrobeService.checkStuffInWardrobe(type, id);

        // Если шмотка была у пользователя
        if (Boolean.TRUE.equals(result)){
            return ResponseEntity.ok(new MessageResponse("Stuff has already been added"));
        }

        // Иначе
        return ResponseEntity.badRequest().body(new MessageResponse("Stuff didn't add"));
    }


    /** Сохранение шмотки из магазина в гардероб
     *
     * @param stuffId тип id шмотки + нужен токен!
     * @return если Алах был доволен тобой и комета минула Землю, то вернет сообщение об успешном добавлении,
      * иначе если не найдет шмотку в бд, то 500 ошибка, сервак это дело пока не обрабатывает(
      * также, если шмотка уже была добавлена, то увидишь сообщение, что мол ты ее уже добавлял
     */
    @PostMapping("/add/shopstuff")
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    public ResponseEntity<?> addShopsStuffInWardrobe(@RequestParam("id") Long stuffId){

        Boolean result = wardrobeService.addShopsStuffInWardrobe(stuffId);

        // Если пользователь был добавлен успешно
        if (Boolean.TRUE.equals(result)){
            return ResponseEntity.ok(new MessageResponse("Stuff ADDED"));
        }

        // Иначе
        return ResponseEntity.badRequest().body(new MessageResponse("Stuff has already been added"));
    }

    /** Сохранение шмотки пользователя в гардероб
     *
     * @param stuffInfo тип хранения, id шмотки, фото(если, нужно) + нужен токен!
     * @return если Алах был доволен тобой и комета минула Землю, то вернет сообщение об успешном добавлении,
     * иначе если не найдет шмотку в бд, то 500 ошибка, сервак это дело пока не обрабатывает(
     * также, если шмотка уже была добавлена, то увидишь сообщение, что мол ты ее уже добавлял
     */
    @PostMapping("/add/userstuff")
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    public ResponseEntity<?> addUsersStuffInWardrobe(@RequestPart("info") UserStuff stuffInfo,
                                                @RequestPart("file") @Valid MultipartFile img) throws IOException {

        Boolean result = wardrobeService.addUsersStuffInWardrobe(stuffInfo, img);

        // Если пользователь был добавлен успешно
        if (Boolean.TRUE.equals(result)){
            return ResponseEntity.ok(new MessageResponse("Stuff ADDED"));
        }

        // Иначе
        return ResponseEntity.badRequest().body(new MessageResponse("Stuff has already been added"));
    }

    /** Удалить шмотку из гардероба пользователя
     *
     * @param stuffId id шмотки, фото(если, нужно) + нужен токен!
     * @return если черная кошка не преградила путь запросу, то вернет сообщение об успешном удалении,
     * иначе если не найдет шмотку в бд, то 500 ошибка, сервак это дело пока не обрабатывает(
     * также, если шмотка уже была удалена/не добавлялась вовсе, то увидишь сообщение, что мол незя так
     */
    @PostMapping("/delete/userstuff")
    @PreAuthorize("hasRole('USER') or HasRole('MODERATOR') or hasRole('ADMIN')")
    public ResponseEntity<?> deleteUsersStuffInWardrobe(@RequestParam("id") Long stuffId) throws IOException {

        Boolean result = wardrobeService.deleteUsersStuffInWardrobe(stuffId);

        // Если пользователь был удалён успешно
        if (Boolean.TRUE.equals(result)){
            return ResponseEntity.ok(new MessageResponse("Stuff DELETED"));
        }
        // Иначе
        return ResponseEntity.ok(new MessageResponse("Stuff was not removed because it was not in your wardrobe"));
    }

    /** Удалить шмотку из гардероба магазина
     *
     * @param id тип хранения, id шмотки, фото(если, нужно) + нужен токен!
     * @return если черная кошка не преградила путь запросу, то вернет сообщение об успешном удалении,
     * иначе если не найдет шмотку в бд, то 500 ошибка, сервак это дело пока не обрабатывает(
     * также, если шмотка уже была удалена/не добавлялась вовсе, то увидишь сообщение, что мол незя так
     */
    @PostMapping("/delete/shopstuff")
    @PreAuthorize("hasRole('USER') or HasRole('MODERATOR') or hasRole('ADMIN')")
    public ResponseEntity<?> deleteShopsStuffInWardrobe(@RequestParam Long id){

        Boolean result = wardrobeService.deleteShopsStuffInWardrobe(id);

        // Если пользователь был удалён успешно
        if (Boolean.TRUE.equals(result)){
            return ResponseEntity.ok(new MessageResponse("Stuff DELETED"));
        }
        // Иначе
        return ResponseEntity.ok(new MessageResponse("Stuff was not removed because it was not in your wardrobe"));
    }
}
