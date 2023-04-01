package com.huawei.rumen;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * @description:
 * @author:xiaoyige
 * @createTime:2023/3/31 22:28
 * @version:1.0
 */
public class Hj90 {
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        String str = "";
        while ((str = br.readLine()) != null) {
            if (str.contains("+") || str.contains("-")) {
                System.out.println("NO");
                return;
            }
            String[] split = str.split("\\.");
            String flag = "YES";
            if (split.length != 4) {
                System.out.println("NO");
                break;
            }
            for (int i = 0; i < split.length; i++) {
                if (split[i].equals("") || (split[i].startsWith("0") && split[i].length() > 1)) {
                    flag = "NO";
                    break;
                }
                if (Integer.parseInt(split[i]) < 0 || Integer.parseInt(split[i]) > 255) {
                    flag = "NO";
                    break;
                }

            }
            System.out.println(flag);
        }
    }
}
