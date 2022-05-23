package com.designpattern.behaviortype.state;

/**
 * 奖品发放完毕状态
 *
 * @author:xiaoyige
 * @createTime:2021/12/6 20:38
 * @version:1.0
 */
public class DispenseOutState implements State {
    Context context;

    public DispenseOutState(Context context) {
        this.context = context;
    }

    @Override
    public void deduceMoney() {
        System.out.println("奖品已经发送完了！下次再参加");
    }

    @Override
    public boolean raffle() {
        System.out.println("奖品已经发送完了！下次再参加");
        return false;
    }

    @Override
    public void dispensePrize() {
        System.out.println("奖品已经发送完了！下次再参加");
    }
}
