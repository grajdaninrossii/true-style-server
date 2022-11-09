package com.truestyle.repository.user;

import com.truestyle.entity.user.Gender;
import com.truestyle.entity.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUsername(String username);
    Optional<User> findByEmail(String email);

    Boolean existsByUsername(String username);
    Boolean existsByEmail(String email);

    @Query(value = "SELECT article_type FROM " +
            "(SELECT article_type " +
            "FROM (wardrobe_from_users w join user_stuff u on u.id = w.users_stuff_id) AS u " +
            "WHERE u.user_id = ?1 " +
            "UNION ALL " +
            "SELECT article_type " +
            "FROM (wardrobe_from_shops w join shop_stuff u on u.id = w.shops_stuff_id) AS u " +
            "WHERE u.user_id = ?1" +
            ") as art " +
            "GROUP BY article_type " +
            "ORDER BY count(article_type) " +
            "LIMIT 2",
            nativeQuery = true)
    List<String> findAllArticleTypeInWardrobeUser(Long userId);

    @Query(value = "SELECT article_type FROM " +
            "(SELECT article_type " +
            "FROM (wardrobe_from_users w join user_stuff u on u.id = w.users_stuff_id) AS u " +
            "WHERE u.user_id = ?1 " +
            "UNION ALL " +
            "SELECT article_type " +
            "FROM (wardrobe_from_shops w join shop_stuff u on u.id = w.shops_stuff_id) AS u " +
            "WHERE u.user_id = ?1" +
            ") as art " +
            "GROUP BY article_type " +
            "ORDER BY count(article_type) DESC " +
            "LIMIT 2",
            nativeQuery = true)
    List<String> findAllArticleTypeInWardrobeUserDesc(Long userId);

    @Query(value = "SELECT gender_id FROM " +
            "(SELECT gender_id " +
            "FROM (wardrobe_from_users w join user_stuff u on u.id = w.users_stuff_id) AS u " +
            "WHERE u.user_id = ?1 " +
            "UNION ALL " +
            "SELECT gender_id " +
            "FROM (wardrobe_from_shops w join shop_stuff u on u.id = w.shops_stuff_id) AS u " +
            "WHERE u.user_id = ?1" +
            ") as g " +
            "GROUP BY gender_id " +
            "ORDER BY count(gender_id) DESC " +
            "LIMIT 1",
            nativeQuery = true)
    Long findAllGenderStuffWardrobeUser(Long userId);

    @Query(value = "SELECT EXISTS(SELECT * FROM wardrobe_from_users WHERE user_id = ?1 and users_stuff_id = ?2)",
            nativeQuery = true)
    Boolean existsStuffInUsersWardrobe(Long userId, Long stuffId);

    @Query(value = "SELECT EXISTS(SELECT * FROM wardrobe_from_shops WHERE user_id = ?1 and shops_stuff_id = ?2)",
            nativeQuery = true)
    Boolean existsStuffInShopsWardrobe(Long userId, Long stuffId);

    @Query(value = "SELECT * FROM users WHERE username = ?1 or email = ?2",
            nativeQuery = true)
    Optional<User> findByLogin(String username, String email);

    @Query(value = "SELECT EXISTS(SELECT * FROM users WHERE username = ?1 or email = ?2)",
            nativeQuery = true)
    Boolean existsByLoginOrEmail(String username, String email);

}