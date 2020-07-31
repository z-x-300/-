package com.zhangxin.myblog.dao;

import com.zhangxin.myblog.po.Blog;
import com.zhangxin.myblog.po.Comment;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author zhangxin
 * @date 2020/7/28
 */
public interface CommentRepository extends JpaRepository<Comment,Long> {

    //根据博客id获取父级评论列表
    List<Comment> findByBlogIdAndParentCommentNull(Long blogId, Sort sort);

    //根据博客id删除评论
    @Transactional
    @Modifying
    @Query(value ="delete from Comment  where blog_id=?1",nativeQuery = true)
    void deleteByBlogId(Long blogId);

}
