package com.util.beansutil;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.spring.circleexception.springinner.A;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @ClassName ListObjectToListBeanUtil
 * @description List<Object>转换成一个List<T>
 * @Author qianchao
 * @Date 2022/7/4
 * @Version V1.0
 **/
public class ListObjectToListBeanUtil {

    /**
     * List<Object>转List<T>
     *
     * @param objectList
     * @param clazz
     * @return
     */
    public static <T> List<T> objectToBean(List<Object> objectList, Class<T> clazz) {
        if (objectList == null || objectList.size() == 0) {
            return null;
        }
        List<T> list = new ArrayList<>();
        for (Object o : objectList) {
            try {
                T t = (T) o;//拆箱
//                T cast = clazz.cast(o);
                list.add(t);
            } catch (Exception e) {
                String s = JSONObject.toJSONString(o);
                JSONObject jsonObject = JSONObject.parseObject(s);
                list.add(JSON.toJavaObject(jsonObject, clazz));
            }
        }
        return list;
    }

    @Test
    public void test() {
        AtomicInteger atomicInteger=new AtomicInteger(0);
        int andIncrement = atomicInteger.getAndIncrement();
        List<Object> list = new ArrayList<>();
        list.add("bs");
        list.add("ks");
        list.add("nS");
        List<String> ts = objectToBean(list, String.class);
        ts.stream().forEach(System.out::print);
    }
}
