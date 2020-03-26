package com.nugget.common.utils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * @Author zhaoyifan
 * @Date 2019/6/24 15:42
 * @Description TODO
 */
public class HolidayUtil {

    /**
     * 获取时间段日期
     * @param start
     * @param end
     * @return
     * @throws Exception
     */
    public static List<Date> dateSplit(Date start, Date end) throws Exception {
        if (!start.before(end))
            throw new Exception("开始时间应该在结束时间之后");
        Long spi = end.getTime() - start.getTime();
        Long step = spi / (24 * 60 * 60 * 1000);// 相隔天数

        List<Date> dateList = new ArrayList<Date>();
        dateList.add(end);
        for (int i = 1; i <= step; i++) {
            dateList.add(new Date(dateList.get(i - 1).getTime() - (24 * 60 * 60 * 1000)));// 比上一天减一
        }
        return dateList;
    }

    /**
     * 判断是否为节假日，若是返回1，否则返回0
     * @param date
     * @return
     */
    public static int isHoliday(Date date,List<String> holidays ,List<String> workdays){
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DAY_OF_MONTH,-1);
        cal.setTime(date);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        if(((cal.get(Calendar.DAY_OF_WEEK)==Calendar.SATURDAY||cal.get(Calendar.DAY_OF_WEEK)==Calendar.SUNDAY)&&!workdays.contains(sdf.format(date))) || holidays.contains(sdf.format(date)) ) {
            return 1;
        }
        return 0;
    }

    /**
     * 获取上一个工作日
     * @param date
     * @param holidays
     * @param workdays
     * @return
     */
    public static Date getLastWorkDay(Date date,List<String> holidays ,List<String> workdays){
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.DAY_OF_MONTH,-1);
        Date dateTemp = cal.getTime();

        while (isHoliday(dateTemp, holidays, workdays) != 0){
            cal.add(Calendar.DAY_OF_MONTH,-1);
            dateTemp = cal.getTime();
        }
        return dateTemp;
    }

    public static Date getNextWorkDay(Date date,List<String> holidays ,List<String> workdays){
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);

        Calendar calNext = Calendar.getInstance();
        calNext.setTime(date);
        calNext.add(Calendar.DAY_OF_MONTH,1);
        Date dateTemp = calNext.getTime();

        boolean flag = false;
        //判断下一天是否节假日
        while (isHoliday(dateTemp, holidays, workdays) != 0){
            calNext.add(Calendar.DAY_OF_MONTH,1);
            dateTemp = calNext.getTime();
            flag = true;
        }
        if(flag){
            return dateTemp;
        }

        if(cal.get(Calendar.DAY_OF_WEEK)==Calendar.FRIDAY){
            cal.add(Calendar.DATE, 3);
        }else{
            cal.add(Calendar.DATE, 1);
        }
        return cal.getTime();
    }
}
