package com.truestyle.service.app;

import com.truestyle.entity.app.Advertisement;
import com.truestyle.repository.app.AdvertisementRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AdvertisementService {

    private final AdvertisementRepository advertisementRepository;

    public List<Advertisement> getAds(){
        return advertisementRepository.findAll();
    }
}
