package com.rabbitmq.two;

import com.rabbitmq.client.CancelCallback;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;
import com.rabbitmq.utils.RabbitMqUtills;

public class Worker01 {
    //队列的名字
    public static final String QUEUE_NAME="hello";

    public static void main(String[] args) throws Exception {
        //接收消息
        Channel channel = RabbitMqUtills.getChannel();
        //消息的接收

        //推送的消息如何进行消费的接口回调
        DeliverCallback deliverCallback=(consumerTag, delivery)->{
            String message= new String(delivery.getBody());
            System.out.println("接收到的消息:"+message);
        };
        //取消消费的一个回调接口 如在消费的时候队列被删除掉了
        CancelCallback cancelCallback=(consumerTag)->{
            System.out.println("消息消费被中断");
        };
        /*
         * 消费者消费消息
         * 1.消费哪个队列
         * 2.消费成功之后是否要自动应答 true 代表自动应答 false 手动应答
         * 3.消费者未成功消费的回调
         */
        System.out.println("C2等待接收消息........");
        channel.basicConsume(QUEUE_NAME,true,deliverCallback,cancelCallback);
    }
}
