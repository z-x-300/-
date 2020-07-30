package com.zhangxin.myblog.interceptor;

import com.zhangxin.myblog.po.User;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author zhangxin
 * @date 2020/7/25
 */

//拦截器
public class LoginInterceptor extends HandlerInterceptorAdapter {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        User user =(User)request.getSession().getAttribute("user");
        //用户未登录
        if ( user== null) {
            //重定向到登录页面
            response.sendRedirect("/admin");
            return false;
        }else if (user.getType()!=1){//用户不是管理员
            //重定向到主页面
            response.sendRedirect("/");
        }
        return true;
    }
}
