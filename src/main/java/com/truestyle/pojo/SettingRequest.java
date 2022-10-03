package com.truestyle.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SettingRequest {

    // Хранение номера телефона
    private String fullNumber;

    // Гендер
    private Long gender;

    // Страна
    private String country;

    // Фото(ссылка на фото)
    private String photoUrl;

    // Слоган
    private String stylePhrase;

}
