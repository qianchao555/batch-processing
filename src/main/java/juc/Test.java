package juc;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * @description:
 * @author:xiaoyige
 * @createTime:2022/4/18 20:50
 * @version:1.0
 */
public class Test {
    public static void main(String[] args) {
        DateTimeFormatter df =DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime localDateTime=LocalDateTime.parse("2022-05-03 14:09:34",df);
//        LocalDateTime localDateTime=LocalDateTime.now();

        System.out.println(localDateTime);

        System.out.println(localDateTime.plusNanos(754927277000L));
    }
}
