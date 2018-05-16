package com.taotao.mapper;

import com.taotao.pojo.Public;
import com.taotao.pojo.PublicExample;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface PublicMapper {
    int countByExample(PublicExample example);

    int deleteByExample(PublicExample example);

    int deleteByPrimaryKey(String name);

    int insert(Public record);

    int insertSelective(Public record);

    List<Public> selectByExampleWithBLOBs(PublicExample example);

    List<Public> selectByExample(PublicExample example);

    Public selectByPrimaryKey(String name);

    int updateByExampleSelective(@Param("record") Public record, @Param("example") PublicExample example);

    int updateByExampleWithBLOBs(@Param("record") Public record, @Param("example") PublicExample example);

    int updateByExample(@Param("record") Public record, @Param("example") PublicExample example);

    int updateByPrimaryKeySelective(Public record);

    int updateByPrimaryKeyWithBLOBs(Public record);
}