package com.truestyle.service;


import com.truestyle.entity.stuff.ShopStuff;
import com.truestyle.entity.user.Gender;
import com.truestyle.pojo.ShopStuffCVData;
import com.truestyle.repository.user.GenderRepository;
import com.truestyle.repository.stuff.StuffShopRepository;
import com.truestyle.repository.user.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class StuffService {

    @Autowired
    StuffShopRepository shopStuffRepository;

    @Autowired
    GenderRepository genderRepository;

    @Autowired
    UserRepository userRepository;

    List<String> articleTypes;
    Gender gender;

    Authentication authentication;

//    private void fillUniqueData(){
//        assert shopStuffRepository != null;
//
//        authentication = SecurityContextHolder.getContext().getAuthentication();
//
//        User user = userRepository.findByUsername(authentication.getName()).orElseThrow(() -> new RuntimeException("User is not found"));
//        gender = user.getGender();
//
////        articleTypes = Arrays.asList("Футболка", "Брюки", "Платье", "Куртка/Парка/Пуховик", "Шорты", "Джемпер", "Блуза", "Джинсы", "Юбка", "Рубашка", "Купальник", "Рюкзак", "Топ", "Худи", "Комбинезон", "Майка", "Жилет", "Бюстгальтер", "Свитер", "Носки", "Мешок", "Шапка", "Шуба", "Халат", "Пиджак", "Перчатки", "Бейсболка", "Плавки", "Сумка", "Пальто", "Ветровка", "Тайтсы/Легинсы");
////        gender = (List<Gender>) genderRepository.findAll();
//    }

    public ShopStuff findById(Long id){
        return shopStuffRepository.findById(id).orElseThrow(() -> new RuntimeException("Error, Stuff is not found"));
    }

    // Получение характеристик шмоток из фотки
//    private String getStuffCharacters(Integer data){
//        if (articleTypes == null){
//            fillUniqueData();
//        }
//
//        return articleTypes.get(data);
//    }

    // Получение шмоток по характеристикам
    public List<ShopStuff> getStuffMLReccomendation(ShopStuffCVData data){

//        System.out.println(data.getArticleType());

        List<ShopStuff> shopsStuffs;
        shopsStuffs = shopStuffRepository.findCVStuff(data.getArticleType(), data.getGender(), data.getSeason(), data.getBaseColor());
        return shopsStuffs;
    }

//    public String addOneStuff(ShopStuff shopsStuff) {
//        shopStuffRepository.save(shopsStuff);
//        return "ok";
//    }
//
//    public String addListStuff(List<ShopStuff> shopsStuffs) {
//        shopStuffRepository.saveAll(shopsStuffs);
//        return "ok";
//    }

    // На вход - id вещи
    // на выход - объект вещи
    ShopStuff getOneStuff(Long id) {
        final Optional<ShopStuff> optStuff = shopStuffRepository.findById(id);
        return optStuff.orElse(null);
    }

//    public List<ShopStuff> getStuffBySeason(String season){
//        return shopStuffRepository.findAllByOrderSeason(season);
//    }

    public List<ShopStuff> getStuffByRecommended() {
        return shopStuffRepository.findByRecommended();
    }

    // Получить все записи (все вещи)
//    List<ShopStuff> getAllStuff(){
//        return shopStuffRepository.findAllByOrderByIdDesc();
//    }

//    public String createAndAddStuff(String productDisplayName, Gender gender, String masterCategory,
//                     String subCategory, String articleType, String base_color,
//                     String season, String usage, String imageUrl) {
//
//        ShopStuff oneShopsStuff = new ShopStuff();
//        oneShopsStuff.setProductDisplayName(productDisplayName);
//        oneShopsStuff.setArticleType(articleType);
//        oneShopsStuff.setGender(gender);
//        oneShopsStuff.setMasterCategory(masterCategory);
//        oneShopsStuff.setBaseColor(base_color);
//        oneShopsStuff.setSeason(season);
//        oneShopsStuff.setUsage(usage);
//        oneShopsStuff.setImageUrl(imageUrl);
//
//        try {
//            shopStuffRepository.save(oneShopsStuff);
//        } catch (Exception e) {
//            log.error("Stuff did not add", e);
//        }
//
//        return "Stuff created";
//    }

}
