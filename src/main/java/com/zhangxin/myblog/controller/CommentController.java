package com.zhangxin.myblog.controller;

import com.zhangxin.myblog.po.Comment;
import com.zhangxin.myblog.po.User;
import com.zhangxin.myblog.service.BlogService;
import com.zhangxin.myblog.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpSession;

/**
 * @author zhangxin
 * @date 2020/7/28
 */

@Controller
public class CommentController {

    @Autowired
    private CommentService commentService;

    @Autowired
    private BlogService blogService;

    @Value("${comment.avatar}")
    private String avatar;

    //返回评论列表
    @GetMapping("/comments/{blogId}")
    public String comments(@PathVariable Long blogId, Model model){

        //根据博客id获取评论列表
        model.addAttribute("comments", commentService.listCommentByBlogId(blogId));
        return "details :: commentList";
    }

    @PostMapping("/comments")
    public String post(Comment comment, HttpSession session){

        //获取评论的博客id
        Long blogId =comment.getBlog().getId();
        //根据博客id查询博客对象，并赋值给该评论（前端只传来博客id）
        comment.setBlog(blogService.getBlog(blogId));
        //获取管理员信息
        User user=(User)session.getAttribute("user");
        if (user!=null&&user.getType()==1){//管理员登录，设置管理员信息
            comment.setAvatar(user.getAvatar());
            comment.setAdminComment(true);
//            comment.setNickname(user.getNickname());
        }else {
            //设置头像
            comment.setAvatar(user.getAvatar());
            comment.setNickname(user.getNickname());

        }

        //保存评论
        commentService.saveComment(comment);
        return "redirect:/comments/"+comment.getBlog().getId();
    }

}
