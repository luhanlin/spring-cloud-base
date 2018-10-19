package com.luhanlin.zuulgateway.filter.post;

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
public class UrlCachePostFilter extends ZuulFilter {
    @Override
    public String filterType() {
        return FilterConstants.POST_TYPE;
    }

    @Override
    public int filterOrder() {
        return FilterConstants.SEND_RESPONSE_FILTER_ORDER +1;
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
//        log.info("responseBody : " + responseBody);
//        String body = null;
//        try {
//            InputStream stream = currentContext.getResponseDataStream();
//            body = StreamUtils.copyToString(stream, Charset.forName("UTF-8"));
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

//        if (!BlankUtil.isBlank(responseBody)){
////            String pathInfo = request.getPathInfo();
////            String[] clientUrl = pathInfo.split("/");
////            String method = clientUrl[clientUrl.length-1];
//            String key = RequestParamUtil.getLordKey(requestURL,RequestParamUtil.getRequestParamValue2String(request));
//            // 设置过期时间
//            RedisUtil.set(key, responseBody, 1000);
//            log.info(String.format("数据存入redis成功，key:%s", key));
//        }
        return null;
    }



}
