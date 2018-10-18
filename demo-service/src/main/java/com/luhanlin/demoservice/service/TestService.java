package com.luhanlin.demoservice.service;

import com.luhanlin.demoservice.entity.po.Test;

import java.util.List;

/**
 * 类详细描述：test 接口类
 *
 * @author Mr_lu
 * @version 1.0
 * @mail allen_lu_hh@163.com
 * 创建时间：2018/10/16 下午3:38
 */
public interface TestService {

    Test queryTest(Integer id);

    int addTest(Test test);

    int updateTest(Test test);

    List<Test> findAll(List<Integer> ids);
}
