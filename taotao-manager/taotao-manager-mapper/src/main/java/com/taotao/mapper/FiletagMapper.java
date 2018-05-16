package com.taotao.mapper;

import com.taotao.pojo.Filetag;
import com.taotao.pojo.FiletagExample;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface FiletagMapper {
    int countByExample(FiletagExample example);

    int deleteByExample(FiletagExample example);

    int deleteByPrimaryKey(String name);

    int insert(Filetag record);

    int insertSelective(Filetag record);

    List<Filetag> selectByExampleWithBLOBs(FiletagExample example);

    List<Filetag> selectByExample(FiletagExample example);

    Filetag selectByPrimaryKey(String name);

    Filetag selectAll();

    int updateByExampleSelective(@Param("record") Filetag record, @Param("example") FiletagExample example);

    int updateByExampleWithBLOBs(@Param("record") Filetag record, @Param("example") FiletagExample example);

    int updateByExample(@Param("record") Filetag record, @Param("example") FiletagExample example);

    int updateByPrimaryKeySelective(Filetag record);

    int updateByPrimaryKeyWithBLOBs(Filetag record);

    int updateByPrimaryKey(Filetag record);
}