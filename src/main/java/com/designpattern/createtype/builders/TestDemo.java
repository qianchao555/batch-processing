package com.designpattern.createtype.builders;

import com.designpattern.createtype.builders.cars.Car;
import com.designpattern.createtype.builders.cars.Manual;
import com.designpattern.createtype.builders.director.Director;

/**
 * @ClassName Test
 * @Author qianchao
 * @Date 2021/11/15
 * @Version designpattern V1.0
 **/
public class TestDemo {
    public static void main(String[] args) {
        Director director = new Director();

        /**
         * Director从客户端获取具体的构建器对象(应用程序代码)。这是因为应用程序更好地知道是哪个建设者使用得到一个特定的产品
         */
        CarBuilder builder = new CarBuilder();
        director.constructSUV(builder);

        /**
         * 最终产品通常从构建器对象检索，因为Director不知道，也不依赖于具体构建器和产品。
         */
        Car car = builder.getResult();
        System.out.println("构建汽车:\n" + car.getCarType());
        System.out.println(car);


        CarManualBuilder manualBuilder = new CarManualBuilder();

        //主管可能知道几个建筑方法
        director.constructSportsCar(manualBuilder);
        Manual carManual = manualBuilder.getResult();
        System.out.println("\n汽车手册构建:\n" + carManual.print());
    }
}
