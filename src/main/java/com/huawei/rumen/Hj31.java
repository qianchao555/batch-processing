package com.huawei.rumen;

import java.util.Scanner;

/**
 * @description:描述
 * 对字符串中的所有单词进行倒排。
 *
 * 说明：
 *
 * 1、构成单词的字符只有26个大写或小写英文字母；
 *
 * 2、非构成单词的字符均视为单词间隔符；
 *
 * 3、要求倒排后的单词间隔符以一个空格表示；如果原字符串中相邻单词间有多个间隔符时，倒排转换后也只允许出现一个空格间隔符；
 *
 * 4、每个单词最长20个字母；
 *
 * 数据范围：字符串长度满足
 * 1
 * ≤
 * �
 * ≤
 * 10000
 *
 * 1≤n≤10000
 * 输入描述：
 * 输入一行，表示用来倒排的句子
 *
 * 输出描述：
 * 输出句子的倒排结果
 *
 * 示例1
 * 输入：
 * I am a student
 * 复制
 * 输出：
 * student a am I
 * 复制
 * 示例2
 * 输入：
 * $bo*y gi!r#l
 * 复制
 * 输出：
 * l r gi y bo
 * @author:xiaoyige
 * @createTime:2023/3/29 22:51
 * @version:1.0
 */
public class Hj31 {
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        while (in.hasNextLine()) { // 注意 while 处理多个 case
            String str = in.nextLine();
            char[] arr = str.toCharArray();
            for (int i = 0; i < str.length(); i++) {
                if ((arr[i] <= 'z' && arr[i] >= 'a') || (arr[i] <= 'Z' && arr[i] >= 'A')) {
                    arr[i] = arr[i];
                } else {
                    arr[i] = ' ';
                }
            }
            System.out.println(arr);
            StringBuilder s = new StringBuilder();
            for (int i = 0; i < arr.length; i++) {
                if (arr[i] == ' ' && arr[i + 1] == ' ') {
                    continue;
                } else {
                    s.append(arr[i]);
                }
            }
            String[] s2 = s.toString().split(" ");
            for (int i = s2.length - 1; i >= 0; i--) {
                System.out.print(s2[i]+" ");
            }

        }
    }
}
