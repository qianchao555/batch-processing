package com.datastructure.link.doublelinktable;

public class HeroNode2 {
    public int no;
    public String name;
    //昵称
    public String nickName;

    public HeroNode2 pre;
    public HeroNode2 next;

    public HeroNode2(int no, String name, String nickName) {
        this.no = no;
        this.name = name;
        this.nickName = nickName;
    }

    @Override
    public String toString() {
        return "HeroNode{" +
                "no=" + no +
                ", name='" + name + '\'' +
                ", nickName='" + nickName + '\'' +
                '}';
    }
}