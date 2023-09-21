package com.rabbitmq.five;

import com.rabbitmq.client.CancelCallback;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;
import com.rabbitmq.utils.RabbitMqUtills;

/*
* 消息接收 交换机
* */
public class RecieveLog02 {
    //交换机的名称
    public static final String EXCHANGE_NAME = "logs";

    public static void main(String[] args) throws Exception {
        Channel channel = RabbitMqUtills.getChannel();
        //声明一个交换机
        channel.exchangeDeclare(EXCHANGE_NAME,"fanout");
        //声明一个临时队列
        String queueName = channel.queueDeclare().getQueue();
        /*
        * 绑定交换机和队列
        * */
        channel.queueBind(queueName,EXCHANGE_NAME,"");//s2为Routing ket
        System.out.println("等待接收消息，把接受的消息打印在屏幕上......");

        //接收消息
        DeliverCallback deliverCallback = (consumerTag,message)->{
            System.out.println("RecieveLog02控制台打印接收的消息");
        };
        //消费者取消消息回调接口
        CancelCallback cancelCallback=(consumerTag)->{
            System.out.println("消息消费被中断");
        };
        channel.basicConsume(queueName,true,deliverCallback,cancelCallback);
    }
}
