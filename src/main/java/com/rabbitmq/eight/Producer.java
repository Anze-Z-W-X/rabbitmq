package com.rabbitmq.eight;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.utils.RabbitMqUtills;

/*
* 死信队列之生产者代码
* */
public class Producer {
    //普通交换机
    public static final String NORMAL_EXCHANGE="normal-exchange";

    public static void main(String[] args) throws Exception {
        Channel channel = RabbitMqUtills.getChannel();
        //死信消息,设置TTL,10s
        AMQP.BasicProperties properties =
                new AMQP.BasicProperties().builder().expiration("10000").build();
        for(int i=0;i<10;i++){
            String message = "info"+i;
            channel.basicPublish(NORMAL_EXCHANGE,"zhangsan",properties,message.getBytes());
        }
    }
}
