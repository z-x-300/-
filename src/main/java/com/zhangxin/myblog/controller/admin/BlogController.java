package com.zhangxin.myblog.controller.admin;

import com.zhangxin.myblog.po.Blog;
import com.zhangxin.myblog.po.User;
import com.zhangxin.myblog.service.BlogService;
import com.zhangxin.myblog.service.TagService;
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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;


/**
 * @author zhangxin
 * @date 2020/7/26
 */
@Controller
@RequestMapping("/admin")
public class BlogController {
    private static final String INPUT="admin/blogs-input";//博客发布
    private static final String LIST="admin/blogs";//博客列表
    private static final String REDIRECT_LIST="redirect:/admin/blogs";//重定向博客列表

    @Autowired
    private BlogService blogService;

    @Autowired
    private TypeService typeService;

    @Autowired
    private TagService tagService;

    //获取博客列表
    @GetMapping("/blogs")
    public String list(@PageableDefault(size = 8,sort = {"updateTime"},direction = Sort.Direction.DESC) Pageable pageable, BlogQuery blogQuery, Model model){

        model.addAttribute("types",typeService.listType());
        model.addAttribute("page",blogService.listBlog(pageable,blogQuery));
        return LIST;
    }

    //查找
    @PostMapping("/blogs/search")
    public String search(@PageableDefault(size = 8,sort = {"updateTime"},direction = Sort.Direction.DESC) Pageable pageable, BlogQuery blogQuery, Model model){

        model.addAttribute("page",blogService.listBlog(pageable,blogQuery));
        return "admin/blogs::blogList";
    }

    //跳转到博客发布页面
    @GetMapping("/blogs/input")
    public String input(Model model){
       setTypeAndTag(model);
        model.addAttribute("blog",new Blog());
        return INPUT;
    }

    //新增/修改博客
    @PostMapping("/blogs")
    public String post(Blog blog, HttpSession session, RedirectAttributes attributes){

        //初始化博客作者
        blog.setUser((User)session.getAttribute("user"));
        //初始化博客分类
        blog.setType(typeService.getType(blog.getType().getId()));
        //初始化博客标签
        blog.setTags(tagService.listTag(blog.getTagIds()));

        Blog blog1;

        if (blog.getId()==null){
            //如果id为空就是添加博客
            blog1 =blogService.saveBlog(blog);
        }else {
            //否则为修改博客
            blog1=blogService.updateBlog(blog.getId(),blog);
        }

        if (blog1 == null) {
            attributes.addFlashAttribute("errorMessage", "提示：操作失败！");
        } else {
            attributes.addFlashAttribute("successMessage", "提示：操作成功！");
        }
        return REDIRECT_LIST;
    }

    //获取分类和标签
    private void setTypeAndTag(Model model){
        model.addAttribute("types",typeService.listType());
        model.addAttribute("tags",tagService.listTag());
    }

    //跳转到编辑页面
    @GetMapping("blogs/{id}/input")
    public String updateInput(@PathVariable Long id, Model model){
        setTypeAndTag(model);
        Blog blog=blogService.getBlog(id);
        blog.init();
        model.addAttribute("blog",blogService.getBlog(id));
        return INPUT;

    }

    //删除博客
    @GetMapping("/blogs/{id}/delete")
    public String delete(@PathVariable Long id,RedirectAttributes attributes){
        blogService.deleteBlog(id);
        attributes.addFlashAttribute("successMessage","提示:删除成功！");
        return REDIRECT_LIST;
    }

}
