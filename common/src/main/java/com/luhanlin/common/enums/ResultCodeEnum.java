package com.luhanlin.common.enums;

/**
 * 响应码相关定义
 * <类详细描述>
 * 响应码编码规则为“xxx    x   nn nn”
 *       xxx 表示系统类型，三位大写字母
 *       x   表示信息种类，一位大写字母
 *       nn  表示信息小分类，两位数字
 *       nn  表示信息编号，两位数字
 *
 * @author luhanlin
 * @version [V_1.0.0, 2018/5/11 18:00]
 */
public enum ResultCodeEnum {

    // **********通知信息定义****************
    SUCCESS("I0000", "请求成功"),

    // **********消息错误定义****************
    MsgCannotBeParsed("M1001", "报文无法解析"),
    MsgDomainLengthIsIllegal("M1002", "域值长度非法"),
    MsgDomainFormatIsIllegal("M1003", "域值格式非法"),
    MsgDomainIsIllegal("M1004", "域值非法"),
    MsgDomainContainsIllegalCharacters("M1005", "域值含非法字符"),
    MsgDomainCannotBeEmpty("M1006", "值域不能为空"),
    MsgIllegalDatetime("M1007", "日期非法"),
    MsgIllegalAmount("M1008", "金额值非法"),
    MsgUpperAmountLimit("M1009", "金额超过规定的金额上限"),
    MsgIllegalTransactionsNum("M1010", "业务笔数非法"),
    MsgMsgNotAgreement("M1011", "报文总分不一致"),
    MsgOthersFormatError("M1999", "其他格式错误"),

    // ***********安全相关错误**************
    MsgCheckFailure("M2001", "报文核验校验失败"),
    MsgDecryptionFailure("M2002", "报文解密失败"),
    MsgNoSuchBusinessPermission("M2003", "无此业务权限"),
    MsgUserOrPasswordError("M2004", "用户或密码错误"),
    MsgChannelWithoutPermission("M2005", "该渠道无权限"),
    MsgOthersPermissionError("M2999", "其他权限错误"),

    // **********证书或签名错误相关错误*********
    MsgInvalidDigitalSignature("M3001", "数字签名无效"),
    MsgSignerCertificateIsInvalid("M3002", "签名者证书无效"),
    MsgNotOriginatorOfTheBusiness("M3003", "签名者非业务发起方"),
    MsgNotBoundDigitalCertificate("M3004", "数字证书未绑定"),
    MsgOthersSignatureError("M3999", "其他编签核签错"),

    // ***********数据库相关错误**************
    DBKeywordRepetition("D1001", "关键字重复"),
    DBRollbackFailure("D1002", "Rollback失败"),
    DBSelectFailure("D1003", "SELECT失败"),
    DBFetchFailure("D1004", "FETCH失败"),
    DBInsertFailure("D1005", "INSERT失败"),
    DBUpdateFailure("D1006", "UPDATE失败"),
    DBDeleteFailure("D1007", "DELETE失败"),
    DBCountFailure("D1008","COUNT失败"),
    DBStatusException("D1009","用户状态异常"),

    // ***********数量相关错误****************
    DBTableEmpty("D2001", "数据库表空"),
    DBRecordNotFound("D2002", "未找到记录"),
    DBNullOfConnection("D2003", "数据库连接对象为空"),
    DBNullOfCursor("D2004", "游标为空"),
    DBGetSequenceException("D2005", "获取序号值信息异常,结果集不为1条"),
    DBDistributedLocksCreateException("D2006", "分布式锁创建异常"),
    DBDistributedLocksUnknownException("D2007", "分布式锁创建未知异常"),
    DBstatus("D2999", "该用户被禁用"),
    OtherDBError("D9999", "数据库其他错误"),

    // ***********mongodb相关错误****************
    DBMongoInsertFailure("D3001", "MongoDB_INSERT失败"),

    // ***********文件相关错误****************
    FileNotFound("F1001", "未能找到文件记录"),
    FileOpenFailed("F1002", "打开文件失败"),
    FileCloseFailed("F1003", "关闭文件失败"),
    FileIOError("F1004", "文件IO错误"),
    FileUnreadable("F1005", "文件不可读"),
    FileParsingFailed("F1006", "文件解析失败"),
    FileExportFailed("F1007", "文件导出失败"),

    // ***********Queue相关错误****************
    QueueManagerConnectionError("Q1001", "连接队列管理器错误"),
    QueueOpenError("Q1002", "打开队列错误"),
    QueueOfMessagePlaceError("Q1003", "放置消息到队列错误"),
    QueueOfMessageReadError("Q1004", "从队列读取消息错误"),
    QueueDestinationUnreachableError("Q1005", "目的地无法到达错误"),
    QueueRouteError("Q1006", "路由错误"),

    // ***********业务相关错误*****************
    BusinessRepeatingError("O1001","业务重复"),
    BusinessTimedOutError("O1002","业务已超时"),
    BusinessInvokeServiceFailed("O1003","调用服务失败"),
    WEAK_NET_WORK("-1", "网络异常，请稍后重试"),
    PARAMETER_ERROR("10101", "参数错误");

    private String code;
    private String msg;


    ResultCodeEnum(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public String getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }

}
