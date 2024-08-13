//package com.bootthinking;
//
//import org.springframework.boot.context.event.ApplicationPreparedEvent;
//import org.springframework.context.ApplicationListener;
//import org.springframework.context.ConfigurableApplicationContext;
//
///**
// * @description:
// * @author:xiaoyige
// * @createTime:2022/11/11 21:00
// * @version:1.0
// */
//public class ApplicationPreparedEventListener implements ApplicationListener<ApplicationPreparedEvent> {
//
//    @Override
//    public void onApplicationEvent(ApplicationPreparedEvent event) {
//        ConfigurableApplicationContext context = event.getApplicationContext();
//        context.setId("修改spring应用上下文Id");
//        System.out.println("修改后spring应用上下文id为："+context.getId());
//    }
//}
