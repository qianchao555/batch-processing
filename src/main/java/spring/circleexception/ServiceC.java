package spring.circleexception;

import org.springframework.stereotype.Component;

/**
 * @description:
 * @author:xiaoyige
 * @createTime:2021/12/4 22:28
 * @version:1.0
 */
@Component
public class ServiceC {
    private ServiceD serviceD;

    /**
     * setter注入 不会引起循环依赖问题
     * @param serviceD
     */
    public void setServiceD(ServiceD serviceD) {
        this.serviceD = serviceD;
        System.out.println("C里面设置了D");
    }
}
