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
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

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

   //获取修改分类的信息
    @ResponseBody
    @PostMapping("/types/input")
    public String updateInput(Long id){
       // model.addAttribute("type",typeService.getType(id));
        Type type =typeService.getType(id);
        return type.getId()+"|"+type.getName();
    }

    //添加分类
    @PostMapping("/types")
    public String add( Type type, RedirectAttributes attributes){

        Type type0 = typeService.getTypeByName(type.getName());
        if (type0!=null) {
            attributes.addFlashAttribute("errorMessage", "提示：操作失败！,分类不能重复！");
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

    //修改分类
    @PutMapping("/types")
    public String  update( Type type, RedirectAttributes attributes){

        Type type0 = typeService.getTypeByName(type.getName());
        if (type0!=null) {
            attributes.addFlashAttribute("errorMessage", "提示：操作失败！,分类不能重复！");
        }else {
            Type type1 = typeService.updateType(type.getId(),type);

            if (type1 == null) {
                attributes.addFlashAttribute("errorMessage", "提示：操作失败！");
            } else {
                attributes.addFlashAttribute("successMessage", "提示：操作成功！");
            }
        }
        return "redirect:/admin/types";
    }

    //删除分类
    @GetMapping("/types/{id}/delete")
    public String delete(@PathVariable Long id ,RedirectAttributes attributes){
        typeService.deleteType(id);
        attributes.addFlashAttribute("successMessage", "提示：删除成功！");
        return "redirect:/admin/types";
    }
}
