package com.spring.circleexception;

import org.springframework.stereotype.Component;

/**
 * @description:
 * @author:xiaoyige
 * @createTime:2021/12/4 22:28
 * @version:1.0
 */
@Component
public class ServiceA {
    private ServiceB serviceB;

    public ServiceA(ServiceB serviceB) {
        this.serviceB = serviceB;
    }
}
