package com.lagou.domin;

import lombok.Data;
import org.springframework.stereotype.Component;

/**
 * @Author jiang.weiyu
 * @Date 2021/02/09 0:40
 */
@Data
public class Comment {
    private Integer id;
    private String content;
    private String author;
    private Integer uid;
}
