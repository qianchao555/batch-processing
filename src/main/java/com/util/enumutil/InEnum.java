package com.util.enumutil;

import jakarta.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


/**
 * 验证value是否在枚举内
 */
@Target({ElementType.FIELD, ElementType.ANNOTATION_TYPE, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface InEnum {
    /**
     * 枚举类
     *
     * @return
     */
    Class<? extends Enum> enumClazz();

    /**
     * 枚举中以哪个字段为key
     *
     * @return 枚举代码值
     */
    String key() default "code";

    /**
     * 枚举验证黑名单
     *
     * @return
     */
    String[] black() default {};

    /**
     * 默认错误信息
     *
     * @return 验证返回错误信息
     */
    String message() default "{没有通过验证}";

    /**
     * 通用分组
     *
     * @return
     */
    Class<?>[] groups() default {};

    /**
     * Payload
     *
     * @return
     */
    Class<? extends Payload>[] payload() default {};

    /**
     * 组合
     */
    @Target({ElementType.FIELD, ElementType.ANNOTATION_TYPE})
    @Retention(RetentionPolicy.RUNTIME)
    @interface List {
        InEnum[] value();
    }
}
