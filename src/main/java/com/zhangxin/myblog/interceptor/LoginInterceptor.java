package com.zhangxin.myblog.interceptor;

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

        //用户未登录
        if (request.getSession().getAttribute("user") == null) {
            //重定向到登录页面
            response.sendRedirect("/admin");
            return false;
        }
        return true;
    }
}
