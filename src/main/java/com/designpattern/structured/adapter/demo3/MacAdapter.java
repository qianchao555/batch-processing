package com.designpattern.structured.adapter.demo3;

/**
 * @ClassName macAdapter
 * @Author qianchao
 * @Date 2021/11/16
 * @Version designpattern V1.0
 **/
public class MacAdapter extends DefaultAdapter{
    private AC ac;

    public MacAdapter(AC ac) {
        this.ac = ac;
    }

    /**
     * mac电源适配器只需要实现20V的方法即可
     * @return
     */
    @Override
    public int output20V() {
        return ac.outputAC()/11;
    }
}
