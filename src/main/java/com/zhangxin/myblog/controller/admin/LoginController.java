package com.zhangxin.myblog.controller.admin;

import com.zhangxin.myblog.po.User;
import com.zhangxin.myblog.service.BlogService;
import com.zhangxin.myblog.service.CommentService;
import com.zhangxin.myblog.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;

/**
 * @author zhangxin
 * @date 2020/7/25
 */
//登录
@Controller
@RequestMapping("/admin")
public class LoginController {

    @Autowired
    private UserService userService;

    @Autowired
    private BlogService blogService;

    @Autowired
    private CommentService commentService;

    @GetMapping
    public String loginPage() {

        return "admin/login";
    }

    //登录
    @PostMapping("/login")
    public String login(@RequestParam String username,
                        @RequestParam String password,
                        HttpSession session,
                        RedirectAttributes redirectAttributes) {

        User user = userService.checkUser(username, password);

        if (user != null) {
            user.setPassword(null);
            session.setAttribute("user", user);
            return "admin/index";
        } else {
            redirectAttributes.addFlashAttribute("message", "用户名和密码错误！");
            return "redirect:/admin";
        }
    }

    //登出
    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.removeAttribute("user");
        return "redirect:/admin";
    }

    //博客底部信息
    @GetMapping("/footer/blogmessage")
    public String blogMessage(Model model){
        Long blogTotal = blogService.countBlog();
        Long blogViewTotal = blogService.blogViewCount();
        Long blogCommentTotal = commentService.commentCount();
        // int blogMessageTotal = blogService.getBlogMessageTotal();

        model.addAttribute("blogTotal",blogTotal);
        model.addAttribute("blogViewTotal",blogViewTotal);
        model.addAttribute("blogCommentTotal",blogCommentTotal);
        // model.addAttribute("blogMessageTotal",blogMessageTotal);
        return "admin/fragments :: blogMessage";
    }
}
