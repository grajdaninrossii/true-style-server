package com.truestyle.controller;

import com.truestyle.entity.app.Advertisement;
import com.truestyle.entity.app.AppVersion;
import com.truestyle.service.app.AdvertisementService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("/ad")
@RequiredArgsConstructor
public class AdvertisementController {
    private final AdvertisementService advertisementService;

    @GetMapping("/get")
    public List<Advertisement> getVersionInfo(){
        return advertisementService.getAds();
    }
}
