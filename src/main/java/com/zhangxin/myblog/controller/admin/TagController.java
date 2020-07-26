package com.zhangxin.myblog.controller.admin;

import com.zhangxin.myblog.po.Tag;
import com.zhangxin.myblog.po.Type;
import com.zhangxin.myblog.service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * @author zhangxin
 * @date 2020/7/26
 */

@Controller
@RequestMapping("/admin")
public class TagController {

    @Autowired
    private TagService tagService;

    //获取标签列表
    @GetMapping("/tags")
    public String list(@PageableDefault(size = 8,sort = {"id"},direction = Sort.Direction.DESC) Pageable pageable,
                                Model model){
        model.addAttribute("page", tagService.listTag(pageable));
        return "/admin/tags";
    }

    //添加标签
    @PostMapping("/tags")
    public String add(Tag tag, RedirectAttributes attributes){

        Tag type0 = tagService.getTagByName(tag.getName());
        if (type0!=null) {
            attributes.addFlashAttribute("errorMessage", "提示：操作失败！,标签不能重复！");
        }else {
            Tag tag1 = tagService.saveTag(tag);

            if (tag1 == null) {
                attributes.addFlashAttribute("errorMessage", "提示：操作失败！");
            } else {
                attributes.addFlashAttribute("successMessage", "提示：操作成功！");
            }
        }
        return "redirect:/admin/tags";
    }


    //获取修改分类的信息
    @ResponseBody
    @PostMapping("/tags/input")
    public String updateInput(Long id){
        Tag tag =tagService.getTag(id);
        return tag.getId()+"|"+tag.getName();
    }

    //修改标签
    @PutMapping("/tags")
    public String  update(Tag tag, RedirectAttributes attributes){

        Tag type0 = tagService.getTagByName(tag.getName());
        if (type0!=null) {
            attributes.addFlashAttribute("errorMessage", "提示：操作失败！,标签不能重复！");
        }else {
            Tag tag1 = tagService.updateTag(tag.getId(),tag);

            if (tag1 == null) {
                attributes.addFlashAttribute("errorMessage", "提示：操作失败！");
            } else {
                attributes.addFlashAttribute("successMessage", "提示：操作成功！");
            }
        }
        return "redirect:/admin/tags";
    }

    //删除分类
    @GetMapping("/tags/{id}/delete")
    public String delete(@PathVariable Long id ,RedirectAttributes attributes){
        tagService.deleteTag(id);
        attributes.addFlashAttribute("successMessage", "提示：删除成功！");
        return "redirect:/admin/tags";
    }
}
