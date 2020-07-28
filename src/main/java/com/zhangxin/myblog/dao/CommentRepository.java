package com.zhangxin.myblog.dao;

import com.zhangxin.myblog.po.Comment;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * @author zhangxin
 * @date 2020/7/28
 */
public interface CommentRepository extends JpaRepository<Comment,Long> {

    //根据博客id获取评论列表
    List<Comment> findByBlogId(Long blogId, Sort sort);
}
