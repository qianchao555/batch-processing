package spring.circleexception;

import org.springframework.stereotype.Component;

/**
 * @description:
 * @author:xiaoyige
 * @createTime:2021/12/4 22:28
 * @version:1.0
 */
@Component
public class ServiceD {

    private ServiceC serviceC;

    /**
     * setter注入 不会引起循环依赖问题
     * @param serviceC
     */
    public void setServiceC(ServiceC serviceC) {
        this.serviceC = serviceC;
        System.out.println("D里面设置了C");
    }
}
