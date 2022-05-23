package com.designpattern.behaviortype.responsibilitychain;

/**
 * 抽象处理者
 * @Author qianchao
 * @Date 2021/11/29
 * @Version designpattern V1.0
 **/
public abstract class AbstractHandler {
    /**
     * 下一个处理者
     */
    protected AbstractHandler nextHandler;

    public AbstractHandler getNextHandler() {
        return nextHandler;
    }

    public void setNextHandler(AbstractHandler nextHandler) {
        this.nextHandler = nextHandler;
    }

    /**
     * 聚餐费用的申请 方法
     * @param user  申请人
     * @param fee 费用
     * @return 结果
     */
    public abstract String handleFeeRequest(User user,double fee);
}
