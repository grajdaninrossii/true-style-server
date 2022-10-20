package com.truestyle.service.app;

import com.truestyle.entity.app.Article;
import com.truestyle.repository.app.ArticleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ArticleService {
    private final ArticleRepository articleRepository;

    public Article findById(Long id){
        return articleRepository.findById(id).orElseThrow(() -> new RuntimeException("Error, Stuff is not found"));
    }

    public String addOneArt(Article art) {
        articleRepository.save(art);
        return "ok";
    }

    public String addListArts(List<Article> arts) {
        articleRepository.saveAll(arts);
        return "ok";
    }

    public List<Article> getStuffByRecommendedThreeArt() {
        return articleRepository.findByRecommendedThreeArt();
    }

    public List<Article> getStuffByRecommendedFiveArt() {
        return articleRepository.findByRecommendedFiveArt();
    }

    public List<Article> getAllArts(){
        List<Article> target = new ArrayList<Article>();
        articleRepository.findAll().forEach(target::add);
        return target;
    }
}
