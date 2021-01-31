package com.lagou.config;

import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.stereotype.Component;

import java.io.Serializable;

/**
 * @Author jiang.weiyu
 * @Date 2021/01/30 2:39
 */
@Component
@ConfigurationPropertiesScan("kafka.topic")
public class KafkaProperties implements Serializable {
    private String groupId;
    private String[] topicName;

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String[] getTopicName() {
        return topicName;
    }

    public void setTopicName(String[] topicName) {
        this.topicName = topicName;
    }
}
