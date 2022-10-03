package com.truestyle.controller;

import com.truestyle.entity.stuff.ShopStuff;
import com.truestyle.pojo.MessageResponse;
import com.truestyle.pojo.ShopStuffCVData;
import com.truestyle.service.StuffService;
import com.truestyle.service.WardrobeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/clothes")
@CrossOrigin(origins = "*", maxAge = 3600)
public class StuffController {

    @Autowired
    WardrobeService wardrobeService;

    @Autowired
    StuffService stuffService;

    @PostMapping("/get/cv")
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    public List<ShopStuff> getCvStuff(@RequestBody ShopStuffCVData stuffData){
        return stuffService.getStuffMLReccomendation(stuffData);
    }

    /** Получить одежду для рекоммендаций
     *
     * нужен токен!
     * @return JSON(List<Stuff>)
     */
    @GetMapping("/recommended")
    public List<ShopStuff> getStuffRecommended(){
        return stuffService.getStuffByRecommended();
    }

    /** Лайкнуть шмотку
     *
     * @param id_stuff - id шмотки + нужен токен!
     * @return вернет сообщение с положительным результатом, иначе косяк сервака
     */
    @PostMapping("/like")
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    public ResponseEntity<?> likeStuff(@RequestParam Long id_stuff){
        ShopStuff shopsStuff = stuffService.findById(id_stuff);
        wardrobeService.likeStuff(shopsStuff);
        return ResponseEntity.ok(new MessageResponse("Stuff LIKED"));
    }

    /** Удалить шмотку из понравившегося
     *
     * @param id_stuff - id шмотки + нужен токен!
     * @return вернет сообщение с положительным результатом, иначе косяк сервака
     */
    @PostMapping("/dislike")
    @PreAuthorize("hasRole('USER') or HasRole('MODERATOR') or hasRole('ADMIN')")
    public ResponseEntity<?> dislikeStuff(@RequestParam Long id_stuff){
        ShopStuff shopsStuff = stuffService.findById(id_stuff);
        wardrobeService.dislikeStuff(shopsStuff);
        return ResponseEntity.ok(new MessageResponse("Stuff NOT LIKED"));
    }
}