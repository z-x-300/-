package com.zhangxin.myblog.dao;

import com.zhangxin.myblog.po.Type;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author zhangxin
 * @date 2020/7/26
 */
public interface TypeRepository extends JpaRepository<Type,Long> {

    Type findByName(String name);
}
