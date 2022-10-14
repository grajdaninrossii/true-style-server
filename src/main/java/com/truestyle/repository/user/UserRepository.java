package com.truestyle.repository.user;

import com.truestyle.entity.user.Gender;
import com.truestyle.entity.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUsername(String username);
    Optional<User> findByEmail(String email);

    Boolean existsByUsername(String username);
    Boolean existsByEmail(String email);

    @Query(value = "select article_type " +
            "from (wardrobe_from_users w join user_stuff u on u.id = w.users_stuff_id) as u " +
            "where u.user_id = ?1 " +
            "group by article_type " +
            "order by count(article_type) " +
            "limit 2",
            nativeQuery = true)
    List<String> findAllArticleTypeInWardrobeUser(Long userId);

    @Query(value = "select article_type " +
            "from (wardrobe_from_users w join user_stuff u on u.id = w.users_stuff_id) as u " +
            "where u.user_id = ?1 " +
            "group by article_type " +
            "order by count(article_type) DESC " +
            "limit 2",
            nativeQuery = true)
    List<String> findAllArticleTypeInWardrobeUserDesc(Long userId);

    @Query(value = "select gender_id " +
            "from (wardrobe_from_users w join user_stuff u on u.id = w.users_stuff_id) as u " +
            "where u.user_id = ?1 " +
            "group by gender_id " +
            "order by count(gender_id) DESC " +
            "limit 1",
            nativeQuery = true)
    Long findAllGenderStuffWardrobeUser(Long userId);

    @Query(value = "select EXISTS(select * from wardrobe_from_users where user_id = ?1 and users_stuff_id = ?2)",
            nativeQuery = true)
    Boolean existsStuffInUsersWardrobe(Long userId, Long stuffId);

    @Query(value = "select EXISTS(select * from wardrobe_from_shops where user_id = ?1 and shops_stuff_id = ?2)",
            nativeQuery = true)
    Boolean existsStuffInShopsWardrobe(Long userId, Long stuffId);

    @Query(value = "select * from users where username = ?1 or email = ?2",
            nativeQuery = true)
    Optional<User> findByLogin(String username, String email);

}