package com.zhangxin.myblog.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * @author zhangxin
 * @date 2020/7/31
 */
@Controller
public class UploadController {

    @Value("${blog.upload.image.url}")
    private String uploadImageUrl;

    //Markdown编辑器上传图片
    @ResponseBody
    @PostMapping(value = "/uploadfile")
    public Map<String,Object> markdownImageUpload(@RequestParam(value = "editormd-image-file", required = false) MultipartFile file, HttpServletRequest request) {
        Map<String,Object> resultMap = new HashMap<String,Object>();
        String fileName=System.currentTimeMillis()+file.getOriginalFilename();
        //保存
        try {

            File imageFolder= new File(request.getServletContext().getRealPath("/upload/images"));
            File targetFile = new File(imageFolder,fileName);
            if(!targetFile.getParentFile().exists())
                targetFile.getParentFile().mkdirs();
            file.transferTo(targetFile);
            resultMap.put("success", 1);
            resultMap.put("message", "上传成功！");
            resultMap.put("url",uploadImageUrl+fileName);
        } catch (Exception e) {
            resultMap.put("success", 0);
            resultMap.put("message", "上传失败！");
            e.printStackTrace();
        }
        System.out.println(resultMap.get("success"));
        return resultMap;
    }
    //图片上传
    @ResponseBody
    @PostMapping("/upload")
    public String upload(@RequestParam(value = "file", required = false) MultipartFile file,HttpServletRequest request){
        String fileName=System.currentTimeMillis()+file.getOriginalFilename();
        try {
            File imageFolder= new File(request.getServletContext().getRealPath("/upload/images"));
            File targetFile = new File(imageFolder,fileName);
            if(!targetFile.getParentFile().exists())
                targetFile.getParentFile().mkdirs();
            file.transferTo(targetFile);
        }catch (Exception e) {
            e.printStackTrace();
        }

        return uploadImageUrl+fileName;

    }
}
