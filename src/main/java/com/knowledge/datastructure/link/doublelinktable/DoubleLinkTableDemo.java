package com.knowledge.datastructure.link.doublelinktable;

/**
 * @description:
 * @author:xiaoyige
 * @createTime:2021/11/24 20:23
 * @version:1.0
 */
public class DoubleLinkTableDemo {
    public static void main(String[] args) {
        HeroNode2 hero1=new HeroNode2(1,"松江","及时雨");
        HeroNode2 hero2=new HeroNode2(2,"卢均已","玉麒麟");
        HeroNode2 hero3=new HeroNode2(3,"无用","智多星");
        HeroNode2 hero4=new HeroNode2(4,"林冲","豹子头");

        DoubleLinkTable doubleLinkTable=new DoubleLinkTable();
        doubleLinkTable.addHeroNode(hero1);
        doubleLinkTable.addHeroNode(hero2);
        doubleLinkTable.addHeroNode(hero3);
        doubleLinkTable.addHeroNode(hero4);
        System.out.println();

        //显示
        doubleLinkTable.showLinkedList();

        //修改
        doubleLinkTable.update(new HeroNode2(1,"宋江","及时雨"));
        doubleLinkTable.showLinkedList();
        System.out.println();
        //删除
        doubleLinkTable.delete(3);
        doubleLinkTable.showLinkedList();
    }

}
