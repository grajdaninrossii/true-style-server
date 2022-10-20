package com.truestyle.service.app;

import com.truestyle.entity.app.AppVersion;
import com.truestyle.repository.app.AppVersionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AppVersionService {

    private final AppVersionRepository appVersionRepository;

    public AppVersion getActualAppVersion(){
        return appVersionRepository.findTopByOrderByIdDesc();
    }
}
