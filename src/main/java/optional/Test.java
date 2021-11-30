package optional;

import java.util.Optional;

/**
 * @ClassName Test
 * @Author qianchao
 * @Date 2021/11/5
 * @Version OPRA V1.0
 **/
public class Test {


    public static void main(String[] args) {
        Car car=new Car();
        Optional<Car> optionalCar=Optional.empty();
        Optional<Car> optionalCar2=Optional.of(car);
        System.out.println(optionalCar);
        System.out.println(optionalCar2);
        Optional<Car> optionalCar3=Optional.ofNullable(car);
        System.out.println(optionalCar3);

    }

}
