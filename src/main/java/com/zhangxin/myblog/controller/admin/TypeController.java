package com.zhangxin.myblog.controller.admin;

import com.zhangxin.myblog.po.Type;
import com.zhangxin.myblog.service.TypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author zhangxin
 * @date 2020/7/26
 */

@Controller
@RequestMapping("/admin")
public class TypeController {

    @Autowired
    private TypeService typeService;

    //获取分类列表
    @GetMapping("/types")
    public String list(@PageableDefault(size = 8,sort = {"id"},direction = Sort.Direction.DESC) Pageable pageable,
                       Model model){
        model.addAttribute("page", typeService.listType(pageable));
        return "/admin/types";
    }

//    @GetMapping("/types/input")
//    public String input(){
//        return
//    }

    @PostMapping("/types")
    public String post(Type type){

        Type type1 = typeService.saveType(type);

        if (type1==null){

        }else {

        }

        return "redirect:/admin/types";
    }
}
