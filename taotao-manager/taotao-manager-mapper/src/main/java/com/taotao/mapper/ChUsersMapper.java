package com.taotao.mapper;

import com.taotao.pojo.ChUsers;
import com.taotao.pojo.ChUsersExample;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ChUsersMapper {
    int countByExample(ChUsersExample example);

    int deleteByExample(ChUsersExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(ChUsers record);

    int insertSelective(ChUsers record);

    List<ChUsers> selectByExample(ChUsersExample example);

    ChUsers selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") ChUsers record, @Param("example") ChUsersExample example);

    int updateByExample(@Param("record") ChUsers record, @Param("example") ChUsersExample example);

    int updateByPrimaryKeySelective(ChUsers record);

    int updateByPrimaryKey(ChUsers record);
}