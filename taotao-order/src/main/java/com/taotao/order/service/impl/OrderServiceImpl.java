package com.taotao.order.service.impl;

import com.taotao.common.pojo.TaotaoResult;
import com.taotao.mapper.TbOrderItemMapper;
import com.taotao.mapper.TbOrderMapper;
import com.taotao.mapper.TbOrderShippingMapper;
import com.taotao.order.pojo.Order;
import com.taotao.order.service.OrderService;
import com.taotao.pojo.TbOrderItem;
import com.taotao.pojo.TbOrderShipping;
import com.taotao.order.dao.JedisClient;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;


import java.util.Date;
import java.util.List;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private TbOrderMapper orderMapper;

    @Autowired
    private TbOrderItemMapper tbOrderItemMapper;

    @Autowired
    private TbOrderShippingMapper tbOrderShippingMapper;

    @Autowired
    private JedisClient jedisClient;

    @Value("${ORDER_GEN_KEY}")
    private String ORDER_GEN_KEY;

    @Value("{ORDER_INIT_ID}")
    private String ORDER_INIT_ID;

    @Value("{ORDER_DETAIL_GEN_KEY}")
    private String ORDER_DETAIL_GEN_KEY;

    @Override
    public TaotaoResult creteOrder(Order order, List<TbOrderItem> itemList, TbOrderShipping tbOrderShipping) {
//       先插入订单表，再查插明细表，最后插物流表
//      1. 插入订单记录，
//      1.1通过redis获得订单号
        String string = jedisClient.get(ORDER_GEN_KEY);
        if (StringUtils.isBlank(string)) {
            jedisClient.set(ORDER_GEN_KEY, ORDER_INIT_ID);
        }
        long orderId = jedisClient.incr(ORDER_GEN_KEY);
//      1.2 补全pojo的属性，
        order.setOrderId(orderId + "");
//        状态： 1 未付款  2 已付款  3 未发货 4 已发货  5 交易成功  6 交易关闭
        order.setStatus(1);
        Date date = new Date();
        order.setCreateTime(date);
        order.setUpdateTime(date);
//        评价 -0  未评价，  1 已评价
        order.setBuyerRate(0);
//      1.3  向订单表中插入数据
        orderMapper.insert(order);
//2.   插入订单明细 : 补全订单明细, 循环订单表，将所有的订单明细插入 到 明细表
        for (TbOrderItem tbOrderItem : itemList) {
            tbOrderItem.setOrderId(orderId + "");
            long orderDetailId = jedisClient.incr(ORDER_DETAIL_GEN_KEY);
            tbOrderItem.setId(orderDetailId + "");
            tbOrderItemMapper.insert(tbOrderItem);

        }
        // 3. 插入物流表 ，补全属性
        tbOrderShipping.setOrderId(orderId + "");
        tbOrderShipping.setCreated(date);
        tbOrderShipping.setUpdated(date);
        tbOrderShippingMapper.insert(tbOrderShipping);
        return TaotaoResult.ok(orderId);
    }
}
