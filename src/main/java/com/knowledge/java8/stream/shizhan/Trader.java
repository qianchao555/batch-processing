package com.knowledge.java8.stream.shizhan;

/**
 * @ClassName Trader
 * @Author qianchao
 * @Date 2021/11/3
 * @Version java8 V1.0
 **/
public class Trader {
    private final String name;
    private final String city;

    public Trader(String n, String c) {
        this.name = n;
        this.city = c;
    }

    public String getName() {
        return this.name;
    }

    public String getCity() {
        return this.city;
    }

    public String toString() {
        return "Trader:" + this.name + " in " + this.city;
    }
}

