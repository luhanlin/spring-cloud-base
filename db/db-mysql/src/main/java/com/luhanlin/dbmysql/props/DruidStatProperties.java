package com.luhanlin.dbmysql.props;


import lombok.Data;

/**
 * durid监控 相关配置
 * <类详细描述>
 *
 * @author luhanlin
 * @version [V_1.0.0, 2018/10/15 14:50]
 */
@Data
public class DruidStatProperties {

    private String deny;

    private String allow;

    private String resetEnable = "false";

    private String loginUsername;

    private String loginPassword;

}
