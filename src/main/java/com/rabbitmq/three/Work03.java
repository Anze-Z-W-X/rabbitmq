package com.rabbitmq.three;

import com.rabbitmq.client.CancelCallback;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;
import com.rabbitmq.utils.RabbitMqUtills;
import com.rabbitmq.utils.SleepUtils;

public class Work03 {
    public static final String TASK_QUEUE_NAME="ack_queue";

    public static void main(String[] args) throws Exception {
        Channel channel = RabbitMqUtills.getChannel();
        System.out.println("C1等待消息处理（时间较短）");

        DeliverCallback deliverCallback = (consumerTag,message) ->{
            //沉睡1s
            SleepUtils.sleep(1);
            System.out.println("接收到的消息:"+new String(message.getBody(),"UTF-8"));
            /*
            * 1.消息的标记：tag
            * 2.是否批量应答
            *
            * */
            channel.basicAck(message.getEnvelope().getDeliveryTag(),false);
        };
        CancelCallback cancelCallback=(consumerTag)->{
            System.out.println("消息消费被中断");
        };
        //采用手动应答
        boolean autoAck = false;
        channel.basicConsume(TASK_QUEUE_NAME,autoAck,deliverCallback,cancelCallback);
    }
}
