package com.rabbitmq.eight;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.CancelCallback;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;
import com.rabbitmq.utils.RabbitMqUtills;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class Consumer01 {
    //普通交换机
    public static final String NORMAL_EXCHANGE="normal-exchange";
    //死信交换机
    public static final String DEAD_EXCHANGE="dead-exchange";
    //普通队列
    public static final String NORMAL_QUEUE="normal-queue";
    //死信队列
    public static final String DEAD_QUEUE="dead-queue";

    public static void main(String[] args) throws Exception {
        Channel channel = RabbitMqUtills.getChannel();
        //声明死信和普通交换机，类型为direct
        channel.exchangeDeclare(NORMAL_EXCHANGE, BuiltinExchangeType.DIRECT);
        channel.exchangeDeclare(DEAD_EXCHANGE, BuiltinExchangeType.DIRECT);
        //声明普通队列,指明参数map后，可将死信交给死信交换机
        Map<String ,Object> arguments = new HashMap<>();
        //正常队列设置死信交换机
        //过期时间,可以由生产者设置
        //arguments.put("x-message-ttl",100000);
        arguments.put("x-dead-letter-exchange",DEAD_EXCHANGE);
        //设置死信RoutingKey
        arguments.put("x-dead-letter-routing-key","lisi");
        //设置队列最大长度
        //arguments.put("x-max-length",6);
        channel.queueDeclare(NORMAL_QUEUE,false,false,false,arguments);
        //声明死信队列
        channel.queueDeclare(DEAD_QUEUE,false,false,false,null);

        //绑定 普通的交换机与队列
        channel.queueBind(NORMAL_QUEUE,NORMAL_EXCHANGE,"zhangsan");
        //绑定死信的交换机和队列
        channel.queueBind(DEAD_QUEUE,DEAD_EXCHANGE,"lisi");
        System.out.println("等待接收消息......");

        DeliverCallback deliverCallback =
                (consumerTag,message)->{
                    System.out.println("Consumer01接收的消息是:"+new String(message.getBody(), StandardCharsets.UTF_8));
                };

        CancelCallback cancelCallback = consumerTag->{};
        channel.basicConsume(NORMAL_QUEUE,false,deliverCallback,cancelCallback);
    }

}
