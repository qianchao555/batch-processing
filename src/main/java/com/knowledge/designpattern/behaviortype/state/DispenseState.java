package com.knowledge.designpattern.behaviortype.state;

/**
 * 发放奖品的状态
 *
 * @author:xiaoyige
 * @createTime:2021/12/6 20:38
 * @version:1.0
 */
public class DispenseState implements State {
    Context context;

    public DispenseState(Context context) {
        this.context = context;
    }

    @Override
    public void deduceMoney() {
        System.out.println("不能扣除积分了");
    }

    @Override
    public boolean raffle() {
        System.out.println("不能抽奖");
        return false;
    }

    /**
     * 发放奖品
     */
    @Override
    public void dispensePrize() {
        if(context.getCount()>0){
            System.out.println("恭喜你，你的奖品！");
            //不能抽奖状态
            context.setState(context.getNoRafflleState());
        }else {
            System.out.println("虽然你中奖了，但是，奖品发完了，下次再来吧！");
            //奖品发送完毕，后边不可以在抽奖了
            context.setState(context.getDispensOutState());
        }
    }
}
