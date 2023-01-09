package com.java8;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @ClassName Dish
 * @Author qianchao
 * @Date 2022/12/9
 * @Version   V1.0
 **/

@Data
public class Dish {


    private String name;
    //素食主义者
    private boolean vegetarian;
    //卡路里
    private int calories;


    private final DishType dishType;

    public Dish(String name, boolean vegetarian, int calories, DishType type) {
        this.name = name;
        this.vegetarian = vegetarian;
        this.calories = calories;
        this.dishType = type;
    }

    public enum DishType {
        MEAT,
        FISH,
        OTHER
    }

}
