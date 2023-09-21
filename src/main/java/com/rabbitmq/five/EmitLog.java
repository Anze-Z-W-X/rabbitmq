package com.rabbitmq.five;

import com.rabbitmq.client.Channel;
import com.rabbitmq.utils.RabbitMqUtills;

import java.util.Scanner;

/*
* 发消息给交换机
* */
public class EmitLog {
    //交换机的名称
    public static final String EXCHANGE_NAME = "logs";

    public static void main(String[] args) throws Exception {
        Channel channel = RabbitMqUtills.getChannel();
        channel.exchangeDeclare(EXCHANGE_NAME,"fanout");

        Scanner scanner = new Scanner(System.in);

        while(scanner.hasNext()){
            String  message = scanner.next();
            /*
             * 发送一个消息
             * 1.发送到那个交换机
             * 2.Routing key 是哪个
             * 3.其他的参数信息
             * 4.发送消息的消息体
             * */
            channel.basicPublish(EXCHANGE_NAME,"",null,message.getBytes("UTF-8"));
            System.out.println("生产者发出消息:"+message);
        }
    }
}
