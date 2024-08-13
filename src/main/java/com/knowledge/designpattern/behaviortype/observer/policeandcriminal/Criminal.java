package com.knowledge.designpattern.behaviortype.observer.policeandcriminal;

/**
 * 被观察者 罪犯
 * @Author qianchao
 * @Date 2021/11/29
 * @Version java8 V1.0
 **/
public interface Criminal {
    //罪犯被盯上了
    void spotted(Police police);
    //罪犯的犯罪行为
    void crime(String illegalAction);
    String getName();
    String getIllegalAction();
}
