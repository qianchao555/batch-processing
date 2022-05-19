package com.designpattern.behaviortype.observer.javaobserver;

import java.util.Observable;

/**
 * 被观察者 实现Java自带的观察者
 * @Author qianchao
 * @Date 2021/11/29
 * @Version java8 V1.0
 **/
public class MyObservable extends Observable {
    public static void main(String[] args) {
        //定义被观察者
        MyObservable myObservable=new MyObservable();
        //建立被观察者与观察者的关系
        myObservable.addObserver(new MyObserver());
        //被观察者触发事件
        myObservable.setChanged();
        //触发了事件，通知观察者
        myObservable.notifyObservers("我是被观察者，我触发了事件。");
    }

}
