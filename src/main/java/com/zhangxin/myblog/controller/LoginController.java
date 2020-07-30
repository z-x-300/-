package com.zhangxin.myblog.controller;

import com.zhangxin.myblog.po.User;
import com.zhangxin.myblog.service.BlogService;
import com.zhangxin.myblog.service.CommentService;
import com.zhangxin.myblog.service.UserService;
import com.zhangxin.myblog.util.CodeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * @author zhangxin
 * @date 2020/7/25
 */
//登录
@Controller
public class LoginController {

    @Autowired
    private UserService userService;

    @Autowired
    private BlogService blogService;

    @Autowired
    private CommentService commentService;

    @GetMapping("/admin")
    public String adminLoginPage() {

        return "admin/login";
    }

    @GetMapping("/login")
    public String loginPage() {

        return "/login";
    }

    @GetMapping("/admin/index")
    public String indexPage(){

        return "admin/index";
    }

    //前端登录
    @PostMapping("/login")
    public String login(@RequestParam String username,
                        @RequestParam String password,
                        HttpSession session,
                        RedirectAttributes redirectAttributes,
                        HttpServletRequest request) {


        if (!CodeUtil.checkVerifyCode(request)) {
            redirectAttributes.addFlashAttribute("message", "验证码错误！");
            return "redirect:/login";
        } else {
            User user = userService.checkUser(username, password);

            if (user != null) {
                user.setPassword(null);
                session.setAttribute("user", user);
                return "redirect:/";
            } else {
                redirectAttributes.addFlashAttribute("message", "用户名和密码错误！");
                return "redirect:/login";
            }
        }

    }

    //后台登录
    @PostMapping("/admin/login")
    public String adminLogin(@RequestParam String username,
                        @RequestParam String password,
                        HttpSession session,
                        RedirectAttributes redirectAttributes,
                        HttpServletRequest request) {


        if (!CodeUtil.checkVerifyCode(request)) {
            redirectAttributes.addFlashAttribute("message", "验证码错误！");
            return "redirect:/admin";
        } else {
            User user = userService.checkUser(username, password);

            if (user != null) {
                if (user.getType()!=1){
                    redirectAttributes.addFlashAttribute("message", "你不是管理员，无法进入后台！");
                    return "redirect:/";
                }else {
                    user.setPassword(null);
                    session.setAttribute("user", user);
                    return "admin/index";
                }
            } else {
                redirectAttributes.addFlashAttribute("message", "用户名和密码错误！");
                return "redirect:/admin";
            }
        }

    }

    //模态框登录
    @PostMapping("/model/login")
    public String modelLogin(@RequestParam String username,
                        @RequestParam String password,
                        @RequestParam Long blogId,
                        HttpSession session,
                        RedirectAttributes redirectAttributes,
                        HttpServletRequest request) {


        if (!CodeUtil.checkVerifyCode(request)) {
            redirectAttributes.addFlashAttribute("message", "验证码错误！");
            return "redirect:/blog/"+blogId;
        } else {
            User user = userService.checkUser(username, password);

            if (user != null) {
                user.setPassword(null);
                session.setAttribute("user", user);
                return "redirect:/blog/"+blogId;
            } else {
                redirectAttributes.addFlashAttribute("message", "用户名和密码错误！");
                return "redirect:/blog/"+blogId;
            }
        }

    }
    //前端登出
    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.removeAttribute("user");
        return "redirect:/";
    }

    //后台登出
    @GetMapping("/admin/logout")
    public String adminLogout(HttpSession session) {
        session.removeAttribute("user");
        return "redirect:/admin";
    }

    //博客底部信息
    @GetMapping("/admin/footer/blogmessage")
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
