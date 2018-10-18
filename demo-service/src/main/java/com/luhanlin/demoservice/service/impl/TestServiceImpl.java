package com.luhanlin.demoservice.service.impl;

import com.luhanlin.demoservice.dao.TestDao;
import com.luhanlin.demoservice.entity.po.Test;
import com.luhanlin.demoservice.service.TestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


/**
 * 类详细描述：
 *
 * @author Mr_lu
 * @version 1.0
 * @mail allen_lu_hh@163.com
 * 创建时间：2018/10/16 下午3:39
 */
@Service
public class TestServiceImpl implements TestService {

    @Autowired
    private TestDao testDao;

    @Override
    public Test queryTest(Integer id) {
        return testDao.selectByPrimaryKey(id);
    }

    @Override
    @Transactional
    public int addTest(Test test) {
        return testDao.insert(test);
    }

    @Override
    @Transactional
    public int updateTest(Test test) {
        return testDao.updateById(test);
    }

    @Override
    public List<Test> findAll(List<Integer> ids) {
        return testDao.selectBatchIds(ids);
    }
}
