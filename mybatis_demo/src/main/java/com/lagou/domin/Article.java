package com.lagou.domin;

import lombok.Data;
import java.util.List;

/**
 * @Author jiang.weiyu
 * @Date 2021/02/09 0:40
 */
@Data
public class Article {
    private Integer id;
    private String title;
    private String text;

    private List<Comment> commentList;
}
