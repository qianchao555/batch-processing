package com.designpattern.createtype.prototype;

/**
 * @description:羊 原型类
 * @author:xiaoyige
 * @createTime:2021/11/14 21:03
 * @version:1.0
 */
public class Sheep implements Cloneable {
    private String name;
    private int age;
    private String color;

    public Sheep(String name, int age, String color) {
        this.name = name;
        this.age = age;
        this.color = color;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    @Override
    public String toString() {
        return "Sheep{" +
                "name='" + name + '\'' +
                ", age=" + age +
                ", color='" + color + '\'' +
                '}';
    }

    /**
     * 克隆该实例，使用默认的clone方法来完成
     *
     *
     * @return
     * @throws CloneNotSupportedException
     */
    @Override
    protected Object clone() {
        Sheep sheep = null;
        try {
            //默认clone方法  ---》浅拷贝
            sheep = (Sheep) super.clone();

            /**
             * 深拷贝
             * 方式1 实现序列化、clonable   使用clone方法
             * Object deep=null
             * super.clone()完成基本数据类型和String的克隆
             *
             * 对引用类型的属性，进行单独处理
             * DeepType deepType=(DeepType)deep
             * deepType.里面的属性类型=(里面的属性类型)里面的属性类型.clone()
             * return deepType
             *
             *
             *方式二 通过对象序列化实现深拷贝 推荐使用
             * 创建流对象
             * ByteArrayOutputStream bos=null
             * ObjectOutputStream oos=null
             * ByteArrayInputStream bis=null
             * ObjectInputStream ois=null
             * 序列化
             * bos=new ByteArrayOutputStream()
             * oos=new ObjectOutputStream(bos)
             * 当前对象以对象流的方式输出
             * oos.writeObject(this)
             * 反序列化
             * bis=new ByteArrayInputStream(bos.toByteArray())
             * ois=new ObjectInputStream(bis)
             * ois.readObject()==>得到对象了
             */
        } catch (CloneNotSupportedException e) {
            System.out.println(e.getMessage());
        }
        return sheep;
    }
}
