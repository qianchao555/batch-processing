package designpattern.behaviortype.observer.policeandcriminal;

import java.util.ArrayList;

/**
 * 小偷 被观察者（Criminal）的实现类
 *
 * @Author qianchao
 * @Date 2021/11/29
 * @Version java8 V1.0
 **/
public class Thief implements Criminal {
    //警察列表 多种类型的警察都可以盯上罪犯
    private ArrayList<Police> polices = new ArrayList<>();
    //小偷名字
    private String name;
    //犯罪行为
    private String illegalAction;

    public void setName(String name) {
        this.name = name;
    }

    /**
     * 单例对象  被观察者应该是同一个
     * @return
     */
    public static Thief getInstance() {
        return InnerClass.THIEF;
    }

    private static class InnerClass {
        private static final Thief THIEF = new Thief("小偷");
    }

    private Thief(String name) {
        this.name = name;
    }

    /**
     * 被警察盯上了
     *
     * @param police
     */
    @Override
    public void spotted(Police police) {
        //添加警察，这些警察都盯上了罪犯
        if (!polices.contains(police)) {
            polices.add(police);
        }
    }

    /**
     * 犯罪
     *
     * @param illegalAction
     */
    @Override
    public void crime(String illegalAction) {
        this.illegalAction = illegalAction;
        //小偷犯罪，警察就会发现小偷犯罪
        for (Police police : polices) {
            police.action(this);
        }
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getIllegalAction() {
        return illegalAction;
    }
}
