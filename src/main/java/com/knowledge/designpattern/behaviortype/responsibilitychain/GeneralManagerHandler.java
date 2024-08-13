package com.knowledge.designpattern.behaviortype.responsibilitychain;

/**
 * @ClassName GeneralManagerHandler
 * @Author qianchao
 * @Date 2021/11/29
 * @Version java8 V1.0
 **/
public class GeneralManagerHandler extends AbstractHandler {

    @Override
    public String handleFeeRequest(User user, double fee) {
        //总经理权力大，全部都可以申请  不用添加条件了
        return "总经理开始审批。。。。\n成功：总经理同意聚餐费用，金额";
    }
}
