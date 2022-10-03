package com.truestyle.service;


import com.truestyle.entity.stuff.ShopStuff;
import com.truestyle.entity.user.Gender;
import com.truestyle.entity.user.User;
import com.truestyle.repository.user.GenderRepository;
import com.truestyle.repository.stuff.StuffShopRepository;
import com.truestyle.repository.user.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Arrays;
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

    private void fillUniqueData(){
        assert shopStuffRepository != null;

        authentication = SecurityContextHolder.getContext().getAuthentication();

        User user = userRepository.findByUsername(authentication.getName()).orElseThrow(() -> new RuntimeException("User is not found"));
        gender = user.getGender();

        articleTypes = Arrays.asList("Accessory Gift Set", "Baby Dolls", "Backpacks", "Bangle", "Basketballs", "Bath Robe", "Beauty Accessory", "Belts", "Blazers", "Body Lotion", "Booties", "Boxers", "Bra", "Bracelet", "Briefs", "Camisoles", "Capris", "Caps", "Casual Shoes", "Churidar", "Clothing Set", "Clutches", "Compact", "Concealer", "Cufflinks", "Deodorant", "Dresses", "Duffel Bag", "Dupatta", "Earrings", "Eye Cream", "Eyeshadow", "Face Moisturisers", "Face Scrub and Exfoliator", "Face Serum and Gel", "Face Wash and Cleanser", "Flats", "Flip Flops", "Footballs", "Formal Shoes", "Foundation and Primer", "Fragrance Gift Set", "Free Gifts", "Gloves", "Hair Colour", "Handbags", "Hat", "Headband", "Heels", "Highlighter and Blush", "Innerwear Vests", "Ipad", "Jackets", "Jeans", "Jeggings", "Jewellery Set", "Jumpsuit", "Kajal and Eyeliner", "Key chain", "Kurta Sets", "Kurtas", "Kurtis", "Laptop Bag", "Leggings", "Lehenga Choli", "Lip Care", "Lip Gloss", "Lip Liner", "Lip Plumper", "Lipstick", "Lounge Pants", "Lounge Shorts", "Lounge Tshirts", "Makeup Remover", "Mascara", "Mask and Peel", "Mens Grooming Kit", "Messenger Bag", "Mobile Pouch", "Mufflers", "Nail Essentials", "Nail Polish", "Necklace and Chains", "Nehru Jackets", "Night suits", "Nightdress", "Patiala", "Pendant", "Perfume and Body Mist", "Rain Jacket", "Rain Trousers", "Ring", "Robe", "Rompers", "Rucksacks", "Salwar", "Salwar and Dupatta", "Sandals", "Sarees", "Scarves", "Shapewear", "Shirts", "Shoe Accessories", "Shoe Laces", "Shorts", "Shrug", "Skirts", "Socks", "Sports Sandals", "Sports Shoes", "Stockings", "Stoles", "Sunglasses", "Sunscreen", "Suspenders", "Sweaters", "Sweatshirts", "Swimwear", "Tablet Sleeve", "Ties", "Ties and Cufflinks", "Tights", "Toner", "Tops", "Track Pants", "Tracksuits", "Travel Accessory", "Trolley Bag", "Trousers", "Trunk", "Tshirts", "Tunics", "Umbrellas", "Waist Pouch", "Waistcoat", "Wallets", "Watches");
//        gender = (List<Gender>) genderRepository.findAll();
    }

    public ShopStuff findById(Long id){
        return shopStuffRepository.findById(id).orElseThrow(() -> new RuntimeException("Error, Stuff is not found"));
    }

    // Получение характеристик шмоток из фотки
    private String getStuffCharacters(Integer data){
        if (articleTypes == null){
            fillUniqueData();
        }

        return articleTypes.get(data);
    }

    // Получение шмоток по характеристикам
    public List<ShopStuff> getStuffML(Integer data){

        String resultClass = getStuffCharacters(data);
        System.out.println(resultClass);

        List<ShopStuff> shopsStuffs;

        if (gender == null){
            shopsStuffs = shopStuffRepository.findCVStuffWithoutGender(resultClass);
            return shopsStuffs;
        } else {
            shopsStuffs = shopStuffRepository.findCVStuff(resultClass, gender.getId());
        }

        if (shopsStuffs.isEmpty()){
            shopsStuffs = shopStuffRepository.findCVStuffWithoutGender(resultClass);
            return shopsStuffs;
        }
        return shopsStuffs;
    }

    public String addOneStuff(ShopStuff shopsStuff) {
        shopStuffRepository.save(shopsStuff);
        return "ok";
    }

    public String addListStuff(List<ShopStuff> shopsStuffs) {
        shopStuffRepository.saveAll(shopsStuffs);
        return "ok";
    }

    // На вход - id вещи
    // на выход - объект вещи
    ShopStuff getOneStuff(Long id) {
        final Optional<ShopStuff> optStuff = shopStuffRepository.findById(id);
        return optStuff.orElse(null);
    }

    public List<ShopStuff> getStuffBySeason(String season){
        return shopStuffRepository.findAllByOrderSeason(season);
    }

    public List<ShopStuff> getStuffByRecommended() {
        return shopStuffRepository.findByRecommended();
    }

    // Получить все записи (все вещи)
    List<ShopStuff> getAllStuff(){
        return shopStuffRepository.findAllByOrderByIdDesc();
    }

    public String createAndAddStuff(String productDisplayName, Gender gender, String masterCategory,
                     String subCategory, String articleType, String base_color,
                     String season, String usage, String imageUrl) {

        ShopStuff oneShopsStuff = new ShopStuff();
        oneShopsStuff.setProductDisplayName(productDisplayName);
        oneShopsStuff.setArticleType(articleType);
        oneShopsStuff.setGender(gender);
        oneShopsStuff.setMasterCategory(masterCategory);
        oneShopsStuff.setBaseColor(base_color);
        oneShopsStuff.setSeason(season);
        oneShopsStuff.setUsage(usage);
        oneShopsStuff.setImageUrl(imageUrl);

        try {
            shopStuffRepository.save(oneShopsStuff);
        } catch (Exception e) {
            log.error("Stuff did not add", e);
        }

        return "Stuff created";
    }

}
