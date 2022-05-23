package com.collection.map;

/**
 * 一亿个整数的数据量去重
 *
 * bit-map来实现
 *
 * @Author qianchao
 * @Date 2021/11/19
 * @Version designpattern V1.0
 **/
public class SimulationBitMap {

    public static void main(String[] args) {
        int n = 16;
        new SimulationBitMap().create(n);



    }
    /**
     * 创建bitmap数组
     */
    public byte[] create(int n){
        byte[] bits = new byte[getIndex(n) + 1];

//        for(int i = 0; i < n; i++){
//            add(bits, i);
//        }
        add(bits,10);

        System.out.println(contains(bits, 16));

        int index = 1;
        for(byte bit : bits){
            System.out.println("-------" + index++ + "-------");
            showByte(bit);

        }

        return bits;
    }

    /**
     * 右移三位的目的：byte可以表示8个bit
     * 构建特定长度的数组new byte[capacity/8 + 1]
     * @param num
     * @return
     */
    public  int getIndex(int num) {
        //右移三位===》n/8
        return num >> 3;
    }

    /**
     * 计算数字num 在byte[index]中的位置  num%8
     *
     * @param num
     * @return
     */
    public int getPosition(int num) {
//        return num & 0x07;
        return num%8;
    }

    /**
     * 将所在位置从0变成1，其它位置不变
     *
     * 这里相当于就去重了  因为每个数字的位置是被确定了的   有重复数据进来 还是原来的值
     * @param bits
     * @param num
     */
    public void add(byte[] bits, int num) {
//        bits[getIndex(num)]= (byte) (bits[getIndex(num)] | 1 << getPosition(num));
        showByte(bits[getIndex(num)]);
        System.out.println(bits[getIndex(num)] |= 1 << getPosition(num));
        //00001000
//        bits[getIndex(num)] |= 1 << getPosition(num);
    }

    /**
     * 判断指定数字num是否存在
     *
     * @param bits
     * @param num
     * @return
     */
    public boolean contains(byte[] bits, int num) {
        return (bits[getIndex(num)] & 1 << getPosition(num)) != 0;
    }

    /**
     * 重置某一数字对应在bitmap中的值
     * @param bits
     * @param num
     */
    public void clear(byte[] bits, int num) {
        bits[getIndex(num)] &= ~(1 << getPosition(num));
    }

    /**
     * 打印byte类型的变量<br/>
     * 将byte转换为一个长度为8的byte数组，数组每个值代表bit
     */

    public void showByte(byte b){
        byte[] array = new byte[8];
        for(int i = 7; i >= 0; i--){
            array[i] = (byte)(b & 1);
            b = (byte)(b >> 1);
        }

        for (byte b1 : array) {
            System.out.print(b1);
            System.out.print(" ");
        }

        System.out.println();
    }

}
