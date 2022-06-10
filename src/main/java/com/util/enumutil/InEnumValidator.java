package com.util.enumutil;

import org.apache.commons.lang3.StringUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.lang.annotation.Annotation;

/**
 * @ClassName InEnumValidator
 * @Author qianchao
 * @Date 2022/6/8
 * @Version   V1.0
 **/
public class InEnumValidator implements ConstraintValidator<InEnum,Object> {
    private InEnum inEnumAnnotation;

    @Override
    public void initialize(InEnum constraintAnnotation) {
        this.inEnumAnnotation=constraintAnnotation;
    }

    @Override
    /**
     * value:需要校验的对象
     */
    public boolean isValid(Object value, ConstraintValidatorContext constraintValidatorContext) {
        if(value==null){
            return false;
        }
        if(value.getClass().equals(String.class) && StringUtils.isBlank((String)value)){
            return true;
        }
        return false;
    }
}
