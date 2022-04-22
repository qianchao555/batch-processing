package util.repeatsubmit;

import java.lang.annotation.*;

/**
 * @description:防止重复提交 注解
 * @projectName:batch-processing
 * @see:util.repeatsubmit
 * @author:xiaoyige
 * @createTime:2022/4/21 20:42
 * @version:1.0
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface PreventDuplication {
    /**
     * 防重复提交 过期时间10s
     * @return
     */
    long expireSecond() default 10;
}
