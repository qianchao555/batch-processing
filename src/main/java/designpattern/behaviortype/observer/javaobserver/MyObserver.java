package designpattern.behaviortype.observer.javaobserver;

import java.util.Observable;
import java.util.Observer;

/**
 * 观察者 实现了Java自带的观察者
 *
 * @Author qianchao
 * @Date 2021/11/29
 * @Version java8 V1.0
 **/
public class MyObserver implements Observer {

    /**
     * @param o   被观察者
     * @param arg
     */
    @Override
    public void update(Observable o, Object arg) {
        System.out.println("被观察对象为" + o + ";\n传递的参数为：" + arg);
    }
}
