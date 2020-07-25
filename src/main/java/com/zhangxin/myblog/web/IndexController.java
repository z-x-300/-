package com.zhangxin.myblog.web;

import com.zhangxin.myblog.handler.NotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * @author zhangxin
 * @date 2020/7/25
 */

@Controller
public class IndexController {

    @GetMapping("/")
    public String index(){
        //int i =9/0;

        String blog =null;
        if(blog==null){
            throw  new NotFoundException("博客不存在！");
        }
        return "index";
    }

}
