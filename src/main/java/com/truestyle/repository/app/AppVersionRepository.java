package com.truestyle.repository.app;

import com.truestyle.entity.app.AppVersion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AppVersionRepository extends JpaRepository<AppVersion, Long> {

    AppVersion findTopByOrderByIdDesc();

}
