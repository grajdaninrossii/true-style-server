package com.truestyle.pojo;

import com.truestyle.entity.stuff.ShopStuff;
import com.truestyle.entity.stuff.UserStuff;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class WardrobeResponse {

    private List<ShopStuff> shopsStuffs;
    private List<UserStuff> usersStuffs;
}
