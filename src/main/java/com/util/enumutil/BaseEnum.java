package com.util.enumutil;

/**
 * 枚举统一入口
 * @param <T>
 */
public interface BaseEnum <T>{
    /**
     * 编码值
     * @return
     */
    T getCode();

    /**
     * 枚举信息描述
     * @return
     */
    String getMsg();
}
