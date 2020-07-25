package com.zhangxin.myblog.service;

import com.zhangxin.myblog.po.User;

/**
 * @author zhangxin
 * @date 2020/7/25
 */
public interface UserService {

    //确认用户（登录使用）
     User checkUser(String username,String password);
}
