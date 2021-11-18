package collection.map;

import java.util.HashMap;
import java.util.Map;

/**
 * @description:
 * @author:xiaoyige
 * @createTime:2021/11/16 20:35
 * @version:1.0
 */
public class Demo {
    public static void main(String[] args) {
        Map map = new HashMap<>();
        map.put("qc", "钱超");
        map.put("qc2", "钱超");
        System.out.println(map);
        int n = 15;
        n |= n >>> 1;
        System.out.println(n);

    }
}
