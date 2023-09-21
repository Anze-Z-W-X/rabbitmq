package com.rabbitmq.four;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.ConfirmCallback;
import com.rabbitmq.utils.RabbitMqUtills;

import java.util.UUID;
import java.util.concurrent.ConcurrentNavigableMap;
import java.util.concurrent.ConcurrentSkipListMap;

/*
* 发布确认模式
* 使用的时间
* 1.单个确认
* 2.批量确认
* 3.异步批量确认
* */
public class ConfirmMessage {
    //批量发消息的个数
    public static final int MESSAGE_COUNT=100;

    public static void main(String[] args) throws Exception {
        //1.单个确认
        publishMessageIndividually();   //发布100条单个确认的消息耗时95ms
        //2.批量确认
        publishMessageBatch(); //发布100条批量确认的消息耗时30ms
        //3.异步批量确认
        publishMessageAsync();//发布100条异步发布确认的消息耗时14ms
    }

    //单个确认
    public static void publishMessageIndividually() throws Exception{
        Channel channel = RabbitMqUtills.getChannel();
        //队列的申明
        String queueName = UUID.randomUUID().toString();
        channel.queueDeclare(queueName,true,false,false,null);
        //开启发布确认
        channel.confirmSelect();
        //开始时间
        long begin = System.currentTimeMillis();

        //批量发消息
        for(int i=0;i<MESSAGE_COUNT;i++){
            String message = i+"";
            channel.basicPublish("",queueName,null,message.getBytes());
            //单个消息发布就马上确认
            boolean flag = channel.waitForConfirms();
            if(flag){
                System.out.println("消息发送成功");
            }
        }

        //结束时间
        long end = System.currentTimeMillis();
        System.out.println("发布"+MESSAGE_COUNT+"个单独确认的消息，耗时："+(end-begin)+"ms");
    }

    //批量发布确认
    public static void publishMessageBatch()throws Exception{
        Channel channel = RabbitMqUtills.getChannel();
        //队列的申明
        String queueName = UUID.randomUUID().toString();
        channel.queueDeclare(queueName,true,false,false,null);
        //开启发布确认
        channel.confirmSelect();
        //开始时间
        long begin = System.currentTimeMillis();

        //批量确认消息大小
        int batchSize = 10;
        boolean flag = false;
        //批量发消息
        for(int i=0;i<MESSAGE_COUNT;i++){
            String message = i+"";
            channel.basicPublish("",queueName,null,message.getBytes());
            //发布确认
            if((i+1)%batchSize==0){
                flag = channel.waitForConfirms();
            }
            if(flag){
                System.out.println("消息发送成功");
            }
        }
        //结束时间
        long end = System.currentTimeMillis();
        System.out.println("发布"+MESSAGE_COUNT+"个批量确认的消息，耗时："+(end-begin)+"ms");
    }

    //异步发布确认
    public static void publishMessageAsync() throws Exception {
        Channel channel = RabbitMqUtills.getChannel();
        //队列的申明
        String queueName = UUID.randomUUID().toString();
        channel.queueDeclare(queueName,true,false,false,null);
        //开启发布确认
        channel.confirmSelect();

        /*
        * 线程安全有序的一个哈希表，适用于高并发的情况下
        *1.轻松的将序号和消息关联
        * 2.可批量删除条目，只要给到序号
        * 3.支持高并发（多线程）
        * */
        ConcurrentSkipListMap<Long,String> outstandingConfirms =
                new ConcurrentSkipListMap<>();

        //准备消息的监听器
        //消息确认成功回调函数
        ConfirmCallback ackCallback = (deliveryTag,multiple)->{
            if(multiple){
                //2：删除掉已经确认的消息，剩下的就是未确认的消息
                ConcurrentNavigableMap<Long, String> confirmed =
                        outstandingConfirms.headMap(deliveryTag);
            }else {
                outstandingConfirms.remove(deliveryTag);
            }
            System.out.println("确认的消息:"+deliveryTag);
        };
        //消息确认失败回调函数
        ConfirmCallback nackCallback = (deliveryTag,multiple)->{
            //3：打印一下未确认的消息有哪些
            String message = outstandingConfirms.get(deliveryTag);
            System.out.println("未确认的消息是:"+message+",未确认的消息:"+deliveryTag);
        };
        channel.addConfirmListener(ackCallback,nackCallback);

        //开始时间
        long begin = System.currentTimeMillis();
        //批量发送消息
        for(int i=0;i<MESSAGE_COUNT;i++){
            String message = "消息"+i;
            channel.basicPublish("",queueName,null,message.getBytes());
            //1：此处记录下所有要发送的消息，消息的总和
            outstandingConfirms.put(channel.getNextPublishSeqNo(),message);
        }

        //结束时间
        long end = System.currentTimeMillis();
        System.out.println("发布"+MESSAGE_COUNT+"个异步发布确认的消息，耗时："+(end-begin)+"ms");

    }
}
