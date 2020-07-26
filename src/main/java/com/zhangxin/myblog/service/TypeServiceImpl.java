package com.zhangxin.myblog.service;

import com.zhangxin.myblog.dao.TypeRepository;
import com.zhangxin.myblog.handler.NotFoundException;
import com.zhangxin.myblog.po.Type;
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
    @Transactional
    @Override
    public Type getType(Long id) {
        return typeRepository.getOne(id);
    }

    //查询所有分类
    @Transactional
    @Override
    public Page<Type> listType(Pageable pageable) {
        return typeRepository.findAll(pageable);
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
