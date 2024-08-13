package com.knowledge.designpattern.createtype.simplefactory.order;

import com.knowledge.designpattern.createtype.simplefactory.pizza.CheesePizza;
import com.knowledge.designpattern.createtype.simplefactory.pizza.ChinaPizza;
import com.knowledge.designpattern.createtype.simplefactory.pizza.GreekPizza;
import com.knowledge.designpattern.createtype.simplefactory.pizza.Pizza;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * @description:简单工厂类
 * @projectName:batch-processing
 * @see:designpattern.simplefactory.order
 * @author:xiaoyige
 * @createTime:2021/11/13 23:10
 * @version:1.0
 */
public class SimpleFactory {
    public Pizza createPizza(String orderType) {
        System.out.println("使用简单工厂模式：");
        Pizza pizza = null;
        if (orderType.equals("greek")) {
            pizza = new GreekPizza();
            pizza.setName("希腊披萨");
        } else if (orderType.equals("cheese")) {
            pizza = new CheesePizza();
            pizza.setName("奶酪披萨");
        }else if (orderType.equals("china")){
            pizza=new ChinaPizza();
            pizza.setName("china");
        }
        return pizza;
    }

    //获取披萨种类
    private String getPizzaType() {
        BufferedReader str = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("输入piza类型：");
        try {
            String pizzaType = str.readLine();
            return pizzaType;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";

    }
}
