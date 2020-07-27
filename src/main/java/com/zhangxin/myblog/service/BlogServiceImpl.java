package com.zhangxin.myblog.service;

import com.sun.org.apache.bcel.internal.generic.IF_ACMPEQ;
import com.zhangxin.myblog.dao.BlogRepository;
import com.zhangxin.myblog.handler.NotFoundException;
import com.zhangxin.myblog.po.Blog;
import com.zhangxin.myblog.po.Type;
import com.zhangxin.myblog.util.MyBeanUtils;
import com.zhangxin.myblog.vo.BlogQuery;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author zhangxin
 * @date 2020/7/26
 */
@Service
public class BlogServiceImpl implements BlogService {

    @Autowired
    private BlogRepository blogRepository;

    //根据id获取博客
    @Override
    public Blog getBlog(Long id) {
        return blogRepository.getOne(id);
    }

    //动态获取博客列表
    @Override
    public Page<Blog> listBlog(Pageable pageable, BlogQuery blogQuery) {
        return blogRepository.findAll(new Specification<Blog>() {
            @Override
            public Predicate toPredicate(Root<Blog> root, CriteriaQuery<?> cq, CriteriaBuilder cb) {
                List<Predicate> predicates = new ArrayList<>();
                if (!"".equals(blogQuery.getTitle()) && blogQuery.getTitle() != null) {
                    predicates.add(cb.like(root.<String>get("title"), "%"+blogQuery.getTitle()+"%"));
                }
                if (blogQuery.getTypeId() != null) {
                    predicates.add(cb.equal(root.<Type>get("type").get("id"), blogQuery.getTypeId()));
                }
                if (blogQuery.isRecommend()) {
                    predicates.add(cb.equal(root.<Boolean>get("recommend"), blogQuery.isRecommend()));
                }
                cq.where(predicates.toArray(new Predicate[predicates.size()]));
                return null;
            }
        },pageable);
    }


    //添加博客
    @Transactional
    @Override
    public Blog saveBlog(Blog blog) {
        //初始化创建日期
        blog.setCreateTime(new Date());
        //初始化修改日期
        blog.setUpdateTime(new Date());
        //初始化浏览次数
        blog.setViews(0);
        return blogRepository.save(blog);
    }

    //修改博客
    @Transactional
    @Override
    public Blog updateBlog(Long id, Blog blog) {
        Blog blog1 =blogRepository.getOne(id);
        if (blog1==null){
            throw new NotFoundException("该博客不存在！");
        }
        BeanUtils.copyProperties(blog,blog1, MyBeanUtils.getNullPropertyNames(blog));
        blog1.setUpdateTime(new Date());
        return blogRepository.save(blog1);
    }

    //删除博客
    @Transactional
    @Override
    public void deleteBlog(Long id) {
        blogRepository.deleteById(id);
    }
}
