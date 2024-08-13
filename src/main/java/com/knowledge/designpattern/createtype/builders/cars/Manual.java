package com.knowledge.designpattern.createtype.builders.cars;

import com.knowledge.designpattern.createtype.builders.components.Engine;
import com.knowledge.designpattern.createtype.builders.components.GPSNavigator;
import com.knowledge.designpattern.createtype.builders.components.Transmission;
import com.knowledge.designpattern.createtype.builders.components.TripComputer;

/**
 * 汽车手册是另一个产品。注意，它与Car没有相同的祖先。他们没有血缘关系
 * @ClassName Manual
 * @Author qianchao
 * @Date 2021/11/15
 * @Version designpattern V1.0
 **/
public class Manual {
    private final CarType carType;
    private final int seats;
    private final Engine engine;
    private final Transmission transmission;
    private final TripComputer tripComputer;
    private final GPSNavigator gpsNavigator;

    public Manual(CarType carType, int seats, Engine engine, Transmission transmission, TripComputer tripComputer, GPSNavigator gpsNavigator) {
        this.carType = carType;
        this.seats = seats;
        this.engine = engine;
        this.transmission = transmission;
        this.tripComputer = tripComputer;
        this.gpsNavigator = gpsNavigator;
    }

    public String print() {
        String info = "";
        info += "Type of car: " + carType + "\n";
        info += "Count of seats: " + seats + "\n";
        info += "Engine: volume - " + engine.getVolume() + "; mileage - " + engine.getMileage() + "\n";
        info += "Transmission: " + transmission + "\n";
        if (this.tripComputer != null) {
            info += "Trip Computer: Functional" + "\n";
        } else {
            info += "Trip Computer: N/A" + "\n";
        }
        if (this.gpsNavigator != null) {
            info += "GPS Navigator: Functional" + "\n";
        } else {
            info += "GPS Navigator: N/A" + "\n";
        }
        return info;
    }
}
