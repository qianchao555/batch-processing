package designpattern.builders;

import designpattern.builders.cars.CarType;
import designpattern.builders.components.Engine;
import designpattern.builders.components.GPSNavigator;
import designpattern.builders.components.Transmission;
import designpattern.builders.components.TripComputer;

/**
 * 通用生成器接口
 * @Author qianchao
 * @Date 2021/11/15
 * @Version OPRA V1.0
 **/
public interface Builder {
    //构建器接口：定义配置产品的所有可能步骤

    //汽车类型
    void setCarType(CarType carType);
    //汽车座位
    void setSeats(int seats);
    //汽车组件：引擎、变速器
    void setEngine(Engine engine);
    void setTransmission(Transmission transmission);
    //汽车带的功能：旅行电脑、GPS
    void setTripComputer(TripComputer tripComputer);
    void setGPSNavigator(GPSNavigator gpsNavigator);
}
