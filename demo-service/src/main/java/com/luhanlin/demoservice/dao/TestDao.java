package com.luhanlin.demoservice.dao;

import com.luhanlin.demoservice.dao.base.SuperMapper;
import com.luhanlin.demoservice.entity.po.Test;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface TestDao extends SuperMapper<Test> {
    int deleteByPrimaryKey(Integer id);

    int insertSelective(Test record);

    Test selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Test record);

    int updateByPrimaryKey(Test record);
}