package util.annotation.thinkinginjava.mytestannotation;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * @description:
 * @author:xiaoyige
 * @createTime:2022/1/15 19:49
 * @version:1.0
 */
public class DemoTest {
    public static void main(String[] args) throws InvocationTargetException, IllegalAccessException, InstantiationException {
        PrintNum printNum = new PrintNum();
        Class<? extends PrintNum> clazz = printNum.getClass();
        for (Method method : clazz.getMethods()) {
            //当方法上有MyTest注解时，反射调用该方法
            boolean annotationPresent = method.isAnnotationPresent(MyTest.class);
            if (annotationPresent) {
                method.invoke(clazz.newInstance());
            }
        }
    }
}
