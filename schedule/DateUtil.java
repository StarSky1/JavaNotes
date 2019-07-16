package com.schedule;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


public class DateUtil {

    /**
     * 日期字符串解析成日期类型
     */
    public static Date DateStrToDate(String dateStr,String simpleDateFormat){
        String pattern;
        if (simpleDateFormat==null || simpleDateFormat.equals(""))
            pattern = "yyyy-MM-dd HH:mm:ss";
        else pattern = simpleDateFormat;
        SimpleDateFormat sdf=new SimpleDateFormat(pattern);
        Date result=null;
        try {
            result = sdf.parse(dateStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return result;
    }


    /**
     * 毫秒转换为指定格式的日期
     */
    public static String secondToDate(long milliSecond, String simpleDateFormat) {
        String patten;
        if (simpleDateFormat==null || simpleDateFormat.equals(""))
            patten = "yyyy-MM-dd hh:mm:ss";
        else patten = simpleDateFormat;
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(milliSecond);        //转换为毫秒
        Date date = calendar.getTime();
        SimpleDateFormat format = new SimpleDateFormat(patten);
        return format.format(date);
    }

    public static String getCurrentDay(){
        Date d = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return sdf.format(d);
    }

    /**
     * 获取与当前偏差i天的日期
     * @param i 日期偏差值:from -10 to -2
     * @return date
     */
    public static String getDate(int i){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Calendar cal = Calendar.getInstance();
        int day = cal.get(Calendar.DATE);
        cal.set(Calendar.DATE,day + i);
        return sdf.format(cal.getTime());
    }

    //获取与指定日期偏差i天的日期
    public static String getDateWithDay(int i, String dateString){
        String result = null;
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date startDate = sdf.parse(dateString);
            Calendar cal = Calendar.getInstance();
            cal.setTime(startDate);
            cal.add(Calendar.DATE, i);
            result= sdf.format(cal.getTime());
        }catch (Exception e){
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 获取指定时间对应的毫秒数
     */
    public static long getTimeMillis(String time) {
        try {
            DateFormat dateFormat = new SimpleDateFormat("yy-MM-dd HH:mm:ss");
            DateFormat dayFormat = new SimpleDateFormat("yy-MM-dd");
            Date curDate = dateFormat.parse(dayFormat.format(new Date()) + " " + time);
            return curDate.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0;
    }

    //TODO:获取yyyy-MM-dd格式的date
    public static Date getNowDateShort() {
        Date currentTime = new Date();
        return null;
    }

    public static void main(String[] args)throws Exception{
//        Date now = new Date();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");//可以方便地修改日期格式
        String dateStr = new SimpleDateFormat("yyMMddhhmmssmmm").format(new Date());
        System.out.println(dateStr);
    }
}
