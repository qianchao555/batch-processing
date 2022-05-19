package com.encryption_algorithm.base64;

import java.io.UnsupportedEncodingException;
import java.util.Base64;

/**
 * @description:
 * @author:xiaoyige
 * @createTime:2022/5/18 21:33
 * @version:1.0
 */
public class Base64Test {
    public static void main(String[] args) throws UnsupportedEncodingException {
        String str="hello world";
        System.out.println(str);
        //加密
        Base64.Encoder encoder=Base64.getEncoder();
        String encoderStr = encoder.encodeToString(str.getBytes("utf-8"));
        System.out.println("base64编码后:"+encoderStr);

        //解密
        Base64.Decoder decoder = Base64.getDecoder();
        String s1 = new String(decoder.decode(encoderStr));
        System.out.println("解码后："+s1);
    }
}
