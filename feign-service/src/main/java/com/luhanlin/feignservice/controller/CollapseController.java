package com.luhanlin.feignservice.controller;

import com.luhanlin.common.bean.Test;
import com.luhanlin.common.result.ResultInfo;
import com.luhanlin.common.utils.ResultUtil;
import com.luhanlin.feignservice.service.CollapseService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * 类详细描述：服务请求api
 *
 * @author Mr_lu
 * @version 1.0
 * @mail allen_lu_hh@163.com
 * 创建时间：2018/10/18 上午9:38
 */
@Log4j2
@RestController
@RequestMapping("/collapse")
public class CollapseController {

    @Autowired
    private CollapseService collapseService;

    @GetMapping("/{id}")
    public ResultInfo testRest(@PathVariable("id") Integer id){
        log.info("进行 Collapse 远程服务调用测试,开始时间： " + LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
        Test test = collapseService.testRest(id);
        log.info("进行 Collapse 远程服务调用测试,结束时间： " + LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));

        /**
         * 启用请求合并:
         *      开始时间： 2018-10-18T10:40:12.374
         *      结束时间： 2018-10-18T10:40:13.952
         * 不使用请求合并：
         *      开始时间： 2018-10-18T10:43:41.472
         *      结束时间： 2018-10-18T10:43:41.494
         */
        return ResultUtil.success(test);
    }

}
