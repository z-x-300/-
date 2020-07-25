package com.zhangxin.myblog.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;

/**
 * @author zhangxin
 * @date 2020/7/25
 * 功能 记录日志内容(请求 url，访问者ip 调用的方法classMethod 参数 args 返回内容)
 */

@Aspect
@Component
public class LogAspect {

    //获取日志对象
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Pointcut("execution(* com.zhangxin.myblog.controller.*.*(..))")
    public void log() {
    }

    @Before("log()")
    public void doBefore(JoinPoint joinPoint) {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request=attributes.getRequest();
        String url =request.getRequestURL().toString();//获取请求url
        String ip =request.getRemoteAddr();//获取请求地址
        String classMethod= joinPoint.getSignature().getDeclaringTypeName()+"."+joinPoint.getSignature().getName();//获取方法名
        Object[] args =joinPoint.getArgs();//获取参数
        RequestLog requestLog = new RequestLog(url, ip, classMethod, args);
        logger.info("--------------doBefore-------------------");
        logger.info("Request:{}",requestLog);
    }

    @After("log()")
    public void doAfter() { logger.info("--------------doAfter--------------------");
    }


    @AfterReturning(returning = "result", pointcut = "log()")
    public void doAfterReturn(Object result) {
        logger.info("Result : {}" + result);
    }

    //请求日志
    private class RequestLog{
        private String url; //请求 url
        private String ip;//访问者ip
        private String classMethod;//调用的方法classMethod
        private Object[] args;//参数 args

        public RequestLog(String url, String ip, String classMethod, Object[] args) {
            this.url = url;
            this.ip = ip;
            this.classMethod = classMethod;
            this.args = args;
        }

        @Override
        public String toString() {
            return "{" +
                    "url='" + url + '\'' +
                    ", ip='" + ip + '\'' +
                    ", classMethod='" + classMethod + '\'' +
                    ", args=" + Arrays.toString(args) +
                    '}';
        }
    }
}
