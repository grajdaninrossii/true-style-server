package com.truestyle.repository.app;

import com.truestyle.entity.app.Article;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface ArticleRepository extends JpaRepository<Article, Long> {

    @Query(value = "select * from article order by random() limit 3",
            nativeQuery = true)
    List<Article> findByRecommendedThreeArt();

    @Query(value = "select * from article order by random() limit 5",
            nativeQuery = true)
    List<Article> findByRecommendedFiveArt();

}
