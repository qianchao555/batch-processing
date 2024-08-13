package com.knowledge.designpattern.behaviortype.observer.policeandcriminal;

/**
 * @ClassName SecurityPolice
 * @Author qianchao
 * @Date 2021/11/29
 * @Version java8 V1.0
 **/
public class SecurityPolice implements Police {
    public SecurityPolice(Criminal criminal) {
        System.out.println(criminal.getName() + "被公安盯上了");
        criminal.spotted(this);
    }

    @Override
    public void action(Criminal criminal) {
        System.out.println("公安发现了" + criminal.getName() + criminal.getIllegalAction());
    }
}
