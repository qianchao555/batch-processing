package designpattern.behaviortype.state;

/**
 * 抽象 状态角色
 *
 * @author:xiaoyige
 * @createTime:2021/12/6 20:37
 * @version:1.0
 */
public interface State {
    //扣除积分
    void deduceMoney();

    //是否抽中奖品
    boolean raffle();

    //发放奖品
    void dispensePrize();
}
