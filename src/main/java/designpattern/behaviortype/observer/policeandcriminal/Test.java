package designpattern.behaviortype.observer.policeandcriminal;

/**
 * @ClassName Test
 * @Author qianchao
 * @Date 2021/11/29
 * @Version java8 V1.0
 **/
public class Test {
    public static void main(String[] args) {
        //单例thief，武警和公安盯得是同一个对象
        Thief thief=Thief.getInstance();
        ArmedPolice armedPolice=new ArmedPolice(thief);
        SecurityPolice securityPolice=new SecurityPolice(thief);
        System.out.println("-------------------------------");
        //小偷犯罪
        thief.crime("偷电瓶车");
    }

}
