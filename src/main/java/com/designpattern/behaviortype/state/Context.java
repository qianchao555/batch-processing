package com.designpattern.behaviortype.state;

/**
 * 环境角色 维护State实例定义的所有状态
 *
 * @author:xiaoyige
 * @createTime:2021/12/6 20:38
 * @version:1.0
 */
public class Context {
    // 持有当前状态
    private State state;
    // 四种状态
    State noRafflleState = new NoRaffleState(this);
    State canRaffleState = new CanRaffleState(this);
    State dispenseState = new DispenseState(this);
    State dispensOutState = new DispenseOutState(this);
    // 奖品数量
    int count = 0;

    public Context(int count) {
        //当前状态为 noRafflleState，即不能抽奖状态
        this.state = getNoRafflleState();
        //设置活动奖品数量
        this.count = count;
    }

    //扣分调用当前状态的deduceMoney
    public void debuctMoney() {
        state.deduceMoney();
    }

    //抽奖
    public void raffle() {
        // 如果当前状态是抽奖成功
        if (state.raffle()) {
            //领取奖品
            state.dispensePrize();
        }

    }

    public State getNoRafflleState() {
        return noRafflleState;
    }

    public State getCanRaffleState() {
        return canRaffleState;
    }

    public State getDispenseState() {
        return dispenseState;
    }

    public State getDispensOutState() {
        return dispensOutState;
    }

    public int getCount() {
        int curCount = count;
        count--;
        return curCount;
    }

    public void setState(State state) {
        this.state = state;
    }
}
