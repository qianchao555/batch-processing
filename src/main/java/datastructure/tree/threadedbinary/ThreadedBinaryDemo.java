package datastructure.tree.threadedbinary;

/**
 * @description:
 * @author:xiaoyige
 * @createTime:2021/11/27 15:25
 * @version:1.0
 */
public class ThreadedBinaryDemo {
    public static void main(String[] args) {
        HeroNode root=new HeroNode(1,"tom");
        HeroNode heroNode2=new HeroNode(3,"jack");
        HeroNode heroNode3=new HeroNode(6,"smith");
        HeroNode heroNode4=new HeroNode(8,"mary");
        HeroNode heroNode5=new HeroNode(10,"king");
        HeroNode heroNode6=new HeroNode(14,"dim");

        root.setLeft(heroNode2);
        root.setRight(heroNode3);
        heroNode2.setLeft(heroNode4);
        heroNode2.setRight(heroNode5);
        heroNode3.setLeft(heroNode6);
        //手动创建二叉树
        ThreadedBinaryTree threadedBinaryTree=new ThreadedBinaryTree(root);
        threadedBinaryTree.threadedNodes(root);

        //测试10号节点
        HeroNode left = heroNode5.getLeft();
        System.out.println("10号前驱："+left);
        HeroNode right = heroNode5.getRight();
        System.out.println("10号后继："+right);

        //使用线索化方式遍历二叉树
        System.out.println("使用线索化方式遍历二叉树");
        threadedBinaryTree.threadedList();

    }
}
