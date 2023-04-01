package com.huawei.rumen;

import java.io.IOException;
import java.io.InputStream;

/**
 * @description:
 * @author:xiaoyige
 * @createTime:2023/3/27 21:44
 * @version:1.0
 */
public class Test1 {
    public static void main(String[] args) throws IOException {
        InputStream in = System.in;

        int count = 0;
        int res;
        while ((res = in.read()) != '\n') {
            if ((char) res == ' ') {
                count = 0;
            } else {
                count++;
            }
        }

        System.out.println(count);
    }
}
