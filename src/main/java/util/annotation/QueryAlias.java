package util.annotation;

import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.*;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface QueryAlias {
    @AliasFor("tableAlias")
    String value() default "";

    String tableAlias() default "";
}
