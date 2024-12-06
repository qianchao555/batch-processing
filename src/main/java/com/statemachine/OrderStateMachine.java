package com.statemachine;

import org.junit.Test;
import org.springframework.core.annotation.Order;

/**
 * 状态机类
 *
 * @author Chao C Qian
 * @date 2024/12/6
 */
public class OrderStateMachine {
    private OrderStateEnum state;
    
    //初始化状态机状态
    public OrderStateMachine() {
        this.state = OrderStateEnum.Order_placed;
    }
    public OrderStateEnum getState() {
        return state;
    }
    
    @Test
    public void testStateMachine(){
        OrderStateMachine orderStateMachine = new OrderStateMachine();
        System.out.println("当前状态为:"+orderStateMachine.getState());
        
        orderStateMachine.handleEvent(OrderEventEnum.Order_confirmed_event);
        System.out.println("当前状态为:"+orderStateMachine.getState());
        
        orderStateMachine.handleEvent(OrderEventEnum.Order_shipped_event);
        System.out.println("当前状态为:"+orderStateMachine.getState());
        
        orderStateMachine.handleEvent(OrderEventEnum.Order_delivered_event);
        System.out.println("当前状态为:"+orderStateMachine.getState());
    }
    
    public void handleEvent(OrderEventEnum eventEnum){
        switch (state){
            case Order_placed -> {
                //订单下单后，可以确认订单或取消订单
                if (eventEnum == OrderEventEnum.Order_confirmed_event){
                    state = OrderStateEnum.Order_confirmed;
                    sendConfirmationEmail();
                }else if (eventEnum == OrderEventEnum.Order_cancelled_event){
                    state = OrderStateEnum.Order_cancelled;
                    processRefund();
                }
            }
            case Order_confirmed -> {
                //订单下单并确认后，订单是否发货
                if (eventEnum == OrderEventEnum.Order_shipped_event){
                    state = OrderStateEnum.Order_shipped;
                    updateShippingStatus();
                }else if (eventEnum == OrderEventEnum.Order_cancelled_event){
                    state = OrderStateEnum.Order_cancelled;
                    processRefund();
                }
            }
            case Order_shipped -> {
                //订单发货后，订单是否送达
                if (eventEnum == OrderEventEnum.Order_delivered_event){
                    state = OrderStateEnum.Order_delivered;
                    sendConfirmationEmail();
                }
            }
            default -> System.out.println("无效事件");
           
        }
    }

    private void updateShippingStatus() {
        System.out.println("状态变为:订单已发货");
    }

    private void processRefund() {
        System.out.println("状态变为:订单已取消");
    }

    private void sendConfirmationEmail() {
        System.out.println("状态变为:订单已确认");
    }
}
