package com.knowledge.designpattern.createtype.simplefactory.order;

import com.knowledge.designpattern.createtype.simplefactory.pizza.Pizza;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * @description:
 * @projectName:batch-processing
 * @see:designpattern.simplefactory.order
 * @author:xiaoyige
 * @createTime:2021/11/13 22:38
 * @version:1.0
 */
public class OrderPizza {
    SimpleFactory simpleFactory;
    Pizza pizza;

    public OrderPizza(SimpleFactory simpleFactory) {
        setSimpleFactory(simpleFactory);
    }

    public void setSimpleFactory(SimpleFactory simpleFactory) {
        String orderType = "";
        this.simpleFactory = simpleFactory;
        do {
            orderType = getPizzaType();
            pizza = simpleFactory.createPizza(orderType);
            if (pizza != null) {
                pizza.prepare();
                pizza.bake();
                pizza.cut();
                pizza.bax();
            } else {
                System.out.println("订购失败！");
                break;
            }
        } while (true);
    }

    //    获取披萨种类
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
