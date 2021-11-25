package datastructure.stack.array;

import java.util.Scanner;

/**
 * 数组模拟栈 测试
 *
 * @author:xiaoyige
 * @createTime:2021/11/25 20:23
 * @version:1.0
 */
public class ArrayStackDemo {
    public static void main(String[] args) {
        //模拟栈大小4
        ArrayStack arrayStack = new ArrayStack(4);
        String key = "";
        boolean loop = true;
        Scanner scanner = new Scanner(System.in);
        while (loop) {
            System.out.println("show:显示");
            System.out.println("exit:退出");
            System.out.println("push:入栈");
            System.out.println("pop:出栈");
            System.out.println("请输入选择：");
            key = scanner.next();
            switch (key) {
                case "show":
                    arrayStack.list();
                    break;
                case "push":
                    System.out.println("请输入一个数：");
                    int value = scanner.nextInt();
                    arrayStack.push(value);
                    break;
                case "pop":
                    try {

                        int result = arrayStack.pop();
                        System.out.println("出栈的是：" + result);
                    } catch (Exception e) {
                        System.out.println( e.getMessage());
                    }
                    break;
                case "exit":
                    scanner.close();
                    loop = false;
                    break;
                default:
                    break;
            }
        }
        System.out.println("程序退出了。。。");
    }
}
