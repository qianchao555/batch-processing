package designpattern.builders.director;

import designpattern.builders.Builder;
import designpattern.builders.cars.CarType;
import designpattern.builders.components.Engine;
import designpattern.builders.components.GPSNavigator;
import designpattern.builders.components.Transmission;
import designpattern.builders.components.TripComputer;

/**
 * Director定义了构建步骤的顺序。
 * 它通过通用builder接口与构建器对象一起工作。因此，它可能不知道正在构建的产品是什么
 *
 * 同样的构建过程，但是创建的是不同的表示
 *
 * @ClassName Director
 * @Author qianchao
 * @Date 2021/11/15
 * @Version OPRA V1.0
 **/
public class Director {
    public void constructSportsCar(Builder builder) {
        builder.setCarType(CarType.SPORTS_CAR);
        builder.setSeats(2);
        builder.setEngine(new Engine(3.0, 0));
        builder.setTransmission(Transmission.SEMI_AUTOMATIC);
        builder.setTripComputer(new TripComputer());
        builder.setGPSNavigator(new GPSNavigator());
    }

    public void constructCityCar(Builder builder) {
        builder.setCarType(CarType.CITY_CAR);
        builder.setSeats(2);
        builder.setEngine(new Engine(1.2, 0));
        builder.setTransmission(Transmission.AUTOMATIC);
        builder.setTripComputer(new TripComputer());
        builder.setGPSNavigator(new GPSNavigator());
    }

    public void constructSUV(Builder builder) {
        builder.setCarType(CarType.SUV);
        builder.setSeats(4);
        builder.setEngine(new Engine(2.5, 0));
        builder.setTransmission(Transmission.MANUAL);
        builder.setGPSNavigator(new GPSNavigator());
    }
}
