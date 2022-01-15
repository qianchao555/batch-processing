package util.annotation.thinkinginjava.mytestannotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

//@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
/**
 * 自定义了一个注解，啥也不做，仅仅作为一个标识
 */
public @interface MyTest {
}
