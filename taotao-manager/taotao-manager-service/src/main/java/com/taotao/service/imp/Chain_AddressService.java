package com.taotao.service.imp;

import com.taotao.mapper.ChUsersMapper;
import com.taotao.pojo.ChUsers;
import com.taotao.pojo.ChUsersExample;
import com.taotao.service.Chain_IAddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by pei on 2017/6/22.
 */
@SuppressWarnings("SpringJavaAutowiringInspection")
@Service
public class Chain_AddressService implements Chain_IAddressService {
    @Autowired
    private ChUsersMapper chUersMapper;

    public List<Map<String, String>> list(String name) {

        //创建查询条件
        ChUsersExample example = new ChUsersExample();
        ChUsersExample.Criteria criteria = example.createCriteria();
        criteria.andUsernameEqualTo(name);
        //根据条件查询
        ArrayList<Map<String, String>> result = new ArrayList<>();

        Map<String, String> hashmap = new HashMap<String, String>();
        List<ChUsers> list = chUersMapper.selectByExample(example);
        for (int i = 0; i < list.size(); i++) {
            hashmap.clear();
            ChUsers chUsers =  list.get(i);
            hashmap.put("address",chUsers.getChainaddress());
            hashmap.put("transaction",chUsers.getTransactionid());
            hashmap.put("userName",chUsers.getUsername());
            hashmap.put("createTime",chUsers.getCreatedat().toString());
            result.add(hashmap);
        }
        return result;
    }

    public int insert(ChUsers users) {

        int flag = chUersMapper.insert(users);

        return flag;
    }
}
