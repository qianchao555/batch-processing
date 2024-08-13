package com.knowledge.designpattern.createtype.builders.components;

import com.knowledge.designpattern.createtype.builders.cars.Car;

/**
 * 汽车出行电脑
 * @ClassName TripComputer
 * @Author qianchao
 * @Date 2021/11/15
 * @Version designpattern V1.0
 **/
public class TripComputer {
    private Car car;

    public void setCar(Car car) {
        this.car = car;
    }

    public void showFuelLevel() {
        System.out.println("Fuel level: " + car.getFuel());
    }

    public void showStatus() {
        if (this.car.getEngine().isStarted()) {
            System.out.println("Car is started");
        } else {
            System.out.println("Car isn't started");
        }
    }
}
