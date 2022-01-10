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
        //判断souce源是否属于List
        if (source instanceof List) {
            //转换为List
            List<?> objects = (List<?>) source;
            //对List里面的每一个对象进行copy
            objects.forEach(o -> {
                T t = null;
                try {
                    t = target.newInstance();
                } catch (InstantiationException e) {
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

