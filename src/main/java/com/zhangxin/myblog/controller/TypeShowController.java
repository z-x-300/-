package com.zhangxin.myblog.controller;

import com.zhangxin.myblog.po.Blog;
import com.zhangxin.myblog.po.Type;
import com.zhangxin.myblog.service.BlogService;
import com.zhangxin.myblog.service.TypeService;
import com.zhangxin.myblog.vo.BlogQuery;
import org.springframework.beans.factory.annotation.Autowired;
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
public class TypeShowController {

    @Autowired
    private TypeService typeService;

    @Autowired
    private BlogService blogService;

    @GetMapping("/types/{id}")
    public String types(@PageableDefault(size = 4,sort = {"updateTime"},direction = Sort.Direction.DESC) Pageable pageable,
                       @PathVariable Long id, Model model){

        List<Type> types = typeService.listTypeTop(1000);
        if (id==-1){//点击菜单分类跳转过来的
            id =types.get(0).getId();
        }
        Type type = new Type();
        type.setId(id);

        model.addAttribute("types",types);
        model.addAttribute("page",blogService.listBlog(type,pageable));
        model.addAttribute("activeTypeId",id);
        return "types";
    }
}
