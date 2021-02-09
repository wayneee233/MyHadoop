package com.lagou.service.impl;

import com.lagou.mapper.ArticleMapper;
import com.lagou.domin.Article;
import com.lagou.service.ArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Author jiang.weiyu
 * @Date 2021/02/09 13:58
 */
@Service
public class ArticleServiceImpl implements ArticleService {

    @Autowired
    private ArticleMapper articleMapper;

    @Override
    public List<Article> findAllComment() {
        return articleMapper.findAllComment();
    }
}
