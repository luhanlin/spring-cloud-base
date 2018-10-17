package com.luhanlin.demoservice.filter;

import com.netflix.hystrix.strategy.concurrency.HystrixRequestContext;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import java.io.IOException;

/**
 * 类详细描述：HystrixRequestContext初始化过滤器
 *
 * @author Mr_lu
 * @version 1.0
 * @mail allen_lu_hh@163.com
 * 创建时间：2018/10/17 下午11:38
 */
@WebFilter(filterName = "hystrixRequestContextServletFilter",urlPatterns = "/*",asyncSupported = true)
public class HystrixRequestContextServletFilter implements Filter {
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        //初始化Hystrix请求上下文
        HystrixRequestContext context = HystrixRequestContext.initializeContext();
        try {
            //请求正常通过
            chain.doFilter(request, response);
        } finally {
            //关闭Hystrix请求上下文
            context.shutdown();
        }
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void destroy() {

    }
}
