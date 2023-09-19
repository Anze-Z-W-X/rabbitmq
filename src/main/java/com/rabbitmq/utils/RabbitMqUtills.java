package com.rabbitmq.utils;

import com.rabbitmq.client.*;

public class RabbitMqUtills {
    public static Channel getChannel() throws Exception{
        //创建一个链接工厂
        ConnectionFactory factory = new ConnectionFactory();
        //工厂ip，连接RabbitMQ队列
        factory.setHost("192.168.164.129");
        //用户名
        factory.setUsername("admin");
        //密码
        factory.setPassword("@200312Zwx");
        Connection connection = factory.newConnection();
        //获取信道
        Channel channel = connection.createChannel();
        return channel;
    }
}
