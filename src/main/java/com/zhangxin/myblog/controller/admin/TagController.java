package com.zhangxin.myblog.controller.admin;

import com.zhangxin.myblog.po.Tag;
import com.zhangxin.myblog.service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
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
}
