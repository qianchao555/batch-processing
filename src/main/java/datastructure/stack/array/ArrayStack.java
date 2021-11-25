package datastructure.stack.array;

/**
 * 数组模拟栈
 *
 * @author:xiaoyige
 * @createTime:2021/11/25 20:24
 * @version:1.0
 */
public class ArrayStack {
    //栈的大小
    private int maxSize;
    //数据
    private int[] stack;
    //栈顶指针
    private int top = -1;

    public ArrayStack(int maxSize) {
        this.maxSize = maxSize;
        stack = new int[maxSize];
    }

    /**
     * 栈满
     */
    public boolean isFull() {
        return top == maxSize - 1;
    }

    /**
     * 栈空
     */
    public boolean isEmpty() {
        return top == -1;
    }

    /**
     * 入栈
     */
    public void push(int value) {
        if (isFull()) {
            System.out.println("栈满");
            return;
        }
        top++;
        stack[top] = value;
    }

    /**
     * 出栈
     */
    public int pop() {
        if (isEmpty()) {
            new RuntimeException("栈为空");
        }
        int value = stack[top];
        top--;
        return value;
    }

    /**
     * 遍历
     */
    public void list() {
        if (isEmpty()) {
            System.out.println("空的！");
            return;
        }
        //从栈顶开始打印
        for (int i = top; i >= 0; i--) {
            System.out.println(stack[i]);
        }
    }

    /**
     * 返回运算符的优先级
     */

}
