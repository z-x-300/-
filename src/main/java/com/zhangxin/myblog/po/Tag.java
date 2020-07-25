package com.zhangxin.myblog.po;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author zhangxin
 * @date 2020/7/25
 */
//标签实体类
@Entity
@Table(name = "tag")
public class Tag {

    @Id
    @GeneratedValue
    private Long id;//标签id

    private String name;//标签名称

    @ManyToMany(mappedBy = "tags")//被维护端
    private List<Blog> blogs =new ArrayList<>();

    public Tag() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Blog> getBlogs() {
        return blogs;
    }

    public void setBlogs(List<Blog> blogs) {
        this.blogs = blogs;
    }

    @Override
    public String toString() {
        return "Tag{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
