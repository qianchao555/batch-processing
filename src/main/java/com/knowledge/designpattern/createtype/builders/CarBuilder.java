package com.knowledge.designpattern.createtype.builders;

import com.knowledge.designpattern.createtype.builders.cars.Car;
import com.knowledge.designpattern.createtype.builders.cars.CarType;
import com.knowledge.designpattern.createtype.builders.components.Engine;
import com.knowledge.designpattern.createtype.builders.components.GPSNavigator;
import com.knowledge.designpattern.createtype.builders.components.Transmission;
import com.knowledge.designpattern.createtype.builders.components.TripComputer;

/**
 * 骑车生成器
 * 具体构建器实现公共接口中定义的步骤
 *
 * @ClassName CarBuilder
 * @Author qianchao
 * @Date 2021/11/15
 * @Version designpattern V1.0
 **/
public class CarBuilder implements Builder {
    private CarType type;
    private int seats;
    private Engine engine;
    private Transmission transmission;
    private TripComputer tripComputer;
    private GPSNavigator gpsNavigator;

    public void setCarType(CarType carType) {
        this.type = carType;
    }

    public void setSeats(int seats) {
        this.seats = seats;
    }

    public void setEngine(Engine engine) {
        this.engine = engine;
    }

    public void setTransmission(Transmission transmission) {
        this.transmission = transmission;
    }

    public void setTripComputer(TripComputer tripComputer) {
        this.tripComputer = tripComputer;
    }

    public void setGPSNavigator(GPSNavigator gpsNavigator) {
        this.gpsNavigator = gpsNavigator;
    }

    public Car getResult() {
        return new Car(type, seats, engine, transmission, tripComputer, gpsNavigator);
    }
}
