package com.luhanlin.common.constant;

/**
 * 返回码常量定义
 * 响应码编码规则为“xxx    x   nn nn”
      xxx 表示系统类型，三位大写字母
      x   表示信息种类，一位大写字母
      nn  表示信息小分类，两位数字
      nn  表示信息编号，两位数字
 *
 * @create 2018-05-08 17:15
 **/
public class ResultCode {

    public static String SysType = "";
    public static String SysIp = "";
    public static String SysPort = "";

    /*
     * I0	（成功）
     */
    //处理成功
    public final static String SUCCESS = "I0000";

    /*
     * M1	（格式错误）
     */
    //报文无法解析
    public final static String M1001 = "M1001";

    /*
     * M4   (查询错误)
     */
    public final static String JQUERY_ERROR = "M4000";

}
