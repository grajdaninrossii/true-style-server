package com.truestyle.repository.stuff;

import com.truestyle.entity.stuff.ShopStuff;
import com.truestyle.entity.user.Gender;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StuffShopRepository extends JpaRepository<ShopStuff, Long> {

    Optional<ShopStuff> findById(Long id);

    List<ShopStuff> findAllByOrderByIdDesc();
    Optional<ShopStuff> findByImageUrl(String url);


    @Query(value = "SELECT * FROM shop_stuff s WHERE s.season = ?1",
            nativeQuery = true)
    List<ShopStuff> findAllByOrderSeason(String season);

    @Query(value = "select * from shop_stuff order by random() limit 5",
           nativeQuery = true)
    List<ShopStuff> findByRecommended();

    @Query(value = "select DISTINCT season from shop_stuff",
            nativeQuery = true)
    List<String> findUniqueSeasons();

    @Query(value = "select DISTINCT article_type from shop_stuff order by article_type",
            nativeQuery = true)
    List<String> findArticleTypes();

    @Query(value = "select DISTINCT base_color from shop_stuff",
            nativeQuery = true)
    List<String> findColors();

    @Query(value = "select DISTINCT master_category from shop_stuff",
            nativeQuery = true)
    List<String> findMasterCategory();


    // Чекнуть запрос
//    @Query(value = "select * from stuff where article_type = ?1 and base_color =?2 and gender_id = ?3 and master_category = ?4 and season = ?5 and sub_category = ?6 limit 10",
//            nativeQuery = true)

    @Query(value = "select * from shop_stuff where article_type like %?1% and gender_id = ?2 and season like %?3% and base_color like %?4% limit 10",
            nativeQuery = true)
    List<ShopStuff> findCVStuff(String articleType, Gender gender, String season, String color);

//    @Query("select e from ShopStuff e "
//            +"where (e.articleType like '%:articleType%') "
//            +"and (:gender_id = '' or e.gender like '%:gender_id%') "
//            +"and (:season = '' or e.season like '%:season%') "
//            +"and (:baseColor = '' or e.baseColor like '%:baseColor%')")
//            List<ShopStuff> findCVStuff(@Param("articleType") String articleType,
//            @Param("gender") Gender gender,
//            @Param("season") String season,
//            @Param("baseColor") String baseColor);


}