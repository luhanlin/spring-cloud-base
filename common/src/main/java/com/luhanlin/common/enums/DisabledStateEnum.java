package com.luhanlin.common.enums;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import lombok.Getter;
import lombok.Setter;

/**
 * @author Mr_lu Email:allen_lu_hh@163.com
 * @version 1.0 
 * 创建时间：2018-6-2
 * 类说明：启用禁用的枚举
 * 禁用启用状态 1启用0是禁用 默认1
 */
public enum DisabledStateEnum {
	
	ON(1,"启用"),
	OFF(0,"禁用");

	@Getter
    @Setter
	private Integer code;
    @Getter
    @Setter
	private String value;
    @Getter
    @Setter
	private String msg;

	DisabledStateEnum(Integer code, String value) {
		this.code = code;
        this.value = value;
	}
	
	DisabledStateEnum(Integer code, String value, String msg) {
        this.code = code;
        this.value = value;
        this.msg = msg;
    }
	
	/**
     *将该枚举全部转化成json
     * @return
     */
    public static String toJson(){
        JSONArray jsonArray = new JSONArray();
        for (DisabledStateEnum e : DisabledStateEnum.values()) {
            JSONObject object = new JSONObject();
            object.put("code", e.getCode());
            object.put("value",e.getValue());
            jsonArray.add(object);
        }
        return jsonArray.toString();
    }
	
    /**
     * 根据code获取value
     * @param code
     * @return
     */
    public static String getValue(Integer code) {
    	if(code==null) return null;
        for (DisabledStateEnum e : DisabledStateEnum.values()) {
            if (e.getCode() == code) {
                return e.value;
            }
        }
        return null;
    }
    
    /**
     * 根据code获取code，即判断code在枚举里是否存在
     * @param code
     * @return
     */
    public static Integer getCode(Integer code) {
    	if(code==null) return null;
        for (DisabledStateEnum e : DisabledStateEnum.values()) {
            if (e.getCode() == code) {
                return e.getCode();
            }
        }
        return null;
    }

}
