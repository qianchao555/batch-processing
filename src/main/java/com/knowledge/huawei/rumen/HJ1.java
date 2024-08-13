package com.knowledge.huawei.rumen;

import java.io.IOException;
import java.io.InputStream;

/**
 * @description:
 * @author:xiaoyige
 * @createTime:2023/3/29 22:33
 * @version:1.0
 */
public class HJ1 {
    public static void main(String[] args) throws IOException {
        InputStream in=System.in;

        int len = 0;
        char c;
        while ((c= (char) in.read())!='\n') {
            len++;
            if(c==' '){
                len=0;
            }

        }
        System.out.println(len);
    }
}
