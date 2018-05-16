package com.taotao.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/message")
public class TestWSController{
    //websocket服务层调用类
//    @Autowired
//    private WSMessageService wsMessageService;


    @RequestMapping("/index")
    public String showIndex(){
        return "testWebSocket";
    }
    private TestWebSocketService websocketDemo = new TestWebSocketService();

    //请求入口
    @RequestMapping(value="/TestWS",method= RequestMethod.GET)
    @ResponseBody
    public String TestWS(@RequestParam(value="userId",required=true) Long userId,
                         @RequestParam(value="message",required=true) String message){
//        logger.debug("收到发送请求，向用户{}的消息：{}",userId,message);
        System.out.println("收到发送请求，向用户{}的消息：{}"+userId + message);
        if(websocketDemo.sendToAllTerminal(userId, message)){
            return "发送成功";
        }else{
            return "发送失败";
        }
    }
}
