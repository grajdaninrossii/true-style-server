package com.truestyle.repository.stuff;

import com.truestyle.entity.stuff.UserStuff;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface StuffUserRepository extends JpaRepository<UserStuff, Long> {

    Optional<UserStuff> findById(Long id);
    Optional<UserStuff> findByImageUrl(String url);
}
