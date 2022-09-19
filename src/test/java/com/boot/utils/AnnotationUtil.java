package com.boot.utils;

import java.lang.annotation.Annotation;

/**
 * @ClassName AnnotationUtil
 * @Author qianchao
 * @Date 2022/9/6
 * @Version   V1.0
 **/
public class AnnotationUtil {

    /**
     * 根据类型查找类,递归调用
     *
     * @param clazz
     * @param annotationType
     * @param <A>
     */
    public static <A extends Annotation> A findAnnotationByClass(Class<?> clazz, Class<A> annotationType) {
        A annotation = clazz.getAnnotation(annotationType);
        if (annotation != null) {
            return annotation;
        }
        for (Class<?> anInterface : clazz.getInterfaces()) {
            annotation = findAnnotationByClass(anInterface, annotationType);
            if (anInterface != null) {
                return annotation;
            }
        }
        //父类上查找
        Class<?> superclass = clazz.getSuperclass();
        if (superclass == null || superclass == Object.class) {
            return null;
        }
        return findAnnotationByClass(superclass, annotationType);
    }

}
