package com.lagou.controller;

import com.alibaba.fastjson.JSON;
import com.lagou.bean.CovidData;
import com.lagou.bean.Message;
import com.lagou.config.KafkaConfig;
import com.lagou.utils.DataFactory;
import com.lagou.utils.Uuid;
import lombok.extern.slf4j.Slf4j;
import com.lagou.service.KafkaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.List;

/**
 * @Author jiang.weiyu
 * @Date 2021/01/30 3:24
 */
@Slf4j
@RestController
@RequestMapping("/kafka")
public class KafkaController {
    @Autowired
    private KafkaService kafkaService;

    @Autowired
    private KafkaConfig kafkaConfiguration;

    /**
     * 发送文本消息
     *
     * @param msg
     * @return
     */
    @GetMapping("/send/{msg}")
    public String send(@PathVariable String msg) {
        kafkaService.send(kafkaConfiguration.getMyTopic1(), msg);
        return "生产者发送消息给topic1：" + msg;
    }

    /**
     * 发送JSON数据
     *
     * @return
     */
    @GetMapping("/send2")
    public String send2() {
        Message message = new Message();
        message.setId(System.currentTimeMillis());
        message.setMsg("生产者发送消息到topic1: " + Uuid.getUid());
        message.setSendTime(new Date());

        String value = JSON.toJSONString(message);
        log.info("生产者发送消息到topic1 message = {}", value);

        kafkaService.send(kafkaConfiguration.getMyTopic1(), value);
        return value;
    }

    /**
     * 发送JSON数据
     *
     * @return
     */
    @GetMapping("/send3")
    public String send3() {
        Message message = new Message();
        message.setId(System.currentTimeMillis());
        message.setMsg("生产者发送消息到topic2: " + Uuid.getUid());
        message.setSendTime(new Date());

        String value = JSON.toJSONString(message);
        log.info("生产者发送消息到topic2 message = {}", value);

        kafkaService.send(kafkaConfiguration.getMyTopic2(), value);
        return value;
    }

    @GetMapping("/sendCovidData")
    @Scheduled(cron = "0 0 8 * * ?")
    //@Scheduled(initialDelay = 1000,fixedDelay = 30000)
    public void sendData() {
        List<CovidData> covidDataList = DataFactory.getHtml();
        String value = "";
        for (CovidData covidData : covidDataList) {
            value = JSON.toJSONString(covidData);
            kafkaService.send(kafkaConfiguration.getMyTopic1(), value);
            log.info("生产者发送消息到topic message = {}", value);
        }
    }
}
