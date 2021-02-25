package com.sunyw.xyz.dao;

import com.sunyw.xyz.domain.LogInfo;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface LogInfoMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(LogInfo record);

    int insertSelective(LogInfo record);

    LogInfo selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(LogInfo record);

    int updateByPrimaryKey(LogInfo record);
}