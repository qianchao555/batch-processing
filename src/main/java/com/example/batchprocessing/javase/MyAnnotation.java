package com.example.batchprocessing.javase;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @description:
 * @projectName:batch-processing
 * @see:com.example.batchprocessing.javase
 * @author:xiaoyige
 * @createTime:2021/11/4 21:30
 * @version:1.0
 */

public class MyAnnotation {
    @MyAnnotation2(name = "qc")
    public void test() {

    }
}

@Target({ElementType.ANNOTATION_TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@interface MyAnnotation2 {
    /**
     * 注解的参数：参数类型 + 参数名
     *并不是方法！！！！
     * @return
     */
    String name() default "";

    int age() default 0;

    int id() default -1;//默认为-1，表示不存在

    String[] schools() default {"清华大学"};

}
