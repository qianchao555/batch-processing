package com.testdemo;

import org.junit.Test;

import java.util.Base64;

/**
 * @description: 凯撒加密解密算法
 * @createTime: 2022/5/19 21:03
 * @version: 1.0
 */
public class CaesarDemoTest {

    @Test
    public void test() {
        char c = 'c';
        int key = -2;
        System.out.println(caesar(String.valueOf(c), key));

        System.out.println(caesarDecrypt(String.valueOf(c), key));

        frequencyAnalysis("abcaaa");
    }


    /**
     * 凯撒加密算法
     *
     * @param str 需要加密的字符串
     * @param key 移动位数,密钥
     * @return 加密后的字符串
     */
    public String caesar(String str, int key) {
        char[] chars = str.toCharArray();
        for (int i = 0; i < chars.length; i++) {
            chars[i] += (char) key;
        }
        return new String(chars);
    }

    //编写一个凯撒解密的方法
    public String caesarDecrypt(String str, int key) {
        char[] chars = str.toCharArray();
        for (int i = 0; i < chars.length; i++) {
            chars[i] -= (char) key;
        }
        return new String(chars);
    }

    //编写一个频率分析法的方法，用来破解凯撒加密算法，给出方法和调用
    public void frequencyAnalysis(String encryptStr) {
      //最重要的：猜测密钥key，拿着这个key去解密
    }


    public void testBase64() {
        // Base64加密
        String str = "hello";
        String encodeStr = Base64.getEncoder().encodeToString(str.getBytes());
        System.out.println(encodeStr);

        // Base64解密
        byte[] decode = Base64.getDecoder().decode(encodeStr);
        System.out.println(new String(decode));
    }


    public void testString() {
        String str = "hello";
        // 字符串转字节数组
        byte[] bytes = str.getBytes();
        // 字节数组转字符串
        String s = new String(bytes);

        str.toString();
    }



}
