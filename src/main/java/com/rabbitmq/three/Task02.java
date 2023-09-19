package com.rabbitmq.three;

import com.rabbitmq.client.Channel;
import com.rabbitmq.utils.RabbitMqUtills;

import java.util.Scanner;

public class Task02 {
    //队列名称
    public static final String TASK_QUEUE_NAME="ack_queue";

    public static void main(String[] args) throws Exception {
        Channel channel = RabbitMqUtills.getChannel();

        channel.queueDeclare(TASK_QUEUE_NAME,false,false,false,null);

        //从控制台中输入
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNext()){
            String message = scanner.next();
            channel.basicPublish("",TASK_QUEUE_NAME,null,message.getBytes("UTF-8"));
            System.out.println("生产者发出消息:"+message);
        }
    }
}
