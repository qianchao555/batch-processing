package com.util;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.micrometer.core.instrument.util.StringUtils;
import lombok.extern.slf4j.Slf4j;
import com.util.annotation.QueryAlias;

import java.lang.reflect.Field;
import java.util.List;

/**
 * 将对象转换为QueryWrapper
 *
 * @ClassName QueryWrapperUtil
 * @Author qianchao
 * @Date 2022/1/11
 * @Version V1.0
 **/
@Slf4j
public class QueryWrapperUtil {
    public static QueryWrapper pareseWarper(Object value) {
        QueryWrapper queryWrapper = new QueryWrapper();
        if (value == null) {
            return queryWrapper;
        }
        String fieldName = "";
        Object fieldValue = null;
        //遍历对象所有字段
        for (Field declaredField : value.getClass().getDeclaredFields()) {
            //可以访问私有字段
            declaredField.setAccessible(true);
            fieldName = declaredField.getName();
            try {
                fieldValue = declaredField.get(value);
            } catch (IllegalAccessException e) {
                log.error("{}", e);
            }
            //处理字段
            processField(queryWrapper, declaredField, fieldName, fieldValue);
        }
        return queryWrapper;
    }

    private static void processField(QueryWrapper queryWrapper, Field field, String fieldName, Object value) {
        if (value == null) {
            return;
        }
        //获取自定义的注解
        QueryAlias queryAlias = field.getAnnotation(QueryAlias.class);
        //
        if (queryAlias != null) {
            //驼峰转换为下划线方式：数据库命名采用下划线方式因为
            fieldName = camelToUnderline(fieldName);
            //拼接QueryAlias注解上的别名  类似：t.student_name
            fieldName = queryAlias.value() + "." + fieldName;

        }
        if (List.class.equals(field.getType()) && !((List) value).isEmpty()) {
            queryWrapper.in(fieldName, value);
            return;
        }
        queryWrapper.eq(fieldName, value);
    }

    /**
     * abcDefJhi=>abc_def_jhi
     * 驼峰转下划线形式
     *
     * @param fieldName
     * @return
     */
    private static String camelToUnderline(String fieldName) {
        if (StringUtils.isEmpty(fieldName)) {
            return "";
        } else {
            int length = fieldName.length();
            StringBuilder sb = new StringBuilder(length);
            for (int i = 0; i < length; i++) {
                char c = fieldName.charAt(i);
                if (Character.isUpperCase(c) && i > 0) {
                    sb.append('_');
                }
                sb.append(Character.toLowerCase(c));
            }
            return sb.toString();
        }

    }
}
















