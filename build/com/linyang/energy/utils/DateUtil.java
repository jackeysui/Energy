package com.linyang.energy.utils;

import com.linyang.common.web.common.Log;
import org.apache.commons.lang.StringUtils;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * @Description 时间工具类
 * @author Leegern
 * @date 2013-7-30 下午01:44:39
 */
public class DateUtil {
	/**
	 * 默认格式化时间格式
	 */
	public static final String DEFAULT_PATTERN = "yyyy-MM-dd HH:mm:ss";

	public static final String SHORT_PATTERN = "yyyy-MM-dd";
	public static final String MOUDLE_PATTERN = "yyyy-MM-dd HH:mm";
	public static final String MONTH_PATTERN = "yyyy-MM";
	public static final String YEAR_PATTERN = "yyyy";
	public static final String HOUR_MINUTE_PATTERN = "HH:mm";
    public static final String MINUTE_PATTERN = "mm";
    public static final String DEFAULT_TIME_PATTERN = "HH:mm:ss";
	/**
	 * 获取当前系统时间字符串
	 * @param pattern 时间格式
	 * @return 时间字符串
	 */
	public static String getCurrentDateStr(String pattern){
		if (null == pattern || pattern.length()==0) {
			pattern = DEFAULT_PATTERN;
		}
		return formatDate(new Date(), pattern);
	}
	
	
	/**
	 * 获取当前系统时间字符串,默认返回的格式:yyyy-MM-dd HH:mm:ss
	 * @return 时间字符串
	 */
	public static String getCurrentDateStr(){
		
		return getCurrentDateStr(null);
	}
	
	/**
	 * 获取昨天时间字符串
	 * @param pattern 时间格式
	 * @return 时间字符串
	 */
	public static String getYesterdayDateStr(String pattern){
		if (null == pattern ||pattern.length()==0) {
			pattern = DEFAULT_PATTERN;
		}
		Date date = getSomeoneDate(-1);
		return formatDate(date, pattern);
	}
	
	/**
	 * 
	 * 函数功能说明  :获取明天时间字符串
	 * @param pattern
	 * @return      
	 * @return  String     
	 * @throws
	 */
	public static String getTomorrowDateStr(String pattern){
		if (null == pattern || pattern.length()==0) {
			pattern = DEFAULT_PATTERN;
		}
		Date date = getSomeoneDate(1);
		return formatDate(date, pattern);
	}
	
	/**
	 * 获取昨天时间字符串,默认返回的格式:yyyy-MM-dd HH:mm:ss
	 * @return 时间字符串
	 */
	public static String getYesterdayDateStr(){
		
		return getYesterdayDateStr(null);
	}
	
	/**
	 * 获取当前时间一周前的时间字符串
	 * @param pattern 时间格式
	 * @return 时间字符串
	 */
	public static String getAWeekAgoDateStr(String pattern){
		if (StringUtils.isEmpty(pattern)) {
			pattern = DEFAULT_PATTERN;
		}
		return formatDate(getSomeoneDate(-7), pattern);
	}
	
