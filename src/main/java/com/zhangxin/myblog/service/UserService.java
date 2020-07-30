package com.zhangxin.myblog.service;

import com.zhangxin.myblog.po.User;

/**
 * @author zhangxin
 * @date 2020/7/25
 */
public interface UserService {

    //确认用户（登录使用）
     User checkUser(String username,String password);

     //根据username查询用户（判断用户名是否重复）
    User findUserByUserName(String username);

    //保存用户（注册使用）
    User saveUser(User user);

}
