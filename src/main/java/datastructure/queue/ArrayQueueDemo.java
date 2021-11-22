package datastructure.queue;

import java.util.Scanner;

/**
 * 使用数组模拟队列
 *
 * @author:xiaoyige
 * @createTime:2021/11/21 21:55
 * @version:1.0
 */
public class ArrayQueueDemo {
    public static void main(String[] args) {
        //创建队列
        ArrayQueue arrayQueue = new ArrayQueue(3);
        //接受用户输入
        char key = ' ';
        Scanner scanner = new Scanner(System.in);
        boolean loop = true;
        //输出一个菜单
        while (loop) {
            System.out.println("show:查看数据");
            System.out.println("exit:推出");
            System.out.println("add:添加数据");
            System.out.println("get:取出数据");
            System.out.println("peek:查看对头数据");
            //接收一个字符
            key = scanner.next().charAt(0);
            switch (key) {
                case 's':
                    arrayQueue.showQueue();
                    break;
                case 'a':
                    System.out.println("输入一个数据：");
                    arrayQueue.addQueue(scanner.nextInt());
                    break;
                case 'g':
                    try {
                        int queue = arrayQueue.getQueue();
                        System.out.println("取出的数据是：" + queue);

                    } catch (Exception e) {
                        System.out.println(e.getMessage());
                    }
                    break;
                case 'p':
                    try {
                        int queue = arrayQueue.peek();
                        System.out.println("查看对头数据是：" + queue);
                    } catch (Exception e) {
                        System.out.println(e.getMessage());
                    }
                    break;
                case 'e':
                    //退出
                    scanner.close();
                    loop = false;
                default:
                    break;
            }
        }
        System.out.println("程序退出！！");

    }
}

class ArrayQueue {
    //数组最大容量-->队列最大容量
    private int maxSize;
    //指向队列头
    private int front;
    //队尾
    private int rear;
    //存放数据
    private int[] arr;

    //创建队列的构造器

    public ArrayQueue(int maxSize) {
        this.maxSize = maxSize;
        arr = new int[maxSize];
        //队头指向队列头的第一个位置
        front = 0;
        //指向队尾的前一个数据,队尾位置为null
        rear = 0;
    }

    /**
     * 队列是否已满
     *
     * @return
     */
    public boolean isFull() {
        return front == (rear + 1) % maxSize;
    }

    /**
     * 队列是否空
     *
     * @return
     */
    public boolean isEmpty() {
        return rear == front;
    }

    /**
     * 添加
     *
     * @return
     */
    public void addQueue(int n) {
        //判断队列是否满了
        if (isFull()) {
            System.out.println("已经满了！！");
            return;
        }
        //
        arr[rear] = n;
        //将rear后移，需要取模
        rear = (rear + 1) % maxSize;
    }

    /**
     * 获取
     *
     * @return
     */
    public int getQueue() {
        //判断队列是否满了
        if (isEmpty()) {
            throw new RuntimeException("队列为空！！");
        }
        //front是指向队列头的
        int tempFront = front;
        //front后移
        front = (front + 1) % maxSize;
        //返回
        return tempFront;

    }

    /**
     * 显示
     *
     * @return
     */
    public void showQueue() {
        //判断队列是否满了
        if (isEmpty()) {
            System.out.println("队列空的！");
            return;
        }
        //从front开始遍历，遍历多少个元素
        //当前队列有效个数
        int size = (rear + maxSize - front) % maxSize;
        for (int i = front; i < size; i++) {
            System.out.println(arr[i % maxSize]);
        }
    }

    /**
     * 显示对头数据，不是取出数据
     *
     * @return
     */
    public int peek() {
        //判断队列是否满了
        if (isEmpty()) {
            throw new RuntimeException("队列为空！！");
        }
        return arr[front];
    }
}
