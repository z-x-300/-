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

import java.util.ArrayList;
import java.util.List;

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
    @Override
    public Tag getTag(Long id) {
        return tagRepository.getOne(id);
    }


    //根据名称获取标签
    @Override
    public Tag getTagByName(String name) {
        return tagRepository.findByName(name);
    }


    //获取所有标签
    @Override
    public Page<Tag> listTag(Pageable pageable) {
        return tagRepository.findAll(pageable);
    }

    //获取所有标签(不分页）
    @Override
    public List<Tag> listTag() {
        return tagRepository.findAll();
    }

    //根据一系列id获取标签
    @Override
    public List<Tag> listTag(String ids) {
        return tagRepository.findAllById(convertToList(ids));
    }

    //把字符串转换为list<Long>
    private  List<Long> convertToList(String ids){
        List<Long> list  =new ArrayList<>();
        if (!"".equals(ids)&&ids!=null){
            String [] idarray =ids.split(",");
            for (int i=0;i<idarray.length;i++){
                list.add(new Long(idarray[i]));
            }
        }

        return list;
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
