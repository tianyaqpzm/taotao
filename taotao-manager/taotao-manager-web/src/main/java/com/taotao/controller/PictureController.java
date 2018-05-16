package com.taotao.controller;

import com.taotao.common.utils.JsonUtils;
import com.taotao.service.PictureService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@Controller
public class PictureController {
    @Autowired
    private PictureService pictureService;

/*    @RequestMapping("/pic/upload")
    @ResponseBody //  SpringMVC默认行为 将对象转为json字符串
    public Map pictureUpload(MultipartFile uploadFile) {
        Map result = pictureService.uploadPicture(uploadFile);
        return result;  // Content-Type: application/json;charset=UTF-8
    }
    */

    @RequestMapping("/pic/upload")
    @ResponseBody   //直接响应字符串   Content-Type: text/plain;charset=ISO-8859-1
    public String pictureUpload(MultipartFile uploadFile) {
        Map result = pictureService.uploadPicture(uploadFile);
        String json = JsonUtils.objectToJson(result);
        return json;
    }

}
