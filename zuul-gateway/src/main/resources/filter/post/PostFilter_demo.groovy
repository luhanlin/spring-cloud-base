package filter.pre

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.cloud.netflix.zuul.filters.support.FilterConstants

import javax.servlet.http.HttpServletRequest

import com.netflix.zuul.ZuulFilter
import com.netflix.zuul.context.RequestContext

class PostFilter_demo extends ZuulFilter {

  Logger log = LoggerFactory.getLogger(PostFilter_demo.class)

  @Override
  String filterType() {
    return FilterConstants.POST_TYPE
  }

  @Override
  int filterOrder() {
    return 777
  }

  @Override
  boolean shouldFilter() {
    return false
  }

  @Override
  Object run() {
    HttpServletRequest request = RequestContext.getCurrentContext().getRequest()
    log.info("this is a post filter: Send {} request to {}", request.getMethod(), request.getRequestURL().toString())
    return null
  }

}