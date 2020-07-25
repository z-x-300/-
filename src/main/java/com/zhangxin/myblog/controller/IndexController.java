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

    @GetMapping("/{id}/{name}")
    public String index(@PathVariable("id") Integer id,@PathVariable("name") String name){
//        //int i =9/0;
//
//        String blog =null;
//        if(blog==null){
//            throw  new NotFoundException("博客不存在！");
//        }

        System.out.println("---------index-------------");
        return "index";
    }

}
