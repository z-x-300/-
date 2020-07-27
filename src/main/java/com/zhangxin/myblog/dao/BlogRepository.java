package com.zhangxin.myblog.dao;

import com.zhangxin.myblog.po.Blog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * @author zhangxin
 * @date 2020/7/26
 */
public interface BlogRepository extends JpaRepository<Blog,Long> , JpaSpecificationExecutor<Blog> {

    //获取具体数量的推荐博客
    @Query("select b from Blog b where b.recommend =true")
    List<Blog> findTop(Pageable pageable);

    //获取发布的博客列表
    @Query("select b from Blog b where b.published= true ")
    Page<Blog>  findAllPublished(Pageable pageable);

    //搜索博客
    @Query("select b from Blog b where b.title like ?1 or b.content like ?1")
    Page<Blog> findByQuery(String query,Pageable pageable);
}
