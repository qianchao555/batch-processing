package com.knowledge.designpattern.behaviortype.responsibilitychain;

/**
 * 具体处理者-项目经理
 *
 * @Author qianchao
 * @Date 2021/11/29
 * @Version java8 V1.0
 **/
public class ProjectManagerHandler extends AbstractHandler {

    @Override
    public String handleFeeRequest(User user, double fee) {
        String str = null;
        //项目经理权限比较小，只能审批500元以下的金额
        if (fee < 500) {
            if (!"张三".equals(user.getName())) {
                str = "失败！" + "项目经理不同意除了张三以外的申请";
            } else {
                str = "项目经理同意金额为" + fee + "的申请";
            }
        } else {
            //超过500元，需要下一个人来处理
            System.out.println("超过500，项目经理移交给总经理进行审批。");
            if (getNextHandler() != null) {
                return getNextHandler().handleFeeRequest(user, fee);
            }
        }
        return str;
    }
}
