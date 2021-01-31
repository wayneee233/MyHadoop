package com.lagou.service.impl;

import com.lagou.service.KafkaService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;
import org.springframework.util.concurrent.ListenableFuture;

/**
 * @Author jiang.weiyu
 * @Date 2021/01/30 3:29
 */
@Slf4j
@Service
public class KafkaServiceImpl implements KafkaService {

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    @Override
    public void send(String topic, String value) {
        ListenableFuture<SendResult<String, String>> resultListenableFuture = kafkaTemplate.send(topic, value);
        resultListenableFuture.addCallback(
                successCallback -> log.info("发送成功：topic= " + topic + " value= " + value),
                failureCallback -> log.info("发送失败：topic= " + topic + " value= " + value));
    }


    /*
     @Override
     @KafkaListener(topics = {"xiaotest1"}, containerFactory = "kafkaListenerContainerFactory0")
     public void listenPartition0(List<ConsumerRecord<String, String>> records) {
     for (ConsumerRecord<String, String> consumerRecord : records){
     String value = consumerRecord.value();
     logger.info("a 消息：partition " + consumerRecord.partition() + " value " + value );
     }
     }

     @Override
     @KafkaListener(topics = {"fptest"}, containerFactory = "kafkaListenerContainerFactory1")
     public void listenPartition2(List<ConsumerRecord<String, String>> records) {
     for (ConsumerRecord<String, String> consumerRecord : records){
     String value = consumerRecord.value();
     try {
     Thread.sleep(10000);
     } catch (InterruptedException e) {
     e.printStackTrace();
     }
     logger.info("c 消息：partition " + consumerRecord.partition() + " value " + value + " thread id " + Thread.currentThread().getName());
     }
     }*/
}
