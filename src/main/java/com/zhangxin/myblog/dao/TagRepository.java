package com.zhangxin.myblog.dao;

import com.zhangxin.myblog.po.Tag;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * @author zhangxin
 * @date 2020/7/26
 */
public interface TagRepository extends JpaRepository<Tag,Long> {

    //根据名称查找标签
    Tag findByName(String name);

    //获取具体数量的标签
    @Query("select t from Tag t")
    List<Tag> findTop();
}
