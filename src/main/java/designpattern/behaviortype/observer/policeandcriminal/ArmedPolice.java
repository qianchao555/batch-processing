package designpattern.behaviortype.observer.policeandcriminal;

/**
 * @ClassName ArmedPolice
 * @Author qianchao
 * @Date 2021/11/29
 * @Version java8 V1.0
 **/
public class ArmedPolice implements Police {

    public ArmedPolice(Criminal criminal) {
        System.out.println(criminal.getName() + "被武警盯上了");
        //调用spotted 说明罪犯被盯上了
        criminal.spotted(this);
    }

    @Override
    public void action(Criminal criminal) {
        System.out.println("武警发现了" + criminal.getName() + criminal.getIllegalAction());
    }
}
