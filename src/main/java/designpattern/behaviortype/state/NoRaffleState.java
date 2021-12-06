package designpattern.behaviortype.state;

/**
 * 具体的状态类，每个子类实现一个与Context的一个状态行为
 * 不能抽奖的状态   可以扣几分、不能抽奖、不能发放奖品
 *
 * @author:xiaoyige
 * @createTime:2021/12/6 20:38
 * @version:1.0
 */
public class NoRaffleState implements State {
    Context context;

    //初始化时，传入活动引用，扣除积分后改变其状态
    public NoRaffleState(Context context) {
        this.context = context;
    }

    /**
     * 当前状态可以扣积分，扣除后，状态设置成可以抽奖的状态
     */
    @Override
    public void deduceMoney() {
        System.out.println("扣除积分50成功，可以开始抽奖了。。");
        context.setState(context.getCanRaffleState());
    }

    /**
     * 当前状态不能抽奖
     *
     * @return
     */
    @Override
    public boolean raffle() {
        System.out.println("扣了积分才能抽奖！");
        return false;
    }

    /**
     * 当前状态不能发放奖品
     */
    @Override
    public void dispensePrize() {
        System.out.println("不能发放奖品！");
    }
}
