package com.taotao.rest.service.impl;

import com.taotao.mapper.TbItemCatMapper;
import com.taotao.pojo.TbItemCat;
import com.taotao.pojo.TbItemCatExample;
import com.taotao.rest.pojo.CartResult;
import com.taotao.rest.pojo.CatNode;
import com.taotao.rest.service.ItemCatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ItemCatServiceImpl implements ItemCatService {
    @Autowired
    private TbItemCatMapper tbItemCatMapper;

    /**
     * 查询 分类列表
     *
     * @return
     */
    @Override
    public CartResult getItemCatList() {
        CartResult cartResult = new CartResult();
        cartResult.setData(getCartList(0));
        return cartResult;
    }

    private List<?> getCartList(long parentId) {
        TbItemCatExample example = new TbItemCatExample();
        TbItemCatExample.Criteria criteria = example.createCriteria();
        criteria.andParentIdEqualTo(parentId);
        //执行查询
        List<TbItemCat> list = tbItemCatMapper.selectByExample(example);
        ArrayList<Object> resultList = new ArrayList<>();
        int count=0;
        for (TbItemCat tbItemCat : list) {
            if(tbItemCat.getIsParent()){
                CatNode catNode = new CatNode();
                if (parentId == 0) {
                    catNode.setName("<a href='/products/1.html'>"+tbItemCat.getName()+"</a>");
                } else {
                    catNode.setName(tbItemCat.getName());
                }
                catNode.setItem(getCartList(tbItemCat.getId()));
                catNode.setUrl("/products/"+tbItemCat.getId()+".html");

                resultList.add(catNode);
                count++;
                // 第一级支取14条记录
                if(parentId == 0 && count>=14){
                    break;
                }
            }else {
                /// 叶子节点地操作
                resultList.add("/products/"+tbItemCat.getId() +".html|"+tbItemCat.getName());
            }

        }
        return resultList;

    }
}
