package com.taotao.service;

import com.taotao.common.pojo.EUTreeNode;

import java.util.List;

/**
 * Created by pei on 2017/7/21.
 */
public interface ItemCatService {

    List<EUTreeNode> getCatList(long parentId);
}
