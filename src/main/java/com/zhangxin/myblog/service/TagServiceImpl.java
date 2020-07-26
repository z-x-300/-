package com.zhangxin.myblog.service;

import com.zhangxin.myblog.dao.TagRepository;
import com.zhangxin.myblog.handler.NotFoundException;
import com.zhangxin.myblog.po.Tag;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author zhangxin
 * @date 2020/7/26
 */
@Service
public class TagServiceImpl implements TagService {

    @Autowired
    private TagRepository tagRepository;


    //保存标签
    @Transactional
    @Override
    public Tag saveTag(Tag tag) {
        return tagRepository.save(tag);
    }


    //根据id获取标签
    @Transactional
    @Override
    public Tag getTag(Long id) {
        return tagRepository.getOne(id);
    }


    //根据名称获取标签
    @Transactional
    @Override
    public Tag getTagByName(String name) {
        return tagRepository.findByName(name);
    }


    //获取所有标签
    @Transactional
    @Override
    public Page<Tag> listTag(Pageable pageable) {
        return tagRepository.findAll(pageable);
    }


    //更新标签
    @Transactional
    @Override
    public Tag updateTag(Long id, Tag tag) {
        Tag tag1 = tagRepository.getOne(id);
        if (tag1==null){
            throw new NotFoundException("该标签不存在！");
        }
        BeanUtils.copyProperties(tag,tag1);
        return tagRepository.save(tag1);
    }


    //删除标签
    @Transactional
    @Override
    public void deleteTag(Long id) {
       tagRepository.deleteById(id);
    }
}
