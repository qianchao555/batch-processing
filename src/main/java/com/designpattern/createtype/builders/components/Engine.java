package com.designpattern.createtype.builders.components;

/**
 * 特征产品-引擎
 * @ClassName Engine
 * @Author qianchao
 * @Date 2021/11/15
 * @Version designpattern V1.0
 **/
public class Engine {
    private final double volume;
    private double mileage;
    private boolean started;

    public Engine(double volume, double mileage) {
        this.volume = volume;
        this.mileage = mileage;
    }
    public void on() {
        started = true;
    }

    public void off() {
        started = false;
    }
    public boolean isStarted() {
        return started;
    }

    public void go(double mileage) {
        if (started) {
            this.mileage += mileage;
        } else {
            System.err.println("Cannot go(), you must start engine first!");
        }
    }

    public double getVolume() {
        return volume;
    }

    public double getMileage() {
        return mileage;
    }
}
