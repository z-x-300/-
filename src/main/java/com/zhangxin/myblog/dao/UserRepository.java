package com.zhangxin.myblog.dao;

import com.zhangxin.myblog.po.User;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author zhangxin
 * @date 2020/7/25
 */
public interface UserRepository extends JpaRepository<User,Long> {

    //确认用户（登录使用）
    User findByUsernameAndPassword(String username,String password);

    //根据username查询用户
    User findByUsername(String username);
}
