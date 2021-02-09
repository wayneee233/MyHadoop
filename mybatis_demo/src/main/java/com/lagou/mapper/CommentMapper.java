package com.lagou.mapper;

import com.lagou.domin.Comment;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @Author jiang.weiyu
 * @Date 2021/2/9 22:08
 */
public interface CommentMapper {

    /*根据id查看评论*/
    @Select("select id,content,author from t_comment where a_id = #{aid}")
    List<Comment> findCommentById(Integer aid);
}
