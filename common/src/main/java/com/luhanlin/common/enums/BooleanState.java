package com.luhanlin.common.enums;

import lombok.Getter;
import lombok.Setter;

/**
 * 通用状态枚举类
 * <类详细描述>
 *      业务枚举类型可以继承此枚举
 *
 * @author luhanlin
 * @version [V_1.0.0, 2018/10/14 10:04]
 */
public enum BooleanState {

    // 通常用于简单的标识 是或者否的状态
    NO("0","否"),
    YES("1","是");

    @Getter
    @Setter
    private String code;
    @Getter
    @Setter
    private String value;


    BooleanState(String code, String value) {
        this.code = code;
        this.value = value;
    }

}
