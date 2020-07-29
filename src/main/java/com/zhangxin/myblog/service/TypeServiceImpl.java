package com.zhangxin.myblog.service;

import com.zhangxin.myblog.dao.TypeRepository;
import com.zhangxin.myblog.handler.NotFoundException;
import com.zhangxin.myblog.po.Type;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author zhangxin
 * @date 2020/7/26
 */
@Service
public class TypeServiceImpl implements TypeService {

    @Autowired
    private TypeRepository typeRepository;

    //保存分类
    @Transactional
    @Override
    public Type saveType(Type type) {
        return typeRepository.save(type);
    }

    //根据id查询分类
    @Override
    public Type getType(Long id) {
        return typeRepository.getOne(id);
    }

    //根据名称查询分类
    @Override
    public Type getTypeByName(String name) {
        return typeRepository.findByName(name);
    }

    //查询所有分类
    @Override
    public Page<Type> listType(Pageable pageable) {
        return typeRepository.findAll(pageable);
    }


    //查询所有分类(不分页)
    @Override
    public List<Type> listType() {
        return typeRepository.findAll();
    }

    //获取具体数量的分类
    @Override
    public List<Type> listTypeTop(Integer size) {
      //  Sort sort =new Sort()
        //Pageable pageable =PageRequest.of(0,size,Sort.by(Sort.Direction.DESC,"blogs.size"));
        List<Type> types =typeRepository.findTop();
        List<Type> typeList =new ArrayList<>();
        int size1 =types.size();
        for (int i=0;i<size1;i++){
            for (int j=0;j<types.get(i).getBlogs().size();j++){
                if (!types.get(i).getBlogs().get(j).isPublished()){
                    types.get(i).getBlogs().remove(types.get(i).getBlogs().get(j));
                }
            }
        }
        Collections.sort(types);
        if (size>size1){
            size=size1;
        }
        for (int i=0;i<size;i++){
            typeList.add(types.get(i));
        }
        return typeList;
    }

    //修改分类
    @Transactional
    @Override
    public Type updateType(Long id, Type type) {
        Type type1 = typeRepository.getOne(id);
        if (type1 == null) {
            throw new NotFoundException("该分类不存在！");
        }
        BeanUtils.copyProperties(type, type1);
        return typeRepository.save(type1);
    }

    //删除分类
    @Transactional
    @Override
    public void deleteType(Long id) {
        typeRepository.deleteById(id);
    }
}
