package com.taotao.portal.controller;

import com.taotao.common.pojo.TaotaoResult;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.awt.*;

@Controller
public class IndexController {

    @RequestMapping("/index")
    public String showIndex(){
        return "index";
    }

    @RequestMapping("/httpclient/post")
    @ResponseBody
    public TaotaoResult testPost(){
        return TaotaoResult.ok();
    }

    @RequestMapping(value = "/httpclient/post2",method = RequestMethod.POST, produces = MediaType.TEXT_PLAIN_VALUE+";charset=utf-8")
    @ResponseBody
    public String testPostWithParams(String username, String password){
        return "Username:"+username+", Paswsword:"+password;

    }


/*    @RequestMapping(value = "/httpclient/post2")
    @ResponseBody
    public TaotaoResult testPostWithParams(String username, String password){
        String result =  "Username:"+username+" \t Paswsword:"+password;
        System.out.println(result);
        return TaotaoResult.ok();


    }*/


}
