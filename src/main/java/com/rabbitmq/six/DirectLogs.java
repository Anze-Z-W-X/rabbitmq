package com.rabbitmq.six;

import com.rabbitmq.client.Channel;
import com.rabbitmq.utils.RabbitMqUtills;

import java.util.Scanner;

public class DirectLogs {
    //交换机的名称
    public static final String EXCHANGE_NAME = "direct_logs";

    public static void main(String[] args) throws Exception {
        Channel channel = RabbitMqUtills.getChannel();
        //channel.exchangeDeclare(EXCHANGE_NAME,"fanout");  已经声明过了

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
            //s1为routing key
            channel.basicPublish(EXCHANGE_NAME,"error",null,message.getBytes("UTF-8"));
            System.out.println("生产者发出消息:"+message);
        }
    }
}
