package com.rabbitmq.utils;
/*
* 睡眠工具类
* */
public class SleepUtils {
    public static void sleep(int second){
        try{
            Thread.sleep(1000*second);
        }catch (InterruptedException _ignore){
            Thread.currentThread().interrupt();
        }
    }
}
