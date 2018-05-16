package com.taotao.controller;

import com.taotao.common.pojo.EUTreeNode;
import com.taotao.service.ItemCatService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by pei on 2017/7/21.
 */

@Controller
@RequestMapping("/item/cat")
public class ItemCatController {

    @Resource
    private ItemCatService itemCatService;

    @RequestMapping("list")
    @ResponseBody
    private List<EUTreeNode> getCatList(@RequestParam(value="id",defaultValue ="0") Long parentId){
        List<EUTreeNode> list = itemCatService.getCatList(parentId);
        return list;
    }
}
