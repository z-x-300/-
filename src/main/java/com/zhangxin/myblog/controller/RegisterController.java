package com.zhangxin.myblog.controller;

import com.zhangxin.myblog.po.User;
import com.zhangxin.myblog.service.UserService;
import com.zhangxin.myblog.util.MD5Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailSendException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author zhangxin
 * @date 2020/7/30
 */
@Controller
public class RegisterController {

    @Autowired
    private UserService userService;

    @Value("${mail.fromMail.sender}")
    private String sender ;// 发送者

    @Autowired
    private JavaMailSender javaMailSender;

    private Map<String, Object> resultMap = new HashMap<>();

    @GetMapping("/register")
    public String register(){

        return "/register";
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
}
