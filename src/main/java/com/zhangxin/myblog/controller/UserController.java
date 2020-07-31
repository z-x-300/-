package com.zhangxin.myblog.controller;

import com.zhangxin.myblog.po.User;
import com.zhangxin.myblog.service.BlogService;
import com.zhangxin.myblog.service.CommentService;
import com.zhangxin.myblog.service.UserService;
import com.zhangxin.myblog.util.CodeUtil;
import com.zhangxin.myblog.util.MD5Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailSendException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author zhangxin
 * @date 2020/7/31
 */
@Controller
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private BlogService blogService;

    @Autowired
    private CommentService commentService;

    @Value("${mail.fromMail.sender}")
    private String sender ;// 发送者

    @Autowired
    private JavaMailSender javaMailSender;

    private Map<String, Object> resultMap = new HashMap<>();

    //跳转后台登录
    @GetMapping("/admin")
    public String adminLoginPage() {

        return "admin/login";
    }

    //跳转到前端登录
    @GetMapping("/login")
    public String loginPage() {

        return "/login";
    }

    //跳转到后台主页面
    @GetMapping("/admin/index")
    public String indexPage(){

        return "admin/index";
    }

    //跳转到注册页面
    @GetMapping("/register")
    public String register(){

        return "/register";
    }

    //跳转到个人中心页面
    @GetMapping("/personal")
    public String personal(HttpSession session,Model model){

        User user=(User)session.getAttribute("user");
        model.addAttribute("user",userService.findUserByUserId(user));
        return "personal-center";
    }

    @GetMapping("/updatepassword")
    public String uodatePassword(HttpSession session,Model model){
        User user=(User)session.getAttribute("user");
        model.addAttribute("user",userService.findUserByUserId(user));
        return "updatepassword";
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

    @RequestMapping("/sendEmail")
    //转换json数据  @ResponseBody
    @ResponseBody
    public String sendEmail(String email) {
        SimpleMailMessage message = new SimpleMailMessage();
        String code = VerifyCode(6);    //随机数生成6位验证码
        message.setFrom(sender);
        message.setTo(email);
        message.setSubject("博客系统");// 标题
        message.setText("【博客系统】你的验证码为："+code+"，有效时间为5分钟(若不是本人操作，可忽略该条邮件)");// 内容
        try {
            javaMailSender.send(message);
            // logger.info("文本邮件发送成功！");
            saveCode(code);
            return "success";
        }catch (MailSendException e){
            //logger.error("目标邮箱不存在");
            return "false";
        } catch (Exception e) {
            //logger.error("文本邮件发送异常！", e);
            return "failure";
        }
    }

    private String VerifyCode(int n){
        Random r = new Random();
        StringBuffer sb =new StringBuffer();
        for(int i = 0;i < n;i ++){
            int ran1 = r.nextInt(10);
            sb.append(String.valueOf(ran1));
        }
//        System.out.println(sb);
        return sb.toString();
    }

    //保存验证码和时间
    private void saveCode(String code){
        SimpleDateFormat sf = new SimpleDateFormat("yyyyMMddHHmmss");
        Calendar c = Calendar.getInstance();
        c.add(Calendar.MINUTE, 5);
        String currentTime = sf.format(c.getTime());// 生成5分钟后时间，用户校验是否过期

        String hash =  MD5Utils.code(code);//生成MD5值
        resultMap.put("hash", hash);
        resultMap.put("tamp", currentTime);
    }

    @ResponseBody
    @PostMapping("/register")
    public String register(String username,String password,String confirmpassword, String email,
                           String identify) {
        if (resultMap.size() ==0){
            return "验证码生成错误，请重新发送！";
        }

        User user =userService.findUserByUserName(username);
        //判断验证码是否正确
        String requestHash = resultMap.get("hash").toString();

        String tamp = resultMap.get("tamp").toString();
        SimpleDateFormat sf = new SimpleDateFormat("yyyyMMddHHmmss");//当前时间
        Calendar c = Calendar.getInstance();
        String currentTime = sf.format(c.getTime());
        if (tamp.compareTo(currentTime) > 0) {
            String hash =  MD5Utils.code(identify);//生成MD5值

            if (!password.equals(confirmpassword)){
                //attributes.addFlashAttribute("message", "");
                return "两次密码不正确！";
            }else if (!hash.equalsIgnoreCase(requestHash)){
                //attributes.addFlashAttribute("message", "");
                return "验证码输入不正确！";
            }else if (user!=null){
                //attributes.addFlashAttribute("message", "");
                return "该用户名已经被注册！";
            }else {
                //校验成功
                User u =new User();
                u.setUsername(username);
                u.setPassword(MD5Utils.code(password));
                u.setEmail(email);
                u.setType(0);
                String nickName ="游客"+(int)((Math.random()*9+1)*1000);
                u.setNickname(nickName);
                u.setAvatar("/images/avatar.png");
                u.setCreateTime(new Date());
                u.setUpdateTime(new Date());
                User user1 =userService.saveUser(u);
                //attributes.addFlashAttribute("m", "");
                return "成功";
            }
        } else {
            // 超时
            System.out.println("3");
            //attributes.addFlashAttribute("message", "验证码已过期");
            return "验证码已过期！";
        }

    }

    //修改用户信息
    @PostMapping("/updateinformation")
    public String updateInformation(User user,HttpSession session, RedirectAttributes attributes){

        User user1 =userService.updateUser(user.getId(),user);
        if (user1 == null) {
            attributes.addFlashAttribute("errorMessage", "提示：操作失败！");
        } else {
            session.setAttribute("user",userService.findUserByUserId(user1));
            attributes.addFlashAttribute("successMessage", "提示：操作成功！");
        }
        return "redirect:/personal";
    }

    //修改密码
    //修改用户信息
    @PostMapping("/updatepassword")
    public String updatePassword(@RequestParam Long id,
                                 @RequestParam String username,
                                 @RequestParam String oldpassword,
                                 @RequestParam String newpassword,
                                 @RequestParam String confirmpassword,
                                 HttpSession session, RedirectAttributes attributes){

        User u =userService.checkUser(username,oldpassword);

        if (!newpassword.equals(confirmpassword)){
            attributes.addFlashAttribute("errorMessage", "提示：新密码和确认密码不一致！");
            return "redirect:/updatepassword";
        }else if (u==null){
            attributes.addFlashAttribute("errorMessage", "提示：旧密码错误！");
            return "redirect:/updatepassword";
        }else {
            User user =new User();
            user.setPassword(MD5Utils.code(newpassword));
            User user1 =userService.updateUser(id,user);
            if (user1 == null) {
                attributes.addFlashAttribute("errorMessage", "提示：操作失败！");
                return "redirect:/updatepassword";
            } else {
                session.removeAttribute("user");
                attributes.addFlashAttribute("successMessage", "提示：操作成功！");
                return "redirect:/";
            }

        }

    }
}
