package com.tiny.tinydemo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Silent
 * @date 2019/10/31 13:34:04
 * @description
 */
@Controller
public class FileTransferController {
    @RequestMapping("/test")
    @ResponseBody
    public String test(){
        return "test";
    }
    @PostMapping("/uploadImg")
    @ResponseBody
    public Map<String, Object> uploadImg(@RequestParam("file")MultipartFile file, HttpServletRequest request) {
        Map<String, Object> ret = new HashMap<>(2);
        long max = 1024*1023;
        if(file.getSize() > max){
            return null;
        }
        if (file.isEmpty()) {
            return null;
        }
        String realPath = "E:/serve_resources/images/";
        String fileName = file.getOriginalFilename();
        //加个时间戳，尽量避免文件名称重复
        String realName =  new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()) + "_" + fileName;
        String path = realPath  + realName;
        File dest = new File(path);
        System.out.println("图片路径："+path);
        //判断文件是否已经存在
        if (dest.exists()) {
            return null;
        }
        //判断文件父目录是否存在
        if (!dest.getParentFile().exists()) {
            dest.getParentFile().mkdir();
        }
        try {
            //保存文件
            file.transferTo(dest);
        } catch (IOException e) {
            return null;
        }
        ret.put("location", "/images/" +realName);
        return ret;
    }
    @PostMapping("/result")
    public String result(String content, Model model){
        model.addAttribute("content", content);
        return "result";
    }
}
