package com.zhangxin.myblog.dao;

import com.zhangxin.myblog.po.Blog;
import com.zhangxin.myblog.po.Type;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author zhangxin
 * @date 2020/7/26
 */
public interface BlogRepository extends JpaRepository<Blog,Long> , JpaSpecificationExecutor<Blog> {

    //获取具体数量的推荐博客
    @Query("select b from Blog b where b.recommend =true and b.published=true ")
    List<Blog> findTop(Pageable pageable);

    //获取发布的博客列表
    @Query("select b from Blog b where b.published= true ")
    Page<Blog>  findAllPublished(Pageable pageable);

    //搜索博客
    @Query("select b from Blog b where (b.title like ?1 or b.content like ?1) and b.published=true ")
    Page<Blog> findByQuery(String query,Pageable pageable);

    //更新博客浏览数量
    @Transactional
    @Modifying
    @Query("update Blog b set b.views =b.views+1 where  b.id=?1")
    void updateViews(Long id);

    //根据分类获取博客列表
    @Query("select b from Blog b where b.type=?1 and b.published=true ")
    Page<Blog> findAllByType(Type type,Pageable pageable);

    //获取博客发表日期列表
    @Query("select function('date_format',b.updateTime,'%Y') as year from Blog b group by function('date_format',b.updateTime,'%Y') order by year desc ")
    List<String> findGroupYear();

    //根据日期获取博客列表
    @Query("select b from Blog b where function('date_format',b.updateTime,'%Y') = ?1")
    List<Blog> findByYear(String year);

    //获取博客总数
    @Query("select count(*) from Blog b where b.published=true ")
    Long BlogCount();

    //获取浏览总数
    @Query("select sum (b .views) from Blog b where b.published=true ")
    Long viewCount();
}
