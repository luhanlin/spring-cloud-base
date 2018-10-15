package com.luhanlin.common.utils;


import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUnit;
import cn.hutool.core.date.DateUtil;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class TimeUtil {

    /**
     * 获取当前时间戳(秒)
     * @return
     */
    public static Long getSecondTimestamp() {
        return System.currentTimeMillis() / DateUnit.SECOND.getMillis();
    }

    /**
     * 时间戳(秒)转日期格式
     * @param timestamp 时间戳(秒)
     * @param format 日期格式
     * @return
     */
    public static String getDateByTimestamp(Long timestamp, String format) {
        return DateUtil.format(new Date(timestamp * DateUnit.SECOND.getMillis()), format);
    }


    public static boolean dateCheck(String dataStr) {
        String eL = "^((\\d{2}(([02468][048])|([13579][26]))[\\-\\/\\s]?((((0?[13578])|(1[02]))[\\-\\/\\s]?((0?[1-9])"
                + "|([1-2][0-9])|(3[01])))|(((0?[469])|(11))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(30)))|(0?2[\\-\\/\\s]?"
                + "((0?[1-9])|([1-2][0-9])))))|(\\d{2}(([02468][1235679])|([13579][01345789]))[\\-\\/\\s]?((((0?[13578])|"
                + "(1[02]))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(3[01])))|(((0?[469])|(11))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(3"
                + "0)))|(0?2[\\-\\/\\s]?((0?[1-9])|(1[0-9])|(2[0-8]))))))";
        Pattern p = Pattern.compile(eL);
        Matcher m = p.matcher(dataStr);
        boolean b = m.matches();
        if (b) {
            return true;
        }
        return false;
    }


    /**
     * 获取过期时间
     * @param minute 超时分钟数
     * @return
     */
    public static Date getExpireTime(Integer minute){
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(System.currentTimeMillis());
        //设置超时
        cal.set(Calendar.MINUTE, cal.get(Calendar.MINUTE) + minute);
        Date time = cal.getTime();
        return time;
    }

    /**
     * 获取过期时间
     * @param minute 超时分钟数
     * @param format 返回时间格式
     * @return
     */
    public static String getExpireTime(Integer minute, String format){
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(System.currentTimeMillis());
        //设置超时
        cal.set(Calendar.MINUTE, cal.get(Calendar.MINUTE) + minute);
        Date time = cal.getTime();
        String time_expire = new SimpleDateFormat(format).format(time);
        return time_expire;
    }

    /**
     * 计算系统时间的提前或推迟
     * @param date
     * @param days
     * @return
     */
    public static Date getBeforeDay(Date date,Integer days) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DAY_OF_MONTH, days);
        date = calendar.getTime();
        return date;
    }

    /**
     * 计算系统时间的提前或推迟
     * @param date
     * @param time 推迟或提前的时间长度
     * @return
     */
    public static Date getBeforeTime(Date date,Integer time,Integer calendarType) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(calendarType, time);
        date = calendar.getTime();
        return date;
    }

    /**
     * 日期转化为cron表达式
     * @param date
     * @return
     */
    public static String getCron(Date  date){
        String dateFormat="ss mm HH dd MM ? yyyy";
        return  DateUtil.format(date, dateFormat);
    }

    /**
     * cron表达式转为日期
     * @param cron
     * @return
     */
    public static Date getCronToDate(String cron) {
        String dateFormat="ss mm HH dd MM ? yyyy";
        SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
        Date date = null;
        try {
            date = sdf.parse(cron);
        } catch (ParseException e) {
            return null;
        }
        return date;
    }

    /**
     * Description:格式化日期,String字符串转化为Date
     *
     * @param date
     * @param dtFormat
     *            例如:yyyy-MM-dd HH:mm:ss yyyyMMdd
     * @return
     */
    public static String fmtDateToStr(Date date, String dtFormat) {
        if (date == null)
            return "";
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat(dtFormat);
            return dateFormat.format(date);
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

//    public static void main(String[] args) {
////        System.out.println(dateCheck("2017-06-04"));
////        String dateFormat = "yyyyMMddHHmmss";
////        Calendar cal = Calendar.getInstance();
////        cal.setTimeInMillis(System.currentTimeMillis());
////        /**设置30分钟后超时*/
////        cal.set(Calendar.MINUTE, cal.get(Calendar.MINUTE) + 30);
////        Date dateinvalid = cal.getTime();
////        String time_expire = new SimpleDateFormat(dateFormat).format(dateinvalid);
////        System.out.println(time_expire);
////        Date startTime = DateUtil.beginOfDay(new Date());
////        Date beforeDay = getBeforeDay(startTime, -2);
////        System.out.println(beforeDay);
////        System.out.println(dateCheck("20171302"));
//        DateTime parse = DateUtil.parse("2018-06-05 12:33:33", "yyyy-MM-dd HH:mm:ss");
//        String dateFormat="ss mm HH dd MM ? yyyy";
//        String format = DateUtil.format(parse, dateFormat);
//        System.out.println(format);
//
//    }

    /**
     * 获取当前时间与23:59:59的时间差
     */
    public static int time(){
        Calendar cal = Calendar.getInstance();
        int currentTime = (int) (cal.getTimeInMillis()/1000);
        cal.set(Calendar.HOUR_OF_DAY, 23);
        cal.set(Calendar.MINUTE, 59);
        cal.set(Calendar.SECOND, 59);
        int  expiryTime  = (int) (cal.getTimeInMillis()/1000);
        int  timeDifference = expiryTime-currentTime;
        return timeDifference;

    }
}
