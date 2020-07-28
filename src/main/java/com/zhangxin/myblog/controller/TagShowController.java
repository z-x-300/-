package com.zhangxin.myblog.controller;

import com.zhangxin.myblog.po.Blog;
import com.zhangxin.myblog.po.Tag;
import com.zhangxin.myblog.service.BlogService;
import com.zhangxin.myblog.service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

/**
 * @author zhangxin
 * @date 2020/7/28
 */

@Controller
public class TagShowController {

    @Autowired
    private TagService tagService;

    @Autowired
    private BlogService blogService;

    @GetMapping("/tags/{id}")
    public String tags(@PageableDefault(size = 1,sort = {"updateTime"},direction = Sort.Direction.DESC) Pageable pageable,
                       @PathVariable Long id, Model model){

        List<Tag> tags =tagService.listTagTop(1000);
        if (id==-1){
            id=tags.get(0).getId();
        }

        model.addAttribute("tags",tags);
        model.addAttribute("page",blogService.listBlog(id,pageable));
        model.addAttribute("activeTagId",id);

        return "tags";
    }
}
