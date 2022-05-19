package com.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.util.ObjectUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * @ClassName BeansUtil
 * @Author qianchao
 * @Date 2022/1/10
 * @Version V1.0
 **/
@Slf4j
public class BeansUtil {
    private BeansUtil() {
    }

    /**
     * List集合copy
     *
     * @param source 源List
     * @param target 目标对象
     * @param <T>    <T>
     * @return 集合
     */
    public static <T> List<T> copyPropertiesByList(Object source, Class<T> target) {
        List<T> returnTargetList = new ArrayList<>();
        if (ObjectUtils.isEmpty(source)) {
            return returnTargetList;
        }
        /**
         * List<T>:表示List中所有元素为T类型，可以进行读写操作add、remove等
         * List<?>：表示List中所有元素为任意类型，只读类型不能进行添加、修改操作但是可以remove操作（因为上次动作于泛型无关）
         *          List<?>读出的元素都为Object类型、一般作为参数来接收外部集合
         * List<Object>：表示List中所有元素为Object类型
         *
         */
        //判断souce源是否属于List
        if (source instanceof List) {
            //转换为List
            List<?> objects = (List<?>) source;
            //对List里面的每一个对象进行copy
            objects.forEach(o -> {
                T t = null;
                try {
                    t = target.newInstance();
                } catch (InstantiationException e) {//多个异常采用 |  管道符
                    log.error("BeanUtils Error ！" + e);
                } catch (IllegalAccessException e) {
                    log.error("BeanUtils Error ！" + e);
                }
                if (ObjectUtils.isEmpty(t)) {
                    return;
                }
                BeanUtils.copyProperties(o, t);
                returnTargetList.add(t);
            });
        }
        return returnTargetList;
    }

    /**
     * 对象转Map
     * @param bean
     * @param <T>
     * @return
     */
    public static <T>  Map<String,T> beanToMap(Object bean){
        return JSON.parseObject(JSON.toJSONString(bean), new TypeReference<Map<String, T>>(){});
    }


    /**
     * 对象列表转map列表.
     *
     * @param beans 对象列表
     * @param <T>   对象类型
     * @param <P>   对象类型
     * @return map列表
     */
    public static <T, P> List<Map<String, T>> beansToMaps(List<P> beans) {
        if (CollectionUtils.isEmpty(beans)) {
            return Collections.emptyList();
        }
        return JSON.parseObject(JSON.toJSONString(beans), new TypeReference<List<Map<String, T>>>() {
        });
    }

}

