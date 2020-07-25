package com.zhangxin.myblog.service;

import com.zhangxin.myblog.dao.UserRepository;
import com.zhangxin.myblog.po.User;
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
        User user =userRepository.findByUsernameAndPassword(username,password);
        return user;
    }
}
