package com.luhanlin.demoservice.controller;

import com.luhanlin.common.result.ResultInfo;
import com.luhanlin.common.utils.ResultUtil;
import com.luhanlin.dbredis.redis.utils.RedisUtil;
import com.luhanlin.demoservice.conf.prop.CustomBean;
import com.luhanlin.demoservice.entity.po.Test;
import com.luhanlin.demoservice.service.TestService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 类详细描述：TEST 服务接口
 *
 * @author Mr_lu
 * @version 1.0
 * @mail allen_lu_hh@163.com
 * 创建时间：2018/10/16 下午3:03
 */
@Log4j2
@RestController
@RequestMapping(value = "/test")
public class TestController {

    @Autowired
    private TestService testService;

    @Autowired
    private CustomBean customBean;

    @GetMapping(value = "/hello")
    public ResultInfo hello(){
        return  ResultUtil.success(customBean.getVersion());
    }

    @GetMapping(value = "/{id}")
	public ResultInfo getTest(@PathVariable("id") Integer id){
        RedisUtil.set("test","123456");
        log.info(">>>>>>>>>> redis 获取 test >>>>>>" + RedisUtil.get("test"));
        Test test = testService.queryTest(id);
        log.info("查询出来的test为： " + test.toString());

        log.info(">>>>>> 添加test信息 >>>>>>");
//        testService.addTest(new Test(2,"zhangsan","183"));

        log.info(">>>>>> 修改test信息 >>>>>>");
//        test.setPhone("112");
//        testService.updateTest(test);
        return ResultUtil.success(test);
	}

}
