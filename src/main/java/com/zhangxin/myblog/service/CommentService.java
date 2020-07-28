package com.zhangxin.myblog.service;

import com.zhangxin.myblog.po.Comment;

import java.util.List;

/**
 * @author zhangxin
 * @date 2020/7/28
 */
public interface CommentService {

    //根据博客id获取评论列表
    List<Comment> listCommentByBlogId(Long blogId);

    //保存评论信息
    Comment saveComment(Comment comment);

    //获取评论总数
    Long commentCount();
}
