package com.zhangxin.myblog.vo;

/**
 * @author zhangxin
 * @date 2020/7/26
 */
//博客搜索类
public class BlogQuery {
    private String title;//博客标题

    private Long typeId;//博客分类id

    private boolean recommend;//博客是否打赏

    public BlogQuery() {
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Long getTypeId() {
        return typeId;
    }

    public void setTypeId(Long typeId) {
        this.typeId = typeId;
    }

    public boolean isRecommend() {
        return recommend;
    }

    public void setRecommend(boolean recommend) {
        this.recommend = recommend;
    }
}
