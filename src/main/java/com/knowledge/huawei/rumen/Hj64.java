package com.knowledge.huawei.rumen;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * @description:
 * @author:xiaoyige
 * @createTime:2023/3/31 21:21
 * @version:1.0
 */
public class Hj64 {
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        String s;
        while ((s= br.readLine()) != null) {
            int songNum = Integer.parseInt(s);
            String upDownString = br.readLine();

            //歌曲现在在第几首歌的位置
            int songIndex = 1;
            char[] upDownCharArray = upDownString.toCharArray();
            if (songNum <= 4) {
                for (int i = 0; i < upDownCharArray.length; i++) {
                    //在末尾按下D，会回到第一条
                    if (upDownCharArray[i] == 'D' && songIndex == songNum) {
                        songIndex = 1;
                    } else if (upDownCharArray[i] == 'D') {
                        songIndex++;
                    } else if (upDownCharArray[i] == 'U' && songIndex == 1) {
                        songIndex = songNum;
                    } else if (upDownCharArray[i] == 'U') {
                        songIndex--;
                    }
                }
                //小于4首歌的情况下输出列表
                for (int i = 1; i <= songNum; i++) {
                    System.out.print(i + " ");
                }
                System.out.println();
                System.out.println(songIndex);
            }
            if (songNum > 4) {
                //多余4时候，需要添加一个指针记录光标在本页的位置.后面需要利用它来计算当前页是哪些歌曲列表
                int count = 1;
                for (int i = 0; i < upDownCharArray.length; i++) {
                    if (upDownCharArray[i] == 'U' && songIndex == 1) {
                        songIndex = songNum;
                        count = 4;
                    } else if (upDownCharArray[i] == 'U') {
                        songIndex--;
                        count--;
                        if (count < 1) {
                            count = 1;
                        }
                    } else if (upDownCharArray[i] == 'D' && songIndex == songNum) {
                        songIndex = 1;
                        count = 1;
                    } else if (upDownCharArray[i] == 'D') {
                        songIndex++;
                        count++;
                        if (count > 4) {
                            count = 4;
                        }
                    }

                }
                if (count == 1) {
                    System.out.println(songIndex + " " + (songIndex + 1) + " " + (songIndex + 2) + " " + (songIndex + 3));
                } else if (count == 2) {
                    System.out.println((songIndex - 1) +" "+ songIndex + " " + (songIndex + 1) + " " + (songIndex + 2));
                } else if (count == 3) {
                    System.out.println((songIndex - 2) + " " + (songIndex - 1) + " " + songIndex + " " + (songIndex + 1));
                } else if (count == 4) {
                    System.out.println((songIndex - 3) + " " + (songIndex - 2) + " " + (songIndex - 1) + " " + songIndex);
                }
                System.out.println(songIndex);
            }
        }
    }
}
