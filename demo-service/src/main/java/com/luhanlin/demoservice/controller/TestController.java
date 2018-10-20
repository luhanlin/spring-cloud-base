package com.luhanlin.demoservice.controller;

import com.luhanlin.common.result.ResultInfo;
import com.luhanlin.common.utils.ResultUtil;
import com.luhanlin.dbredis.redis.utils.RedisUtil;
import com.luhanlin.demoservice.conf.prop.CustomBean;
import com.luhanlin.demoservice.entity.po.Test;
import com.luhanlin.demoservice.service.TestService;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 类详细描述：TEST 服务接口
 *
 * @author Mr_lu
 * @version 1.0
 * @mail allen_lu_hh@163.com
 * 创建时间：2018/10/16 下午3:03
 */
@Api(value = "Test服务API",tags = "提供基本的CRUD接口")
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

    @ApiOperation(value = "feign远程服务调用测试",notes = "查询test")
    @ApiImplicitParam(name = "id",value = "用户id")
    @HystrixCommand
    @GetMapping(value = "/{id}")
	public ResultInfo getTest(@PathVariable("id") Integer id){
        RedisUtil.set("test","123456");
        log.info(">>>>>>>>>> redis 获取 test >>>>>>" + RedisUtil.get("test"));
        Test test = testService.queryTest(id);
        log.info("查询出来的test为： " + test);
        return ResultUtil.success(test);
	}

    @HystrixCommand
    @PostMapping(value = "/add")
    public ResultInfo addTest(@RequestBody Test test){
        log.info(">>>>>> 添加test信息 >>>>>>");
        testService.addTest(test);
        return ResultUtil.success("添加成功...");
    }

    @HystrixCommand
    @PutMapping(value = "/update")
    public ResultInfo updateTest(@RequestBody Test test){
        log.info(">>>>>> 修改test信息 >>>>>>");
        testService.updateTest(test);
        return ResultUtil.success("修改成功...");
    }

    @HystrixCommand
    @GetMapping(value = "/collapse/{id}")
    public Test collapse(@PathVariable("id") Integer id){
        return testService.queryTest(id);
    }

    @HystrixCommand
    @GetMapping(value = "/collapse/findAll")
    public List<Test> collapseFindAll(@RequestParam(value = "ids") List<Integer> ids){
        return testService.findAll(ids);
    }
}
