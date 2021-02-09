package com.lagou.mapper;

import com.lagou.domin.Article;
import org.apache.ibatis.annotations.Many;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.mapping.FetchType;

import java.util.List;

/**
 * @Author jiang.weiyu
 * @Date 2021/02/09 13:54
 */
public interface ArticleMapper {
    /*查询有所文章信息*/
    @Select("select id,title,content from t_article")
    @Results({
            @Result(property = "id", column = "id", id = true),
            @Result(property = "title", column = "title"),
            @Result(property = "text", column = "text"),
            @Result(property = "commentList",column = "id",javaType = List.class,
                    many = @Many(select = "com.lagou.mapper.CommentMapper.findCommentById",fetchType = FetchType.LAZY))
    })
    public List<Article> findAllComment();
}
