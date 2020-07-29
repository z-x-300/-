package com.zhangxin.myblog.dao;

import com.zhangxin.myblog.po.Type;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * @author zhangxin
 * @date 2020/7/26
 */
public interface TypeRepository extends JpaRepository<Type,Long> {

    //根据名称获取分类
    Type findByName(String name);

    //获取具体数量的分类
    @Query("select t from Type t ")
    List<Type> findTop();
}
