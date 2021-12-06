package designpattern.behaviortype.state;

import java.util.Random;

/**
 * 可以抽奖状态
 *
 * @author:xiaoyige
 * @createTime:2021/12/6 20:38
 * @version:1.0
 */
public class CanRaffleState implements State {
    Context context;

    public CanRaffleState(Context context) {
        this.context = context;
    }

    @Override
    public void deduceMoney() {
        System.out.println("已经扣除积分了！");
    }

    /**
     * 这个状态可抽奖,抽完奖后改变实际状态
     *
     * @return
     */
    @Override
    public boolean raffle() {
        System.out.println("正在抽奖请稍等。。");
        Random random = new Random();
        int num = random.nextInt(4);
        //10%的中奖机会
        if (num == 0) {
            //改变活动状态未发放奖品
            context.setState(context.getDispenseState());
            return true;
        } else {
            System.out.println("没有中奖！");
            //改变活动状态未不能抽奖状态
            context.setState(context.getNoRafflleState());
        }
        return false;
    }

    @Override
    public void dispensePrize() {
        System.out.println("没有中奖，不能发放奖品！");
    }
}
