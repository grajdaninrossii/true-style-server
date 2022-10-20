package com.truestyle.repository.stuff;

import com.truestyle.entity.stuff.UserStuff;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface StuffUserRepository extends JpaRepository<UserStuff, Long> {

    Optional<UserStuff> findById(Long id);
    Optional<UserStuff> findByImageUrl(String url);

    @Query(value = "select count(user_id) from wardrobe_from_users where user_id = ?1 group by user_id",
            nativeQuery = true)
    Integer countUserStuff(Long userId);
}
