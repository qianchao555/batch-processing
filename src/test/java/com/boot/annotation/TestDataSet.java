package com.boot.annotation;

import java.lang.annotation.*;

/**
 * 自定义注解，标注测试的数据集
 *
 * @Author qianchao
 * @Date 2022/9/6
 * @Version V1.0
 **/
@Target({ElementType.METHOD, ElementType.FIELD})
@Documented
@Retention(RetentionPolicy.RUNTIME)
public @interface TestDataSet {
    /**
     * 数据集路径
     *
     * @return
     */
    String[] locals() default {};

    /**
     * 文件类型
     * @return
     */
    String fileType() default "xls";

    /**
     * dataSource名
     * @return
     */
    String[] dataSourceName() default "dataSource";

    /**
     * IDatabaseTester操作
     * @return
     */
    String setupOperation() default "REFRESH";

    /**
     * IDatabaseTester操作
     * @return
     */
    String teardownOperation() default "DELETE";

}
