package com.rabbitmq.three;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.MessageProperties;
import com.rabbitmq.utils.RabbitMqUtills;

import java.util.Scanner;

public class Task02 {
    //队列名称
    public static final String TASK_QUEUE_NAME="ack_queue";

    public static void main(String[] args) throws Exception {
        Channel channel = RabbitMqUtills.getChannel();
        //队列持久化，即使重启rabbitmq后队列依然存在
        boolean durable = true;
        channel.queueDeclare(TASK_QUEUE_NAME,durable,false,false,null);

        //从控制台中输入
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNext()){
            String message = scanner.next();
            //消息持久化，在basicProperties参数列表中不写null而是写入MessageProperties.PERSISTENT_TEXT_PLAIN,可实现不保证的持久化
            channel.basicPublish("",TASK_QUEUE_NAME,null ,message.getBytes("UTF-8"));
            System.out.println("生产者发出消息:"+message);
        }
    }
}
