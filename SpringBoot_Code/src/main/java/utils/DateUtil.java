package utils;


import org.apache.commons.lang.StringUtils;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;

import org.joda.time.format.DateTimeFormatter;

import java.util.Date;

public class DateUtil {
    //时间格式化工具
    public static final String STANDARD = "yyyy-MM-dd HH:mm:ss";
    //将字符串类型转换成Date类型
    public static Date string2Date(String strDate){
        DateTimeFormatter dateTimeFormatter = DateTimeFormat.forPattern(STANDARD);
        DateTime dateTime = dateTimeFormatter.parseDateTime(strDate);
        return dateTime.toDate();
    }

    public static Date string2Date(String strDate, String format){
        DateTimeFormatter dateTimeFormatter = DateTimeFormat.forPattern(format);
        DateTime dateTime = dateTimeFormatter.parseDateTime(strDate);
        return dateTime.toDate();
    }

    //将Date类时间转换成字符串时间
    public static String date2String(Date date){
        if(date==null){
            return StringUtils.EMPTY;
        }
        DateTime dateTime = new DateTime(date);
        return dateTime.toString(STANDARD);
    }

    public static String date2String(Date date, String format){
        if(date==null){
            return StringUtils.EMPTY;
        }
        DateTime dateTime = new DateTime(date);
        return dateTime.toString(format);
    }
}
