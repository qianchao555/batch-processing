package com.statemachine;

/**
 * 订单-事件枚举
 *
 * @author Chao C Qian
 * @date 2024/12/6
 */
public enum OrderEventEnum {
    Order_confirmed_event,    // 订单已确认
    Order_shipped_event, // 订单已发货
    Order_delivered_event,   // 订单已送达
    Order_cancelled_event // 订单已取消
}
