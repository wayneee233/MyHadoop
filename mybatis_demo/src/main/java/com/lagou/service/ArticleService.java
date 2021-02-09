package com.lagou.service;

import com.lagou.domin.Article;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Author jiang.weiyu
 * @Date 2021/02/09 13:58
 */
public interface ArticleService {
    /*查询文章信息及用户评论*/
    public List<Article> findAllComment();
}
