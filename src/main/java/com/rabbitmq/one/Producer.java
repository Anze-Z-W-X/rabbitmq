package com.rabbitmq.one;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.utils.RabbitMqUtills;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class Producer {
    //队列名称
    public static final String QUEUE_NAME="hello";
    //发消息
    public static void main(String[] args) throws Exception {
        Channel channel = RabbitMqUtills.getChannel();
        /*
        *1.队列名称
        *2.队列消息是否持久化
        * 3.该队列是否允许多个消费者共享
        * 4.是否自动删除
        * 5。其他参数
        * */
        String message = "hello world";

        channel.queueDeclare(QUEUE_NAME,false,false,false,null);

        /*
         * 发送一个消息
         * 1.发送到那个交换机
         * 2.路由的 key 是哪个
         * 3.其他的参数信息
         * 4.发送消息的消息体
         * */

        channel.basicPublish("",QUEUE_NAME,null,message.getBytes());
    }
}
