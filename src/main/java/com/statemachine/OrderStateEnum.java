package com.statemachine;

/**
 * @author Chao C Qian
 * @date 2024/12/6
 * 状态枚举
 */
public enum OrderStateEnum {
    Order_placed, // 订单已下单
    Order_confirmed,    // 订单已确认
    Order_shipped, // 订单已发货
    Order_delivered,   // 订单已送达
    Order_cancelled // 订单已取消
}
