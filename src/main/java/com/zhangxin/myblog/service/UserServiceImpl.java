package com.zhangxin.myblog.service;

import com.zhangxin.myblog.dao.UserRepository;
import com.zhangxin.myblog.po.User;
import com.zhangxin.myblog.util.MD5Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author zhangxin
 * @date 2020/7/25
 */

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    UserRepository userRepository;

    //确认用户（登录使用）
    @Override
    public User checkUser(String username, String password) {
        User user =userRepository.findByUsernameAndPassword(username, MD5Utils.code(password));
        return user;
    }

    //根据username查询用户（判断用户名是否重复）
    @Override
    public User findUserByUserName(String username) {
        return userRepository.findByUsername(username);
    }
    //保存用户（注册使用）
    @Override
    public User saveUser(User user) {
        return userRepository.save(user);
    }
}
