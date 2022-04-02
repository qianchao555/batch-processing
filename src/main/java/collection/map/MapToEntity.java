package collection.map;


import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

/**
 * @ClassName map转实体
  @Author qianchao
 * @Date 2022/3/30
 * @Version  V1.0
 **/

public class MapToEntity {
    public static void main(String[] args) {
        Map<String,Object> map=new HashMap<>();
        map.put("qcKey",new Student());
        Object o = mapToEntity(map, Student.class);
        System.out.println(o.toString());
    }
    /**
     * 方式一：Json提供的方法来转
     */

    /**
     * 方式二：利用反射实现
     * @param map
     * @param clazz
     * @return
     */
    public static Object mapToEntity(Map<String, Object> map, Class<?> clazz) {
        if (map == null) {
            return null;
        }
        Object obj = null;
        try {
            //反射构造实体类
            obj = clazz.newInstance();
            Field[] declaredFields = obj.getClass().getDeclaredFields();
            for (Field field : declaredFields) {
                field.setAccessible(true);
                field.set(obj, map.get(field.getName()));
            }
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return obj;
    }


}
