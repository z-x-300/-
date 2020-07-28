package com.zhangxin.myblog.controller;

import com.zhangxin.myblog.service.BlogService;
import com.zhangxin.myblog.service.TagService;
import com.zhangxin.myblog.service.TypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;


/**
 * @author zhangxin
 * @date 2020/7/25
 */

@Controller
public class IndexController {

    @Autowired
    private BlogService blogService;

    @Autowired
    private TypeService typeService;

    @Autowired
    private TagService tagService;

    //获取博客列表
    @GetMapping("/")
    public String index(@PageableDefault(size = 5,sort = {"updateTime"},direction = Sort.Direction.DESC)Pageable pageable,
                        Model model){

        //获取博客列表
        model.addAttribute("page",blogService.listPublishedBlog(pageable));
        //获取分类列表
        model.addAttribute("types",typeService.listTypeTop(6));
        //获取标签列表
        model.addAttribute("tags",tagService.listTagTop(10));
        //获取最新的推荐博客
        model.addAttribute("recommendBlogs",blogService.listRecommendBlogTop(8));
        return "index";
    }

    //搜索
    @GetMapping("/search")
    public String search(@PageableDefault(size = 4,sort = {"updateTime"},direction = Sort.Direction.DESC)Pageable pageable,
                         String query , Model model){
        model.addAttribute("page",blogService.listBlog("%"+query+"%",pageable));
        model.addAttribute("query",query);
        return "search";
    }

    //跳转到博客详情页面
    @GetMapping("/blog/{id}")
    public String blog(@PathVariable Long id,Model model){
        model.addAttribute("blog",blogService.getAndConvert(id));

        return "details";
    }
}
