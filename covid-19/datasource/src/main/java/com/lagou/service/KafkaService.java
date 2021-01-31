package com.lagou.service;

/**
 * @Author jiang.weiyu
 * @Date 2021/01/30 2:47
 */

public interface KafkaService {
    /**
     * 发送消息
     *
     * @param topic 消息主题
     * @param value 消息内容
     */
    public void send(String topic, String value);

    /**
     *
     * @Title: listenPartition0
     * @param records void 返回类型
     */
    //public void listenPartition0(List<ConsumerRecord<String, String>> records);

    /**
     *
     * @Title: listenPartition2
     * @param records void 返回类型
     */
    //public void listenPartition2(List<ConsumerRecord<String, String>> records);
}
