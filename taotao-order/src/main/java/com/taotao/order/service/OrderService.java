package com.taotao.order.service;

import com.taotao.common.pojo.TaotaoResult;
import com.taotao.order.pojo.Order;
import com.taotao.pojo.TbOrderItem;
import com.taotao.pojo.TbOrderShipping;

import java.util.List;

public interface OrderService {
    TaotaoResult creteOrder(Order order, List<TbOrderItem> itemList, TbOrderShipping tbOrderShipping);
}
