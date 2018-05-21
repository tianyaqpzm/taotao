package com.taotao.rest.controller;

import com.taotao.common.utils.JsonUtils;
import com.taotao.rest.pojo.CartResult;
import com.taotao.rest.service.ItemCatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.awt.*;
import java.lang.annotation.Retention;

@Controller
public class ItemCatController {
    @Autowired
    private ItemCatService itemCatService;

    /*// produce 防止出现乱码  方法1
        @RequestMapping(value = "itemcat/list", produces = MediaType.APPLICATION_JSON_VALUE+";charset=utf-8")
        @ResponseBody
        public String getItemZCatList(String callback){
            CartResult catResult = itemCatService.getItemCatList();
            // 将pojo 转为字符串 + callback
            String json = JsonUtils.objectToJson(catResult);
            String  result = callback+"("+json+");";
            return result;
        }*/
// object 自动会转化json 防止出现乱码
    @RequestMapping(value = "itemcat/list")
    @ResponseBody
    public Object getItemZCatList(String callback) {
        CartResult catResult = itemCatService.getItemCatList();
        MappingJacksonValue mappingJacksonValue = new MappingJacksonValue(catResult);
        // 将pojo 转为字符串 + callback
        mappingJacksonValue.setJsonpFunction(callback);
        return mappingJacksonValue;
    }
}
