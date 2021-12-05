package spring.circleexception.springinner;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * spring内部解决方式
 *
 * @author:xiaoyige
 * @createTime:2021/12/4 22:40
 * @version:1.0
 */
public class Demo {
    public static void main(String[] args) {

        //debug看源码！！！比较难！！
        ApplicationContext applicationContext =
                new AnnotationConfigApplicationContext(BeanConfig.class);
        //BeanCurrentlyInCreationException 因为scope定义为的prototype   默认的singleton能解决

        A a = applicationContext.getBean("a", A.class);
        B b = applicationContext.getBean("b", B.class);
        //默认采用三级缓存解决循环依赖 DefaultSingletonBeanRegistry


    }

}
