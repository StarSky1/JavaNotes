package com.schedule;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


public class DateUtil {
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

    /**
     * 日期字符串转日期
     * @param dateStr
     * @param pattern
     * @return
     */
    public static Date strToDate(String dateStr,String pattern){
        if(dateStr==null || dateStr.isEmpty()){
            throw new IllegalArgumentException("argument 1 has wrong value with "+dateStr);
        }
        if(pattern==null || pattern.isEmpty()){
            pattern="yyyy-MM-dd HH:mm:ss";
        }
        SimpleDateFormat sdf=new SimpleDateFormat(pattern);
        Date date= null;
        try {
            date = sdf.parse(dateStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    public static Date getPastDate(int past, Date time) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(time);
        calendar.set(calendar.DAY_OF_YEAR, calendar.get(Calendar.DAY_OF_YEAR) - past + 1);
        Date today = calendar.getTime();
        return today;
    }

    /**
     * 根据offset，改变当前的日期
     * offset单位为分钟
     * @param offset
     * @param time
     * @return
     */
    public static Date getNewDate(int offset,Date time){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(time);
        calendar.setTimeInMillis(calendar.getTimeInMillis()+offset*60*1000);
        Date newDate = calendar.getTime();
        return newDate;
    }

    public static void main(String[] args)throws Exception{
//        Date now = new Date();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");//可以方便地修改日期格式
        String dateStr = new SimpleDateFormat("yyMMddhhmmssmmm").format(new Date());
        System.out.println(dateStr);
    }
}
