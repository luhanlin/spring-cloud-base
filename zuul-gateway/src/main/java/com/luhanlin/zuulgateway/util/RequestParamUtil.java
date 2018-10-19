package com.luhanlin.zuulgateway.util;

import cn.hutool.crypto.SecureUtil;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * <类详细描述> 请求参数转换工具
 *
 * @author luhanlin
 * @version [V_1.0.0, 2018/7/24 18:23]
 */
public class RequestParamUtil {

    public static String list2String(List<?> list) {
        StringBuffer sb = new StringBuffer();
        if (list != null && list.size() > 0) {
            for (int i = 0; i < list.size(); i++) {
                if (list.get(i) == null || list.get(i) == "") {
                    continue;
                }
                if (list.get(i) instanceof List) {
                    sb.append(list2String((List<?>) list.get(i)));
                } else {
                    sb.append(list.get(i));
                }
            }
        }
        return sb.toString();
    }

    /**
     * 获取request作用域中所有参数值,除verifycode
     * @param request
     */
    @SuppressWarnings("rawtypes")
    public static String getRequestParamValue2String(HttpServletRequest request) {
        List<String> params = new ArrayList<>();
        Map map=request.getParameterMap();
        Set keSet=map.entrySet();
        for(Iterator itr = keSet.iterator(); itr.hasNext();){
            Map.Entry me= (Map.Entry)itr.next();
            Object paramName = me.getKey();
            if ("verifycode".equals(paramName.toString())) {
                continue;
            }

            Object paramValue= me.getValue();
            String[] value = new String[1];

            if(paramValue instanceof String[]){
                value = (String[]) paramValue;
            }else{
                value[0] = paramValue.toString();
            }

            for(int k=0;k<value.length;k++){
                params.add(value[k]);
            }
        }
        Collections.sort(params);
        return SecureUtil.md5(list2String(params)).toUpperCase();
    }

    public static String getLordKey(String url,String paramMD5){
        return url+":"+paramMD5;
    }
}
