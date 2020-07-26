package com.zhangxin.myblog.po;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.ArrayList;
import java.util.List;

/**
 * @author zhangxin
 * @date 2020/7/25
 */
//分类实体类

@Entity
@Table(name = "type")
public class Type {

    @Id
    @GeneratedValue
    private Long id;//类别id

    @NotBlank(message = "分类名称不能为空")
    private String name;//类别名称

    @OneToMany(mappedBy="type")//被维护端
    private List<Blog> blogs = new ArrayList<>();//分类对应的博客

    public Type() {
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
        return "Type{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
