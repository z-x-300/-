package com.zhangxin.myblog.po;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author zhangxin
 * @date 2020/7/25
 */
//评论实体类
@Entity
@Table(name = "comment")
public class Comment {

    @Id
    @GeneratedValue
    private Long id;//评论id

    private String nickname;//评论者昵称

    private String email;//评论者邮箱

    private String content;//评论内容

    private String avatar;//头像地址

    @Temporal(TemporalType.TIMESTAMP)
    private Date createTime;//创建时间

    @ManyToOne
    private Blog blog;//评论所属博客

    @OneToMany(mappedBy = "parentComment")
    private List<Comment> replyComments =new ArrayList<>();//回复评论

    @ManyToOne
    private Comment parentComment;//被回复评论

    private boolean adminComment;//是否作者评论

    public Comment() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Blog getBlog() {
        return blog;
    }

    public void setBlog(Blog blog) {
        this.blog = blog;
    }

    public List<Comment> getReplyComments() {
        return replyComments;
    }

    public void setReplyComments(List<Comment> replyComments) {
        this.replyComments = replyComments;
    }

    public Comment getParentComment() {
        return parentComment;
    }

    public void setParentComment(Comment parentComment) {
        this.parentComment = parentComment;
    }

    public boolean isAdminComment() {
        return adminComment;
    }

    public void setAdminComment(boolean adminComment) {
        this.adminComment = adminComment;
    }

    @Override
    public String toString() {
        return "Comment{" +
                "id=" + id +
                ", nickname='" + nickname + '\'' +
                ", email='" + email + '\'' +
                ", content='" + content + '\'' +
                ", avatar='" + avatar + '\'' +
                ", createTime=" + createTime +
                ", blog=" + blog +
                ", replyComments=" + replyComments +
                ", parentComment=" + parentComment +
                ", adminComment=" + adminComment +
                '}';
    }
}
