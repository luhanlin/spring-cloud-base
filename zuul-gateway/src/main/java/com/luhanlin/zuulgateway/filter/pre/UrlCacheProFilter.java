package com.luhanlin.zuulgateway.filter.pre;

import com.luhanlin.common.utils.BlankUtil;
import com.luhanlin.dbredis.redis.utils.RedisUtil;
import com.luhanlin.zuulgateway.util.RequestParamUtil;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import lombok.extern.log4j.Log4j2;
import org.springframework.cloud.netflix.zuul.filters.support.FilterConstants;

import javax.servlet.http.HttpServletRequest;

/**
 * <类详细描述> url缓存
 *
 * @author luhanlin
 * @version [V_1.0.0, 2018/7/23 18:26]
 */
@Log4j2
public class UrlCacheProFilter extends ZuulFilter {
    @Override
    public String filterType() {
        return FilterConstants.PRE_TYPE;
    }

    @Override
    public int filterOrder() {
        return FilterConstants.PRE_DECORATION_FILTER_ORDER +1;
    }

    @Override
    public boolean shouldFilter() {
        return false;
    }

    @Override
    public Object run() {
        RequestContext currentContext = RequestContext.getCurrentContext();
        HttpServletRequest request = currentContext.getRequest();
        String method = request.getMethod();
        String requestURL = request.getRequestURL().append(method).toString();
        log.info("this is a UrlCache filter: Send {} request to {}", method, requestURL);
        //        String pathInfo = request.getPathInfo();
//        log.info("pathInfo : " + pathInfo);
//        String[] clientUrl = pathInfo.split("/");
//        String method = clientUrl[clientUrl.length-1];
        String key = RequestParamUtil.getLordKey(requestURL,RequestParamUtil.getRequestParamValue2String(request));
        log.info("key : " + key);
        String urlCache = RedisUtil.get(key);
        log.info(String.format("从缓存中得到数据:%s", urlCache));
        // 不做路由进行跳转，直接返回
        if (!BlankUtil.isBlank(urlCache)){
            currentContext.setSendZuulResponse(false);
            currentContext.setResponseStatusCode(200);
            currentContext.setResponseBody(urlCache);
        }
        return null;
    }

}
