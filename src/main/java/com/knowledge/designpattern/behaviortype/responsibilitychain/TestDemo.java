package com.knowledge.designpattern.behaviortype.responsibilitychain;

/**
 * @ClassName Demo
 * @Author qianchao
 * @Date 2021/11/29
 * @Version designpattern V1.0
 **/
public class TestDemo {
    public static void main(String[] args) {
        //组装责任链
        AbstractHandler projectManagerHandler=new ProjectManagerHandler();
        AbstractHandler generalManagerHandler=new GeneralManagerHandler();
        //项目经理的下一个处理者是总经理
        projectManagerHandler.setNextHandler(generalManagerHandler);

        //测试
        String zhangsan = projectManagerHandler.handleFeeRequest(new User("张三"), 700);
        System.out.println(zhangsan);

        //测试2
        System.out.println("\n\n测试2：非张三的审批\n");
        String zhangsan2 = projectManagerHandler.handleFeeRequest(new User("李四"), 400);
        System.out.println(zhangsan2);
    }

}
