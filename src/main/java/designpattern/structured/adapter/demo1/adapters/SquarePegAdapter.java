package designpattern.structured.adapter.demo1.adapters;

import designpattern.structured.adapter.demo1.round.RoundPeg;
import designpattern.structured.adapter.demo1.square.SquarePeg;

/**
 * 类的适配器模式
 *
 * 方钉适配圆孔  ==》圆孔不变，方钉做适配  ==》圆孔和适配器交互
 *
 * 方钉到圆孔的适配器
 *
 * 适配器允许将方钉安装到圆孔中
 * @Author qianchao
 * @Date 2021/11/16
 * @Version designpattern V1.0
 **/
public class SquarePegAdapter extends RoundPeg {
    private SquarePeg squarePeg;

    public SquarePegAdapter(SquarePeg squarePeg){
        this.squarePeg=squarePeg;
    }

    @Override
    public double getRadius() {
        double result;
        result=Math.sqrt(Math.pow((squarePeg.getWidth()/2),2)*2);
        return result;
    }
}
