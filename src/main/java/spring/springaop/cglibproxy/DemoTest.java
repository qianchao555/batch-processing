package spring.springaop.cglibproxy;

import org.springframework.aop.framework.ProxyFactoryBean;

/**
 * @ClassName DemoTest
 * @Author qianchao
 * @Date 2021/11/16
 * @Version designpattern V1.0
 **/
public class DemoTest {
    public static void main(String[] args) {
        //被代理对象 DayWork
        DayWork dayWork=new DayWork();
        //代理类
        CglibProxy cglibProxy=new CglibProxy(dayWork,DayWork.class);
        //生成代理对象
        DayWork dayWorkProxy= (DayWork) cglibProxy.getNewProxy();
        dayWorkProxy.breakfast();
        dayWorkProxy.lunch();
        dayWorkProxy.dinner();
        //class springaop.cglibproxy.DayWork$$EnhancerByCGLIB$$a7063e42
        System.out.println(dayWorkProxy.getClass());
        ProxyFactoryBean proxyFactoryBean = new ProxyFactoryBean();
    }

}
