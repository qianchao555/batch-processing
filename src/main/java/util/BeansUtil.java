package util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.util.ObjectUtils;

import java.util.ArrayList;
import java.util.List;

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



}

