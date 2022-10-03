package com.truestyle.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class NewToken {

    private String token;
    private String username;
}