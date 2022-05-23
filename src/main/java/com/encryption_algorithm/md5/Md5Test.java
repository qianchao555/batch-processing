package com.encryption_algorithm.md5;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * @description:
 * @author:xiaoyige
 * @createTime:2022/5/18 22:04
 * @version:1.0
 */
public class Md5Test {
    public static void main(String[] args) throws NoSuchAlgorithmException {
        // 原始字符
        String data = "Hello World!";

        // 编码
        MessageDigest md = MessageDigest.getInstance("MD5");
        byte[] digest = md.digest(data.getBytes());

        // 转化为16进制编码
        BigInteger bigInteger = new BigInteger(1, digest);
        //会得到一个32位的16进制数 即：128位
        System.out.println("JDK MD5: " + bigInteger.toString(16));

    }
}
