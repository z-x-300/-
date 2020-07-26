package com.zhangxin.myblog.controller.admin;

import com.zhangxin.myblog.po.Type;
import com.zhangxin.myblog.service.TypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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


    @ResponseBody
    @PostMapping("/types/input")
    public String editInput(Long id){
       // model.addAttribute("type",typeService.getType(id));
        Type type =typeService.getType(id);
        return type.getName();
    }
    @PostMapping("/types")
    public String post( Type type, RedirectAttributes attributes){

        Type type0 = typeService.getTypeByName(type.getName());
        if (type0!=null) {
            attributes.addFlashAttribute("errorMessage", "提示：操作失败！,添加的分类不能重复！");
        }else {
            Type type1 = typeService.saveType(type);

            if (type1 == null) {
                attributes.addFlashAttribute("errorMessage", "提示：操作失败！");
            } else {
                attributes.addFlashAttribute("successMessage", "提示：操作成功！");
            }
        }
        return "redirect:/admin/types";
    }
}
