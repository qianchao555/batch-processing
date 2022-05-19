package com.designpattern.createtype.builders;

import com.designpattern.createtype.builders.cars.CarType;
import com.designpattern.createtype.builders.components.Engine;
import com.designpattern.createtype.builders.components.GPSNavigator;
import com.designpattern.createtype.builders.components.Transmission;
import com.designpattern.createtype.builders.components.TripComputer;

/**
 * 通用生成器接口
 * @Author qianchao
 * @Date 2021/11/15
 * @Version designpattern V1.0
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
