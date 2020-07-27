package com.zhangxin.myblog.service;

import com.zhangxin.myblog.po.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * @author zhangxin
 * @date 2020/7/26
 */
public interface TagService {

    //保存标签
    Tag saveTag(Tag tag);

    //根据id查询标签
    Tag getTag(Long id);

    //根据名称查询标签
    Tag getTagByName(String name);

    //查询所有标签
    Page<Tag> listTag(Pageable pageable);

    //查询所有标签（不分页）
    List<Tag> listTag();

    //根据一系列id获取标签
    List<Tag> listTag(String ids);

    //修改标签
    Tag updateTag(Long id,Tag tag);

    //删除标签
    void deleteTag(Long id);
}