	/**
	 * 获取本月某天的日期
	 * @param num 天数
	 * @return 日期
	 */
	public static Date getSomeoneDate(int num){
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.DAY_OF_MONTH, num);
		return calendar.getTime();
	}
	
	/**
	 * 将字符串日期转换为Date日期 
	 * @param dateStr 字符串日期
	 * @return Date日期
	 * @throws ParseException 
	 */
	public static Date convertStrToDate(String dateStr){
		if (StringUtils.isEmpty(dateStr)){
			return null;
		}
		return parseDate(dateStr, DEFAULT_PATTERN);
	}
	
	/**
	 * 
	 * 函数功能说明  :将字符串日期转换为指定格式的Date日期
	 * @param dateStr
	 * @param pattern
	 * @return      
	 * @return  Date     
	 * @throws
	 */
	public static Date convertStrToDate(String dateStr,String pattern){
		if (StringUtils.isEmpty(dateStr)){
			return null;
		}
		return parseDate(dateStr, pattern);
	}
	
	/**
	 * 
	 * 函数功能说明  :将字符串日期转换为指定格式的timeStamp日期
	 * @param dateStr
	 * @param pattern
	 * @return      
	 * @return  Date     
	 * @throws
	 */
	public static Timestamp convertStrToTimestamp(String dateStr,String pattern){
		if (StringUtils.isEmpty(dateStr)){
			return null;
		}
		DateFormat format = new SimpleDateFormat(pattern); 
		//严格匹配日期格式
		format.setLenient(false);
		Timestamp ts = new Timestamp(0);
		try {
			ts = new Timestamp(format.parse(dateStr).getTime());
		} catch (ParseException e) {
			Log.error("DateUtil -- convertStrToTimestamp format date error!");
		}
		return ts;
	}
	
	/**
	 * 函数功能说明 :获取给定日期的上个月同期日期
	 * @param date
	 * @return Date
	 */
	public static Date getLastMonthDate(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.MONTH, -1);
		return cal.getTime();
	}
	
	/**
	 * 
	 * 函数功能说明  :获取给定日期的几个月前或者后同期日期
	 * @param date
	 * @param i （正数为后，负数为前）
	 * @return      
	 * @return  Date     
	 * @throws
	 */
	public static Date getMonthDate(Date date,int i) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.MONTH, i);
		return cal.getTime();
	}
	
	/**
	 * 函数功能说明 :获取给定日期的下个月同期日期
	 * @param date
	 * @return Date
	 */
	public static Date getNextMonthDate(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.MONTH, 1);
		return cal.getTime();
	}
	
	 /**
     * 函数功能说明 :获取给定日期的下一天的日期
     * @param date
     * @return Date
     */
    public static Date getNextDayDate(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.DAY_OF_MONTH, 1);
        return cal.getTime();
    }

    public static Date getSomeNextDayDate(Date date,int n) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.DAY_OF_MONTH, n);
        return cal.getTime();
    }

    /**
     * 函数功能说明 :获取给定日期的下n小时的日期
     * @param date
     * @return Date
     */
    public static Date getSomeNextHourDate(Date date, int n) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.HOUR, n);
        return cal.getTime();
    }

    /**
     * 函数功能说明 :获取给定日期的下n分钟的日期
     * @param date
     * @return Date
     */
    public static Date getNextMinuteDate(Date date, int n) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.MINUTE, n);
        return cal.getTime();
    }

    /**
     * 函数功能说明 :获取给定日期的下一秒的日期
     * @param date
     * @return Date
     */
    public static Date getNextSecondDate(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.SECOND, 1);
        return cal.getTime();
    }

    public static Date getSomeNextSecondDate(Date date,int n) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.SECOND, n);
        return cal.getTime();
    }

	/**
	 * 函数功能说明 :获取给定日期的上个年同期日期
	 * @param date
	 * @return Date
	 */
	public static Date getLastYearDate(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.YEAR, -1);
		return cal.getTime();
	}
	
	
	public static Date getYearFirstDate(Date date){
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.set(Calendar.MONTH,0);
		calendar.set(Calendar.DAY_OF_MONTH,1);
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		return calendar.getTime();
		
		
	}
	/**
	 * 函数功能说明 :获取给定日期的下个年同期日期
	 * @param date
	 * @return Date
	 */
	public static Date getNextYearDate(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.YEAR, 1);
		return cal.getTime();
	}
	/**
	 * 获取当前月的第一天(不包括时分秒)  
	 * @return
	 */
	public  static Date getCurrMonthFirstDay(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.set(Calendar.DAY_OF_MONTH, 1);
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		return calendar.getTime();
	}
	
	/**
	 * 获取上个月的第一天
	 * */
	public static Date getPreMonthFristDay(Date date)
	{
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.MONTH, -1);
		calendar.set(Calendar.DAY_OF_MONTH, 1);
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		return calendar.getTime();
	}
	
	/**
	 * 将Date日期 转换为字符串日期
	 * @param date     日期
	 * @param  pattern 格式
	 * @return 字符串日期
	 */
	public static String convertDateToStr(Date date, String pattern){
		if (null == date){
			return null;
		}
		if (StringUtils.isEmpty(pattern)) {
			pattern = DEFAULT_PATTERN;
		}
		return formatDate(date, pattern);
	}
	
	/**
	 * 将字符串日期按照给定的格式转化为Date日期
	 * @param dateStr 字符串日期
	 * @param pattern 格式
	 * @return
	 * @throws ParseException
	 */
	public static Date parseDate(String dateStr, String pattern){
		SimpleDateFormat sdf = new SimpleDateFormat(pattern);
		try {
			return sdf.parse(dateStr);
		} catch (ParseException e) {
			Log.error("DateUtil -- parseDate parse date error!");
		}
		return null;
	}
	
	/**
	 * 将日期按照给定的格式转化为字符串
	 * @param date    日期
	 * @param pattern 格式
	 * @return 日期字符串
	 */
	private static String formatDate(Date date, String pattern){
		SimpleDateFormat sdf = new SimpleDateFormat(pattern);
		return sdf.format(date);
	}
	
	 // 给定一个日期，获得这个日期所在周的第一天(周日)的日期
    // 输入参数：String date
    // -----------------------------------------------------
    public static String getMonday(String date) {
        SimpleDateFormat format = new SimpleDateFormat(SHORT_PATTERN);
        Date d = null;
        try {
            d = format.parse(date);
        } catch(ParseException e) {
        	Log.info("getMonday error ParseException");
        }
        Calendar cal = Calendar.getInstance();
        cal.setTime(d);
        if (cal.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY)
			cal.add(Calendar.DAY_OF_MONTH, -1);
		cal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        return format.format(cal.getTime());
    }
   
    // 给定一个日期，获得这个日期所在周的周六的日期
    // 输入参数：String date
    // -----------------------------------------------------
	public static String getSunday(String date) {
        SimpleDateFormat format = new SimpleDateFormat(SHORT_PATTERN);
        Date d = null;
        try {
            d = format.parse(date);
        } catch(ParseException e) {
        	Log.info("getSunday error ParseException");
        }
        Calendar cal = Calendar.getInstance();
        cal.setTime(d);
    
        if(cal.get(Calendar.DAY_OF_WEEK) != Calendar.SUNDAY){
            cal.set(Calendar.DAY_OF_WEEK,Calendar.SATURDAY);
            cal.add(Calendar.DAY_OF_MONTH, 1);
        }
        return format.format(cal.getTime());
    }


    // 给定一个日期，获得是周几 (1代表周日...)
    public static int getDateWeekNum(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return cal.get(Calendar.DAY_OF_WEEK);
    }

   
    // 给定一个日期和天数，获得这个相加后的日期
    // 输入参数：String date,int iday
    // -----------------------------------------------------
    @SuppressWarnings("static-access")
    public static String addDay(String date,int iday) {
        SimpleDateFormat format = new SimpleDateFormat(SHORT_PATTERN);
        Date d = null;
        try {
            d = format.parse(date);
        } catch(ParseException e) {
        	Log.info("addDay error ParseException");
        }
        Calendar cal = Calendar.getInstance();
        cal.setTime(d);
        cal.add(cal.DAY_OF_YEAR,iday);
        return format.format(cal.getTime());
    }
    
    /**
     * 得到月所在的最后一天的日期
     * @param date
     * @return
     */
    public static String getMonthLastDay(String date){
    	 SimpleDateFormat format = new SimpleDateFormat(SHORT_PATTERN);
         Date d = null;
         try {
             d = format.parse(date);
         } catch(ParseException e) {
             Log.error("DateUtil -- getMonthLastDay format date error!");
         }
         Calendar calendar = Calendar.getInstance();
         calendar.setTime(d);
         calendar.add(Calendar.MONTH, -1);
         
         calendar.add(Calendar.MONTH, 2);    //加2个月变成本月的下个月
         calendar.set(Calendar.DATE, 1);        //设置为该月第一天
         calendar.add(Calendar.DATE, -1);    //再减一天即为上个月最后一天
         return format.format(calendar.getTime());
    }
    
    /**
	 * 字串6位，前4代表年，后2代表月，
	 * 返回给定日期中的月份中的最后一天组成的日期
	 * @param date  年-月-日，格式为yyyy-MM-dd
	 * @return String 
	 */
	public static String getMonthLastDay2(String date){
		int getYear = Integer.parseInt(date.substring(0, 4));
		int getMonth = Integer.parseInt(date.substring(5, 7));
		String getLastDay = "";
		if (getMonth == 2){
			if (getYear % 4 == 0 && getYear % 100 != 0 || getYear % 400 == 0)
				getLastDay = "29";
			else
				getLastDay = "28";
		}
		else if (getMonth == 4 || getMonth == 6 || getMonth == 9 || getMonth == 11)
			getLastDay = "30";
		else
			getLastDay = "31";
		return String.valueOf(getYear) + "-" + String.valueOf(getMonth) + "-" + getLastDay;
	}
    
    /**
	 * 比较两个日期(年月型，格式为YYYYMM)之间相差月份
	 * @param dealMonth -开始年月
	 * @param alterMonth - 结束年月
	 * @return alterMonth-dealMonth相差的月数
	 */
	public static int calBetweenTwoMonth(String dealMonth, String alterMonth) {
		int length = 0;
		if ((dealMonth.length() != 6) || (alterMonth.length() != 6)) {
			// 比较年月字符串的长度不正确
			length = -1;
		} else {
			int dealInt = Integer.parseInt(dealMonth);
			int alterInt = Integer.parseInt(alterMonth);
			if (dealInt < alterInt) {
				// 第一个年月变量应大于或等于第二个年月变量
				length = -2;
			} else {
				int dealYearInt = Integer.parseInt(dealMonth.substring(0, 4));
				int dealMonthInt = Integer.parseInt(dealMonth.substring(4, 6));
				int alterYearInt = Integer.parseInt(alterMonth.substring(0, 4));
				int alterMonthInt = Integer.parseInt(alterMonth.substring(4, 6));
				length = (dealYearInt - alterYearInt) * 12+ (dealMonthInt - alterMonthInt);
			}
		}
		return length;
	}
	/**
	 * 得到两个日期之间相差的天数
	 * @param newDate 大的日期
	 * @param oldDate 小的日期
	 * @return newDate-oldDate相差的天数
	 */
	public static int daysBetweenDates(Date newDate, Date oldDate){
		int days = 0;
		Calendar calo = Calendar.getInstance();
		Calendar caln = Calendar.getInstance();
		calo.setTime(oldDate);
		caln.setTime(newDate);
		int oday = calo.get(Calendar.DAY_OF_YEAR);
		int nyear = caln.get(Calendar.YEAR);
		int oyear = calo.get(Calendar.YEAR);
		while (nyear > oyear){
			calo.set(Calendar.MONTH, 11);
			calo.set(Calendar.DATE, 31);
			days = days + calo.get(Calendar.DAY_OF_YEAR);
			oyear = oyear + 1;
			calo.set(Calendar.YEAR, oyear);
		}
		int nday = caln.get(Calendar.DAY_OF_YEAR);
		days = days + nday - oday;
		return days;
	}
	/**
	 * 取得与原日期相差一定天数的日期，返回Date型日期
	 * @param date 原日期
	 * @param intBetween 相差的天数
	 * @return date加上intBetween天后的日期
	 */
	public static Date getDateBetween(Date date, int intBetween){
		Calendar calo = Calendar.getInstance();
		calo.setTime(date);
		calo.add(Calendar.DATE, intBetween);
		return calo.getTime();
	}
	
	/**
	 * 
	 * 函数功能说明  :比较两个时刻相差的分钟数在一天中的占比，格式如04:00与08:00占25%保留2位有效数字
	 * @param startTime
	 * @param endTime
	 * @return      
	 * @return  int     
	 * @throws
	 */
	public static double getMinBetween(String startTime, String endTime) {
		String[] s1 = startTime.split(":");
		String[] s2 = endTime.split(":");
		int between = 0;//相差分钟数
		int sTimeH = Integer.parseInt(s1[0]);//开始时间小时
		int sTimeM = Integer.parseInt(s1[1]);//开始时间分钟
		int eTimeH = Integer.parseInt(s2[0]);//结束时间小时
		int eTimeM = Integer.parseInt(s2[1]);//结束时间分钟
		if((sTimeH>eTimeH)||(sTimeH==eTimeH&&sTimeM>=eTimeM)) {//跨天
			between = (24-sTimeH+eTimeH)*60+(eTimeM-sTimeM);
		} else if((sTimeH<eTimeH)||(sTimeH==eTimeH&&sTimeM<eTimeM)) {
			between = (eTimeH-sTimeH)*60+(eTimeM-sTimeM);
		}
		BigDecimal bg = new BigDecimal(between).divide(new BigDecimal(1440), 2, BigDecimal.ROUND_HALF_UP);     
		//DecimalFormat df = new DecimalFormat("#.00");
		return bg.doubleValue();
	}

	/**
	 * 获取上上个月第一天日期字符串
	 * @return 上上个月第一天日期字符串
	 */
	public static Date getPrePreMonthFirstDay(Date date) {
		Calendar lastDate = Calendar.getInstance();
        lastDate.setTime(date);
		lastDate.set(Calendar.DATE, 1);
		lastDate.add(Calendar.MONTH, -2);
		lastDate.set(Calendar.HOUR_OF_DAY, 0);
		lastDate.set(Calendar.MINUTE, 0);
		lastDate.set(Calendar.SECOND, 0);
		return new Date(lastDate.getTimeInMillis());
	}

    /**
     * 获取相差n个月的第一天的日期
     * @return
     */
    public static Date getPreSomeMonthFirstDay(Date date, int n) {
        Calendar lastDate = Calendar.getInstance();
        lastDate.setTime(date);
        lastDate.set(Calendar.DATE, 1);
        lastDate.add(Calendar.MONTH, n);
        lastDate.set(Calendar.HOUR_OF_DAY, 0);
        lastDate.set(Calendar.MINUTE, 0);
        lastDate.set(Calendar.SECOND, 0);
        return new Date(lastDate.getTimeInMillis());
    }

	/**
	 * 获取上上个月最后一天日期字符串
	 * @return 上上个月最后一天日期字符串
	 */
	public static Date getPrePreMonthLastDay(Date date) {
		Calendar lastDate = Calendar.getInstance();
        lastDate.setTime(date);
		lastDate.add(Calendar.MONTH, -2);
		lastDate.set(Calendar.DATE, 1);
		lastDate.roll(Calendar.DATE, -1);
		lastDate.set(Calendar.HOUR_OF_DAY, 23);
		lastDate.set(Calendar.MINUTE, 59);
		lastDate.set(Calendar.SECOND, 59);
		return new Date(lastDate.getTimeInMillis());
	}
	
	public static boolean isToday(Date date) {
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		Calendar n = Calendar.getInstance();
		if (c.get(Calendar.YEAR) == n.get(Calendar.YEAR)
				&& c.get(Calendar.MONTH) == n.get(Calendar.MONTH)
				&& c.get(Calendar.DAY_OF_MONTH) == n.get(Calendar.DAY_OF_MONTH)) {
			return true;
		}
		return false;
	}
	
	/**
	 * 清掉时间的小时、分、秒
	 * 
	 * @param d
	 * @return
	 */
	public static Date clearDate(Date d) {
		Calendar c = Calendar.getInstance();
		c.setTime(d);
		c.set(Calendar.HOUR_OF_DAY, 0);
		c.set(Calendar.MINUTE, 0);
		c.set(Calendar.SECOND, 0);
		c.set(Calendar.MILLISECOND, 0);
		return c.getTime();
	}

    /**
     * 清掉时间的分、秒
     *
     * @param d
     * @return
     */
    public static Date clearMinute(Date d) {
        Calendar c = Calendar.getInstance();
        c.setTime(d);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MILLISECOND, 0);
        return c.getTime();
    }

    /**
     * 清掉时间的秒
     *
     * @param d
     * @return
     */
    public static Date clearSecond(Date d) {
        Calendar c = Calendar.getInstance();
        c.setTime(d);
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MILLISECOND, 0);
        return c.getTime();
    }

    /**
     * 得到日期所在当天的最后一个时刻
     */
    public static Date getDateLastMoment(Date d) {
        Calendar c = Calendar.getInstance();
        c.setTime(d);
        c.set(Calendar.HOUR_OF_DAY, 23);
        c.set(Calendar.MINUTE, 59);
        c.set(Calendar.SECOND, 59);
        return c.getTime();
    }
	
	/**
	 * @description 获得一个比当前时间提前N小时的时间
	 * @author Yaojiawei
	 * @date 2014-08-27
	 * */
	public static Date getPreNDate(Date d, int N)
	{
		Calendar c = Calendar.getInstance();
		c.setTime(d);
		c.set(Calendar.HOUR_OF_DAY, c.get(Calendar.HOUR_OF_DAY)-N);
		return new Date(c.getTimeInMillis());
	}
	
	/**
	 * @description 计算两个时间之间差多少分钟
	 * @author Yaojiawei
	 * @date 2014-08-27
	 * */
	public static int getMinBetweenTime(Date startTime, Date endTime)
	{
		long cha = endTime.getTime() - startTime.getTime();
		double result = new BigDecimal(cha).divide(new BigDecimal(1000 * 60 ), 1, BigDecimal.ROUND_DOWN).doubleValue();	
		return Integer.parseInt(String.format("%.0f", result));
	}
	
	/**
	 * @description 计算出比当前时间前N分钟的时间
	 * @author Yaojiawei
	 * @date 2014-08-29
	 * */
	public static Date getDateMinusMinute(Date d,int minute)
	{
		Calendar c = Calendar.getInstance();
		c.setTime(d);
		c.add(Calendar.MINUTE,(0- minute));
		return c.getTime();
	}
	
	/**
	 * 取上个月并格式化
	 * 
	 * @return
	 */
	public static String getLastMonthFormat() {
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.MONTH, -1);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
		return sdf.format(cal.getTime());
	}
	
	/**
	 * 得到月的最后一天
	 * 
	 * @param d
	 * @return
	 */
	public static Date getMonthLastDay(Date d) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(d);

		calendar.add(Calendar.MONTH, 1);
		calendar.set(Calendar.DATE, 1);
		calendar.add(Calendar.DATE, -1);
		calendar.set(Calendar.HOUR_OF_DAY, 23);
		calendar.set(Calendar.MINUTE, 59);
		return calendar.getTime();
	}
	
	/**
	 * 获取当前年的年数
	 * 
	 * @return
	 */
	public static int getCurrentYear() {
		Calendar calendar = Calendar.getInstance();
		return calendar.get(Calendar.YEAR);
	}

    /**
     * 获取时间的年份、月份、日
     *
     * @return
     */
    public static int getYear(Date d) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(d);
        return calendar.get(Calendar.YEAR);
    }
    public static int getMonth(Date d) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(d);
        return calendar.get(Calendar.MONTH) + 1;
    }
    public static int getDay(Date d) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(d);
        return calendar.get(Calendar.DATE);
    }
	
	/**
	 * 获取给定日期的calendar实例
	 * @param date 给定的日期
	 * @return
	 */
	private static Calendar getCalendar(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		return cal;
	}
	
	/**
	 * 获取两时间之间相差月数
	 * @param beginTime 开始日期
	 * @param endTime   结束日期
	 * @return
	 */
	public static int getBetweenMonthes(Date beginTime, Date endTime) {
		Calendar cal1 = getCalendar(beginTime);
		Calendar cal2 = getCalendar(endTime);
		return (cal2.get(Calendar.YEAR) - cal1.get(Calendar.YEAR)) * 12 + cal2.get(Calendar.MONTH) - cal1.get(Calendar.MONTH);
	}
	
	/**
	 * 获取上个月最后一天日期字符串
	 * 
	 * @return 上个月最后一天日期
	 */
	public static Date getPreMonthLastDay(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.set(Calendar.DAY_OF_MONTH, 1);
		calendar.add(Calendar.DAY_OF_MONTH, -1);
		calendar.set(Calendar.HOUR_OF_DAY, 23);
		calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
		return calendar.getTime();
	}
	
	/**
	 * 得到月的天数
	 * 
	 * @param date
	 * @return
	 */
	public static int getMonthDays(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		return calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
	}
	
	public static String getLastMonth(Date d) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(d);
		cal.add(Calendar.MONTH, -1);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
		return sdf.format(cal.getTime());
	}

    /**
     * 得到日期的小时（12小时制）
     * @return
     */
    public static int getHourFor12(Date d){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(d);
        return calendar.get(Calendar.HOUR);
    }

    /**
     * 得到日期的小时（24小时制）
     * @return
     */
    public static int getHourFor24(Date d){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(d);
        return calendar.get(Calendar.HOUR_OF_DAY);
    }
    
	/**
	 * 判断日期是否是当前的月份
	 * 
	 * @param d
	 * @return
	 */
	public static boolean isCurrentMonth(Date d) {
		Calendar c1 = Calendar.getInstance();
		c1.setTime(d);
		Calendar c2 = Calendar.getInstance();

		if (c1.get(Calendar.MONTH) == c2.get(Calendar.MONTH))
			return true;

		return false;
	}
	
	/**
	 * 获得下个月第一天日期
	 * 
	 * @param d
	 * @return
	 */
	public static Date getNextMonthFirstDay(Date d) {
		Calendar c = Calendar.getInstance();
		c.setTime(d);
		c.set(Calendar.DATE, 1);
		c.add(Calendar.MONTH, 1);
		c.set(Calendar.HOUR_OF_DAY, 0);
		c.set(Calendar.MINUTE, 0);
		c.set(Calendar.SECOND, 0);
		c.set(Calendar.MILLISECOND, 0);
		c.set(Calendar.MILLISECOND, 0);
		return c.getTime();
	}
	
	/**
	 * 给定一个日期和天数，获得这个相加后的日期
	 * 
	 * @param d
	 * @param iday
	 * @return
	 */
	public static Date addDateDay(Date d, int iday) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(d);
		cal.add(Calendar.DAY_OF_MONTH, iday);
		return cal.getTime();
	}
	
	/**
	 * 设置日的最后时间点
	 * @param d
	 * @return
	 */
	public static Date setDateEnd(Date d) {
		Calendar c = Calendar.getInstance();
		c.setTime(d);
		c.set(Calendar.HOUR_OF_DAY, 23);
		c.set(Calendar.MINUTE, 59);
		c.set(Calendar.SECOND, 59);
		
		return c.getTime();
	}
	
	/**
	 * 设置到指定月份
	 * 
	 * @param month
	 * @return
	 */
	public static Date setDateMonth(int month) {
		Calendar c = Calendar.getInstance();
		c.set(Calendar.DAY_OF_MONTH, 1);
		c.set(Calendar.MONTH, month - 1);
		return c.getTime();
	}
	
	/**
	 * 获取日期的月份
	 * 
	 * @param d
	 * @return
	 */
	public static int getDateMonth(Date d) {
		Calendar c = Calendar.getInstance();
		c.setTime(d);
		return (c.get(Calendar.MONTH) + 1);
	}
	
	/**
	 * 得到该日期所属的季度
	 * @param d
	 * @return
	 */
	public static int getQuartar(Date d){
		Calendar cal = Calendar.getInstance();
		cal.setTime(d);
	    int month = cal.get(Calendar.MONTH) + 1;
	    int quarter = 0;
	    if(month%3==0){
	    	quarter = month/3;
	    }else {
	    	quarter = month/3+1;
	    }
	    return quarter;
	}
	
	/**
	 * @description 计算出比当前时间前N秒钟的时间
	 * @author yaojiawei
	 * @date 2018-06-07
	 * @param second 秒
	 * @param d 日期
	 * @return Date
	 * */
	public static Date getDateMinusSeconde(Date d,int second)
	{
		Calendar c = Calendar.getInstance();
		c.setTime(d);
		c.add(Calendar.SECOND,(0- second));
		return c.getTime();
	}
	
	
	/**
	 * 获取上个月最后一天日期字符串
	 * 只有日期,时,分,秒都是0的日期
	 * @return 上个月最后一天日期
	 */
	public static Date getPreMonthLastDay2(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.set(Calendar.DAY_OF_MONTH, 1);
		calendar.add(Calendar.DAY_OF_MONTH, -1);
//		calendar.set(Calendar.HOUR_OF_DAY, 00);
//		calendar.set(Calendar.MINUTE, 00);
//		calendar.set(Calendar.SECOND, 00);
		return calendar.getTime();
	}
	
}
