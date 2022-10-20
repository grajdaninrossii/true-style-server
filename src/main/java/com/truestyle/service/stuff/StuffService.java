package com.truestyle.service.stuff;


import com.truestyle.entity.stuff.ShopStuff;
import com.truestyle.entity.stuff.UserStuff;
import com.truestyle.entity.user.User;
import com.truestyle.pojo.ShopStuffCVData;
import com.truestyle.repository.user.GenderRepository;
import com.truestyle.repository.stuff.StuffShopRepository;
import com.truestyle.repository.user.UserRepository;
import com.truestyle.service.user.SecurityService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class StuffService {

    private final StuffShopRepository shopStuffRepository;

    private final UserRepository userRepository;

    private final GenderRepository genderRepository;

    private final SecurityService auth;

    private static final List<String> MONTH = Arrays.asList("зима", "зима", "весна", "весна", "весна", "лето", "лето", "лето", "осень", "осень", "осень", "зима");


    public ShopStuff findById(Long id){
        return shopStuffRepository.findById(id).orElseThrow(() -> new RuntimeException("Error, Stuff is not found"));
    }

    // Получение шмоток по характеристикам
    public List<ShopStuff> getStuffMLRRecommendation(ShopStuffCVData data){

        List<ShopStuff> shopsStuffs;
        if (data.getGender().getId() != 0){
            shopsStuffs = shopStuffRepository.findCVStuff(data.getArticleType(), data.getGender(), data.getSeason(), data.getBaseColor());
        } else {
            shopsStuffs = shopStuffRepository.findCVWithoutGenderStuff(data.getArticleType(), data.getSeason(), data.getBaseColor());
        }
        return shopsStuffs;
    }

    // На вход - id вещи
    // на выход - объект вещи
    ShopStuff getOneStuff(Long id) {
        final Optional<ShopStuff> optStuff = shopStuffRepository.findById(id);
        return optStuff.orElse(null);
    }

    public List<ShopStuff> getStuffByRecommended() {

        User user = auth.getAuthUser();
        List<UserStuff> userStuffs = user.getWardrobeUsers().stream().toList();

        String articleTypeTop1 = "";
        String articleTypeTop2 = "";
        List<String> artType;

        // Получаем сезоны для рекомендаций
        int nowMonth = LocalDateTime.now().getMonthValue();
        String season1 = MONTH.get(nowMonth);
        String season2 = Math.random() >= 0.5? "демисезон":MONTH.get((nowMonth + 3) % 12);

        // !!!!!!! Придумать, что делать с гендером и прописать логику articleType
        if (Math.random() >= 0.5){
            artType = userRepository.findAllArticleTypeInWardrobeUser(user.getId());
            System.out.println();
        } else {
            artType = userRepository.findAllArticleTypeInWardrobeUserDesc(user.getId());
        }

        if (!artType.isEmpty()){
            articleTypeTop1 = artType.get(0);
            if (artType.size() > 2){
                articleTypeTop2 = artType.get(1);
            }
        }

        Long gender = userRepository.findAllGenderStuffWardrobeUser(user.getId());
        if (gender == null){
            return shopStuffRepository.findByRecommendedWithoutGender(articleTypeTop1, articleTypeTop2, season1, season2);
        }

        return shopStuffRepository.findByRecommended(articleTypeTop1, articleTypeTop2, season1, season2, genderRepository.getById(gender));
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

    //    public String addOneStuff(ShopStuff shopsStuff) {
//        shopStuffRepository.save(shopsStuff);
//        return "ok";
//    }
//
//    public String addListStuff(List<ShopStuff> shopsStuffs) {
//        shopStuffRepository.saveAll(shopsStuffs);
//        return "ok";
//    }

    // Получение характеристик шмоток из фотки
//    private String getStuffCharacters(Integer data){
//        if (articleTypes == null){
//            fillUniqueData();
//        }
//
//        return articleTypes.get(data);
//    }


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


//    public List<ShopStuff> getStuffBySeason(String season){
//        return shopStuffRepository.findAllByOrderSeason(season);
//    }

}
