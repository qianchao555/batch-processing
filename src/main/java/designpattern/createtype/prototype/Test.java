package designpattern.createtype.prototype;

/**
 * @description:
 * @author:xiaoyige
 * @createTime:2021/11/14 21:01
 * @version:1.0
 */
public class Test {
    public static void main(String[] args) {
        Sheep sheep=new Sheep("tom",12,"白色的羊！");
        //克隆  会产生一个新的Sheep对象
        Sheep cloneSheep1 = (Sheep) sheep.clone();
        Sheep cloneSheep2 = (Sheep) sheep.clone();
        Sheep cloneSheep3 = (Sheep) sheep.clone();
        Sheep cloneSheep4 = (Sheep) sheep.clone();
        Sheep cloneSheep5 = (Sheep) sheep.clone();
        System.out.println("使用原型模式：完成对象的克隆");
        System.out.println("cloneSheep1："+cloneSheep1);
        System.out.println("cloneSheep2："+cloneSheep2);
        System.out.println("cloneSheep3："+cloneSheep3);
        System.out.println("cloneSheep4："+cloneSheep4);
        System.out.println("cloneSheep5："+cloneSheep5);
    }
}
