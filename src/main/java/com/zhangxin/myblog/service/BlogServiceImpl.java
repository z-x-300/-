package com.zhangxin.myblog.service;

import com.sun.org.apache.bcel.internal.generic.IF_ACMPEQ;
import com.zhangxin.myblog.dao.BlogRepository;
import com.zhangxin.myblog.handler.NotFoundException;
import com.zhangxin.myblog.po.Blog;
import com.zhangxin.myblog.po.Type;
import com.zhangxin.myblog.util.MarkdownUtils;
import com.zhangxin.myblog.util.MyBeanUtils;
import com.zhangxin.myblog.vo.BlogQuery;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.*;
import java.util.*;

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

    //根据id获取博客并转换格式
    @Override
    public Blog getAndConvert(Long id) {
        Blog blog =blogRepository.getOne(id);
        if (blog == null) {
            throw new NotFoundException("该博客不存在");
        }
        blogRepository.updateViews(id);
        Blog b = new Blog();
        BeanUtils.copyProperties(blog,b);
        String content = b.getContent();
        b.setContent(MarkdownUtils.markdownToHtmlExtensions(content));
        return b;
    }

    //动态获取博客列表
    @Override
    public Page<Blog> listBlog(Pageable pageable, BlogQuery blogQuery) {
        return blogRepository.findAll(new Specification<Blog>() {
            @Override
            public Predicate toPredicate(Root<Blog> root, CriteriaQuery<?> cq, CriteriaBuilder cb) {
                List<Predicate> predicates = new ArrayList<>();
                if (!"".equals(blogQuery.getTitle()) && blogQuery.getTitle() != null) {
                    predicates.add(cb.like(root.<String>get("title"), "%" + blogQuery.getTitle() + "%"));
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
        }, pageable);
    }

    //获取博客列表
    @Override
    public Page<Blog> listBlog(Pageable pageable) {
        return blogRepository.findAll(pageable);
    }

    //获取发布博客列表
    @Override
    public Page<Blog> listPublishedBlog(Pageable pageable) {
        return blogRepository.findAllPublished(pageable);
    }

    //获取具体数量的推荐博客
    @Override
    public List<Blog> listRecommendBlogTop(Integer size) {
        Pageable pageable = PageRequest.of(0, size, Sort.by(Sort.Direction.DESC, "updateTime"));
        return blogRepository.findTop(pageable);
    }

    //搜索博客
    @Override
    public Page<Blog> listBlog(String query, Pageable pageable) {
        return blogRepository.findByQuery(query, pageable);
    }

    //根据分类获取博客列表
    @Override
    public Page<Blog> listBlog(Type type,Pageable pageable) {
        return blogRepository.findAllByType(type,pageable);
    }

    //根据标签获取博客列表
    @Override
    public Page<Blog> listBlog(Long tagId, Pageable pageable) {
        return blogRepository.findAll(new Specification<Blog>() {
            @Override
            public Predicate toPredicate(Root<Blog> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                Join join =root.join("tags");
                List<Predicate> predicates = new ArrayList<>();
                predicates.add(criteriaBuilder.equal(join.get("id"),tagId));
                predicates.add(criteriaBuilder.equal(root.<Boolean>get("published"),true));
                criteriaQuery.where(predicates.toArray(new Predicate[predicates.size()]));
                //return criteriaBuilder.equal(join.get("id"),tagId);
                return null;
            }
        },pageable);
    }

    //归档博客
    @Override
    public Map<String, List<Blog>> archiveBlog() {
        List<String> years =blogRepository.findGroupYear();
        Map<String,List<Blog>> map =new HashMap<>();
        for (String year: years){
             map.put(year,blogRepository.findByYear(year));
        }
        return map;
    }

    @Override
    public Long countBlog() {
        return blogRepository.BlogCount();
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
        Blog blog1 = blogRepository.getOne(id);
        if (blog1 == null) {
            throw new NotFoundException("该博客不存在！");
        }
        BeanUtils.copyProperties(blog, blog1, MyBeanUtils.getNullPropertyNames(blog));
        blog1.setUpdateTime(new Date());
        return blogRepository.save(blog1);
    }

    //删除博客
    @Transactional
    @Override
    public void deleteBlog(Long id) {
        blogRepository.deleteById(id);
    }

    //获取博客浏览总数
    @Override
    public Long blogViewCount() {
        return blogRepository.viewCount();
    }
}
