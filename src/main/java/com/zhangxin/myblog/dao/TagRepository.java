package com.zhangxin.myblog.dao;

import com.zhangxin.myblog.po.Tag;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author zhangxin
 * @date 2020/7/26
 */
public interface TagRepository extends JpaRepository<Tag,Long> {
    Tag findByName(String name);
}
