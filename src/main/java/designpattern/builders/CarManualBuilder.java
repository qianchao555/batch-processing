package designpattern.builders;

import designpattern.builders.cars.CarType;
import designpattern.builders.cars.Manual;
import designpattern.builders.components.Engine;
import designpattern.builders.components.GPSNavigator;
import designpattern.builders.components.Transmission;
import designpattern.builders.components.TripComputer;

/**
 * 与其他创建模式不同，Builder可以构建不相关的产品，这些产品没有公共接口。
 *
 * 在本例中，我们使用与构建汽车相同的步骤构建汽车的用户手册。这样就可以生产特定车型的手册，配置不同的功能
 * @ClassName CarManualBuilder
 * @Author qianchao
 * @Date 2021/11/15
 * @Version OPRA V1.0
 **/
public class CarManualBuilder implements Builder{
    private CarType type;
    private int seats;
    private Engine engine;
    private Transmission transmission;
    private TripComputer tripComputer;
    private GPSNavigator gpsNavigator;

    public void setCarType(CarType carType) {
        this.type=carType;
    }

    public void setSeats(int seats) {
        this.seats=seats;
    }

    public void setEngine(Engine engine) {
        this.engine=engine;
    }

    public void setTransmission(Transmission transmission) {
        this.transmission=transmission;
    }

    public void setTripComputer(TripComputer tripComputer) {
    this.tripComputer=tripComputer;
    }

    public void setGPSNavigator(GPSNavigator gpsNavigator) {
        this.gpsNavigator=gpsNavigator;
    }
    public Manual getResult() {
        return new Manual(type, seats, engine, transmission, tripComputer, gpsNavigator);
    }
}
