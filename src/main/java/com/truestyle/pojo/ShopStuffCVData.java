package com.truestyle.pojo;

import com.truestyle.entity.user.Gender;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ShopStuffCVData {

    String articleType;
    Gender gender;
    String season;
    String baseColor;

}
