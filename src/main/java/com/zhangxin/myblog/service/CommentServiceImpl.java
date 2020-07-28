package com.zhangxin.myblog.service;

import com.zhangxin.myblog.dao.CommentRepository;
import com.zhangxin.myblog.po.Comment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * @author zhangxin
 * @date 2020/7/28
 */
@Service
public class CommentServiceImpl implements CommentService {

    @Autowired
    private CommentRepository commentRepository;

    //根据博客id获取评论列表
    @Override
    public List<Comment> listCommentByBlogId(Long blogId) {
        Sort sort = Sort.by(Sort.Direction.DESC, "createTime");
        return commentRepository.findByBlogId(blogId, sort);
    }

    //保存评论信息
    @Transactional
    @Override
    public Comment saveComment(Comment comment) {
        //获取当前评论的父评论，没有默认为-1
        Long parentCommentId = comment.getParentComment().getId();
        if (parentCommentId != -1) {//父评论id不等于-1，表示该评论存在(即传进来的评论是子评论)
            //设置该评论的父评论
            comment.setParentComment(commentRepository.getOne(parentCommentId));
        } else {//父评论不存在(即传进来的评论是父评论)
            comment.setParentComment(null);
        }

        //初始化评论创建id
        comment.setCreateTime(new Date());
        return commentRepository.save(comment);
    }
}
