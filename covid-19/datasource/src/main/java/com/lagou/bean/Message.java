package com.lagou.bean;

import java.util.Date;

import lombok.Data;
import lombok.ToString;

/**
 * @Author jiang.weiyu
 * @Date 2021/01/30 3:18
 */
@Data
@ToString
public class Message {
    private Long id;
    private String msg;
    private Date sendTime;
}
