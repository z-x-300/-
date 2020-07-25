package com.zhangxin.myblog.controller;

import com.zhangxin.myblog.handler.NotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * @author zhangxin
 * @date 2020/7/25
 */

@Controller
public class IndexController {

    @GetMapping("/")
    public String index(){
       //int i =9/0;
//
//        String blog =null;
//        if(blog==null){
//            throw  new NotFoundException("博客不存在！");
//        }

        System.out.println("---------index-------------");
        return "index";
    }

    @GetMapping("/details")
    public String details(){
        return "details";
    }

    @GetMapping("/types")
    public String types(){
        return "types";
    }

    @GetMapping("/tags")
    public String tags(){
        return "tags";
    }

    @GetMapping("/archives")
    public String archives(){
        return "archives";
    }
    @GetMapping("/about")
    public String about(){
        return "about";
    }

    @GetMapping("/blogs")
    public String blogs(){
        return "admin/blogs";
    }

}
