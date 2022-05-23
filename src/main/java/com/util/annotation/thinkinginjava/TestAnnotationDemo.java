package com.util.annotation.thinkinginjava;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @description:
 * @author:xiaoyige
 * @createTime:2022/1/15 16:49
 * @version:1.0
 */
public class TestAnnotationDemo {
    public static void main(String[] args) {
        List<Integer> useCases = new ArrayList<>();
        Collections.addAll(useCases, 1, 2, 3, 4);
        trackUseCase(useCases, PasswordUtil.class);
    }

    public static void trackUseCase(List<Integer> useCases, Class<?> clazz) {
        for (Method declaredMethod : clazz.getDeclaredMethods()) {
            //获取某个方法上的UseCase注解
            UseCase useCaseAnnotation = declaredMethod.getAnnotation(UseCase.class);
            if (useCaseAnnotation != null) {
                System.out.println("发现了UseCase类的注解值：id=" + useCaseAnnotation.id() + "  description=" + useCaseAnnotation.description());
                useCases.remove(useCaseAnnotation.id());
            }
        }
        for (int i :useCases) {
            System.out.println("缺失的用例id："+i);
        }

    }
}
