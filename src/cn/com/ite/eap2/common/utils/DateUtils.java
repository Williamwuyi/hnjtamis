package cn.com.ite.eap2.common.utils;

import java.text.SimpleDateFormat;
import java.text.ParseException;
import java.util.*;

/**
 * <p>Title:com.ite.oxhide.common.util.DateUtils</p>
 * <p>Description:日期时间工具类</p>
 * <p>Copyright: Copyright (c) 2007</p>
 * <p>Company: ITE</p>
 *
 * @author 宋文科
 * @version 1.0
 * @date 2007-6-4
 * @modify
 * @date
 */
public class DateUtils {

	/**
     * 获得本月的第一天的日期
     * @return 日期
     */
    public static Date getCurrMonthFirstDay() {
        Calendar cal = Calendar.getInstance();
        String s = (getYear(cal)) + "-" + (getMonth(cal)) + "-01";
        return convertStrToDate(s, "yyyy-MM-dd");
    }

    /**
     * 获得本月的最后一天的日期
     * @return 日期
     */
    public static Date getCurrMonthLastDay() {
        Calendar cal = Calendar.getInstance();
        String s = (getYear(cal)) + "-" + (getMonth(cal)) + "-" + getDays(cal);
        return convertStrToDate(s, "yyyy-MM-dd");
    }

    /**
     * 获得指定月的第一天的日期
     * @param cal 日历
     * @return 日期
     */
    public static Date getCurrMonthFirstDay(Calendar cal) {
        String s = (getYear(cal)) + "-" + (getMonth(cal)) + "-01";
        return convertStrToDate(s, "yyyy-MM-dd");
    }

    /**
     * 获得指定月的最后一天的日期
     * @param cal 日历
     * @return 日期
     */
    public static Date getCurrMonthLastDay(Calendar cal) {
        String s = (getYear(cal)) + "-" + (getMonth(cal)) + "-" + getDays(cal);
        return convertStrToDate(s, "yyyy-MM-dd");
    }

    /**
     * 获得给定日历的年
     * @param cal 日历
     * @return 年
     */
    public static int getYear(Calendar cal) {
        return cal.get(Calendar.YEAR);
    }

    /**
     * 获得给定日期的年
     * @param date 日期
     * @return 年
     */
    public static int getYear(Date date) {
        return convertDateToCal(date).get(Calendar.YEAR);
    }

    /**
     * 获得给定日期字符串对应的年
     * @param date_str 日期
     * @param type     格式
     * @return 年
     */
    public static int getYear(String date_str, String type) {
        return (convertStrToCal(date_str, type).get(Calendar.YEAR));
    }

    /**
     * 获得给定日历的月
     * @param cal 日历
     * @return 月
     */
    public static int getMonth(Calendar cal) {
        return (cal.get(Calendar.MONTH) + 1);
    }

    /**
     * 获得给定日期字符串对应的月
     * @param date 日期
     * @return 月
     */
    public static int getMonth(Date date) {
        return (convertDateToCal(date).get(Calendar.MONTH) + 1);
    }

    /**
     * 获得给定日期字符串对应的月  英语简称
     * @param date 日期
     * @return 月
     */
    public static String getMonth_en(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("MMM", Locale.US);
        return sdf.format(date);
    }

    /**
     * 获得给定日期字符串对应的月  英语简称
     * @param datestr 日期
     * @return 月
     */
    public static String getMonth_en(String datestr) {
        Date date = convertStrToDate(datestr, "yyyy-MM");
        SimpleDateFormat sdf = new SimpleDateFormat("MMM", Locale.US);
        return sdf.format(date);
    }

    /**
     * 获得给定日期字符串对应的月
     * @param date_str 日期
     * @param type     类型
     * @return 月
     */
    public static int getMonth(String date_str, String type) {
        return (convertStrToCal(date_str, type).get(Calendar.MONTH) + 1);
    }

    /**
     * 获得给定日期的当天
     * @param cal 日历
     * @return 天数
     */
    public static int getDay(Calendar cal) {
        return (cal.get(Calendar.DAY_OF_MONTH));
    }

    /**
     * 获得给定日期的当天
     * @param date 日期
     * @return 天数
     */
    public static int getDay(Date date) {
        return (convertDateToCal(date).get(Calendar.DAY_OF_MONTH));
    }

    /**
     * 获得给定日期的当天
     * @param date_str 字符串日期
     * @param type     类型
     * @return 天数
     */
    public static int getDay(String date_str, String type) {
        return (convertStrToCal(date_str, type).get(Calendar.DAY_OF_MONTH));
    }

    /**
     * 获得给定日期当月的天数
     * @param cal 日历
     * @return 天数
     */
    public static int getDays(Calendar cal) {
        return cal.getActualMaximum(Calendar.DAY_OF_MONTH);
    }

    /**
     * 获得给定日期当月的天数
     * @param date 日期
     * @return 天数
     */
    public static int getDays(Date date) {
        return (convertDateToCal(date).getActualMaximum(Calendar.DAY_OF_MONTH));
    }

    /**
     * 获得给定日期当月的天数
     * @param date_str 字符串日期
     * @param type     格式
     * @return 天数
     */
    public static int getDays(String date_str, String type) {
        return (convertStrToCal(date_str, type)
                .getActualMaximum(Calendar.DAY_OF_MONTH));
    }

    /**
     * 获得给定日期当月的天数
     * @param date_str 字符串日期
     * @return 天数
     */
    public static int getDays(String date_str) {
        return (convertStrToCal(date_str, "MM")
                .getActualMaximum(Calendar.DAY_OF_MONTH));
    }

    /**
     * 获得一个月的最后一天    2008-10-31
     * @param date_str 字符串日期
     * @return 天数
     */
    public static String getDaysString(String date_str, String type) {
        Date date = convertStrToDate(date_str, type);
        int year = getYear(date);
        int month = getMonth(date);
        int days = getDays(year, month);
        String days_result = year + "-" + month + "-" + days;
        return days_result;
    }

    /**
     * 获得一个月的最后一天    2008-10-31
     * @param date 日期
     * @return 天数
     */
    public static String getDaysString(Date date) {
        int year = getYear(date);
        int month = getMonth(date);
        int days = getDays(year, month);
        String days_result = year + "-" + month + "-" + days;
        return days_result;
    }

    /**
     *  获得指定年月的天数
     * @param year  指定年
     * @param month 指定月份
     * @return 返回指定年月的月末是几号。
     */
    private static int getDays(int year, int month) {
        int strEndDay[] = {31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};
        if ((year % 4 == 0) && ((year % 100 != 0) || (year % 400 == 0))) {
            if (month == 2) strEndDay[1] = 29;
        }
        return strEndDay[month - 1];
    }

    /**
     * 获得当前日期
     * @return 日期
     */
    public static Date getCurrDate() {
        Calendar cal = Calendar.getInstance();
        return cal.getTime();

    }

    /**
     * 获得当前年
     * @return 年
     */
    public static int getCurrYear() {
        Calendar cal = Calendar.getInstance();
        return cal.get(Calendar.YEAR);
    }

    /**
     * 获得当前月
     * @return 月
     */
    public static int getCurrMonth() {
        Calendar cal = Calendar.getInstance();
        return (cal.get(Calendar.MONTH) + 1);
    }

    /**
     * 获得当前天
     * @return 天数
     */
    public static int getCurrDay() {
        Calendar cal = Calendar.getInstance();
        return (cal.get(Calendar.DAY_OF_MONTH));
    }

    /**
     * 日期转换字符(动态格式转换)
     * @param date 日期
     * @param type 类型
     * @return 字符串时间
     */
    public static String convertDateToStr(Date date, String type) {
        SimpleDateFormat dateformat = new SimpleDateFormat(type);
        if (date == null)
            return "";
        return dateformat.format(date);
    }

    /**
     * 字符转换日期(动态格式转换)
     * @param date_str 字符串时间
     * @param type     类型
     * @return 时间
     */
    public static Date convertStrToDate(String date_str, String type) {
        SimpleDateFormat dateformat = new SimpleDateFormat(type);
        if(date_str==null||date_str.equals(""))
        	return null;
        try {
            return dateformat.parse(date_str);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 字符转换日历(动态格式转换)
     * @param date_str 字符串式日期
     * @param type     类型
     * @return 日历
     */
    public static Calendar convertStrToCal(String date_str, String type) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(convertStrToDate(date_str, type));
        return cal;
    }

    /**
     * 日期转日历
     * @param date 日期
     * @return 日历
     */
    public static Calendar convertDateToCal(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return cal;
    }


    /**
     * 判断日期格式是否正确
     * @param date_str 字符串时间
     * @param type     格式字符串
     * @return 是否正确
     */
    public static boolean isDate(String date_str, String type) {
        SimpleDateFormat dateformat = new SimpleDateFormat(type);
        try {
            dateformat.parse(date_str);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 获得在此时间后几天的时间
     * @param amount 天数
     * @param type   类型
     * @return 时间
     */
    public Date getCurDateAddDate(int amount, int type) {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, amount);
        return cal.getTime();
    }

    /**
     * 获得指定时间后几天的时间   前3天可以使用-3表示
     * @param date   指定时间
     * @param type   类型
     * @param amount 天数
     * @return 时间
     */
    public static Date getDateAddDate(Date date, int type, int amount) {
        Calendar cal = Calendar.getInstance();
        cal.clear();
        cal.setTime(date);
        cal.add(Calendar.DATE, amount);
        return cal.getTime();
    }
    /**
     * 获得指定时间后几天的时间   前3天可以使用-3表示
     * @param String   指定时间
     * @param type   类型
     * @param amount 天数
     * @return 时间
     */
    public static String getDateAddDateString(String date, int type, int amount) {
    	SimpleDateFormat fSdf=new SimpleDateFormat("yyyy-MM-dd");
    	Date d=new Date();
    	try {
			d=fSdf.parse(date);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        Calendar cal = Calendar.getInstance();
        cal.clear();
        cal.setTime(d);
        cal.add(Calendar.DATE, amount);
        
    	//fSdf.format(d);
    	String str=fSdf.format(cal.getTime());
        return str;
    }
    /**
     * 获得指定时间后几天的时间   前3天可以使用-3表示
     * @param date   指定时间
     * @param amount 天数
     * @return 时间
     */
    public static Date getDateAddDate(Date date, int amount) {
        Calendar cal = Calendar.getInstance();
        cal.clear();
        cal.setTime(date);
        cal.add(Calendar.DATE, amount);
        return cal.getTime();
    }

    /**
     * 获得指定时间后几天的时间   前3天可以使用-3表示
     * @param date   指定时间
     * @param type   类型
     * @param amount 天数
     * @return 时间
     */
    public static Date getDateAddDate(String date, String type, int amount) {
        Calendar cal = Calendar.getInstance();
        cal.clear();
        cal.setTime(convertStrToDate(date, type));
        cal.add(Calendar.DATE, amount);
        return cal.getTime();
    }

    /**
     * 获得指定时间后几天的时间   前3天可以使用-3表示
     * @param date   指定时间
     * @param type   类型如"yyyy-MM-dd"
     * @param amount 天数
     * @return 时间
     */
    public static String getDateAddDay(String date, String type, int amount) {
        Calendar cal = Calendar.getInstance();
        cal.clear();
        cal.setTime(convertStrToDate(date, type));
        cal.add(Calendar.DATE, amount);
        return convertDateToStr(cal.getTime(), type);
    }

    /**
     * 计算相隔天数的方法
     * @param d1
     * @param d2
     * @return
     */
    public static int getDaysBetween(Calendar d1, Calendar d2) {
        if (d1.after(d2)) {  // 时间大小转换
            java.util.Calendar swap = d1;
            d1 = d2;
            d2 = swap;
        }
        int days = d2.get(Calendar.DAY_OF_YEAR) - d1.get(Calendar.DAY_OF_YEAR);
        int y2 = d2.get(Calendar.YEAR);
        if (d1.get(Calendar.YEAR) != y2) {
            d1 = (Calendar) d1.clone();
            do {
                days += d1.getActualMaximum(Calendar.DAY_OF_YEAR);//得到当年的实际天数
                d1.add(Calendar.YEAR, 1);
            } while (d1.get(Calendar.YEAR) != y2);
        }
        return days;
    }

    /**
     * 计算相隔天数的方法
     * @param date1
     * @param date2
     * @return
     */
    public static int getDaysBetween(Date date1, Date date2) {
        Calendar d1 = Calendar.getInstance();
        Calendar d2 = Calendar.getInstance();
        d1.setTime(date1);
        d2.setTime(date2);
        return getDaysBetween(d1, d2);
    }

    /**
     * 遍历两日期的日期列表
     * @param startDate
     * @param endDate
     * @return
     */
    public static List<String> getDaysBetweenList(Date startDate, Date endDate) {
        List<String> list = new ArrayList<String>();
        Calendar startTime = Calendar.getInstance();
        startTime.clear();
        startTime.setTime(startDate);
        int startYear = startTime.get(Calendar.YEAR);
        int startMonth = startTime.get(Calendar.MONTH);
        int startDay = startTime.get(Calendar.DAY_OF_MONTH);

        Calendar endTime = Calendar.getInstance();
        endTime.clear();
        endTime.setTime(endDate);
        int endYear = endTime.get(Calendar.YEAR);
        int endMonth = endTime.get(Calendar.MONTH);
        int endDay = endTime.get(Calendar.DAY_OF_MONTH);
        //System.out.println("注意西方的月份从0到11，中国的月份从1到12");
        //System.out.println("下面输入的是中国的日期.注意其中的转换问题");
        //System.out.println("start date : " + startDate);
        //System.out.println("end date : " + endDate);

        int count = 0;
        for (int x = startYear; x <= endYear; x++) {
            //罗马历法产生年份公元1582年
            int gregorianCutoverYear = 1582;
            boolean isLeapYear = x >= gregorianCutoverYear ?
                    ((x % 4 == 0) && ((x % 100 != 0) || (x % 400 == 0))) :
                    (x % 4 == 0);
            //判断是否是闰年
            //java方法
            //boolean isLeapYear = (new GregorianCalendar()).isLeapYear(x);
            @SuppressWarnings("unused")
			String isBigYear = "是平年";
            if (isLeapYear) {
                isBigYear = "是闰年";
            }
            //System.out.println(x + "年" + isBigYear);
            //获取开始月的最大天数
            //java方法
            //SimpleDateFormat aFormat=new SimpleDateFormat("yyyy-MM-dd");
            //Date date = aFormat.parse(start);
            //Calendar time = Calendar.getInstance();
            //time.clear();
            //time.setTime(date);
            //int max=time.getActualMaximum(Calendar.DAY_OF_MONTH); //本月份的天数
            //System.out.println(max);

            //获取开始月的最大天数；大月是1，3，5，7，8，10，12；小月是4，6，9，11；特殊月是2
            int max = 0;
            if (startMonth == 1) {
                if (isLeapYear) {
                    max = 29;
                }
                if (!isLeapYear) {
                    max = 28;
                }
            }
            if (startMonth == 3 || startMonth == 5 || startMonth == 8 || startMonth == 10) {
                max = 30;
            }
            if (startMonth == 0 || startMonth == 2 || startMonth == 4 || startMonth == 6 || startMonth == 7 || startMonth == 9 || startMonth == 11) {
                max = 31;
            }

            //循环每个月
            //如果在日期范围内月份循环时自增到了一年的最后一个月就将月份初始化到一月份
            int y = 0;
            //如果是开始日期的第一个年的月数就从开始月数循环
            if (x == startYear) {
                y = startMonth;
            }
            for (; y < 12; y++) {
                //获取当月的最大天数；大月是1，3，5，7，8，10，12；小月是4，6，9，11；特殊月是2
                max = 0;
                if (y == 1) {
                    if (isLeapYear) {
                        max = 29;
                    }
                    if (!isLeapYear) {
                        max = 28;
                    }
                }
                if (y == 3 || y == 5 || y == 8 || y == 10) {
                    max = 30;
                }
                if (y == 0 || y == 2 || y == 4 || y == 6 || y == 7 || y == 9 || y == 11) {
                    max = 31;
                }
                int ty = y + 1;
                //System.out.println(x + "年" + ty + "月");
                //循环每一天
                int z = 1;
                //如果是开始日期的第一个月的天数就从开始天数循环
                if (x == startYear && y == startMonth) {
                    z = startDay;
                }
                for (; z <= max; z++) {
                    count++;
                    //System.out.println(x + "年" + ty + "月" + z + "日");
                    list.add(x + "-" + ty + "-" + z);
                    if (x == endYear && y == endMonth && z == endDay) {
                        break;
                    }
                }
                //如果已经遍历过了截至日期的最后月份就中止月份的循环
                if (x == endYear && y == endMonth) {
                    break;
                }
            }
        }
        //System.out.println(startDate + " 到 " + endDate + " 的天数差：" + count);
        return list;
    }

    /**
     * 遍历两日期的日期列表
     * @param startDate
     * @param endDate
     * @return
     */
    public static List<Date> getDaysBetweenDateList(Date startDate, Date endDate) {
        List<Date> list = new ArrayList<Date>();
        Calendar startTime = Calendar.getInstance();
        startTime.clear();
        startTime.setTime(startDate);
        int startYear = startTime.get(Calendar.YEAR);
        int startMonth = startTime.get(Calendar.MONTH);
        int startDay = startTime.get(Calendar.DAY_OF_MONTH);

        Calendar endTime = Calendar.getInstance();
        endTime.clear();
        endTime.setTime(endDate);
        int endYear = endTime.get(Calendar.YEAR);
        int endMonth = endTime.get(Calendar.MONTH);
        int endDay = endTime.get(Calendar.DAY_OF_MONTH);
        //System.out.println("注意西方的月份从0到11，中国的月份从1到12");
        //System.out.println("下面输入的是中国的日期.注意其中的转换问题");
        //System.out.println("start date : " + startDate);
        //System.out.println("end date : " + endDate);

        int count = 0;
        for (int x = startYear; x <= endYear; x++) {
            //罗马历法产生年份公元1582年
            int gregorianCutoverYear = 1582;
            boolean isLeapYear = x >= gregorianCutoverYear ?
                    ((x % 4 == 0) && ((x % 100 != 0) || (x % 400 == 0))) :
                    (x % 4 == 0);
            //判断是否是闰年
            //java方法
            //boolean isLeapYear = (new GregorianCalendar()).isLeapYear(x);
            @SuppressWarnings("unused")
			String isBigYear = "是平年";
            if (isLeapYear) {
                isBigYear = "是闰年";
            }
            //System.out.println(x + "年" + isBigYear);
            //获取开始月的最大天数
            //java方法
            //SimpleDateFormat aFormat=new SimpleDateFormat("yyyy-MM-dd");
            //Date date = aFormat.parse(start);
            //Calendar time = Calendar.getInstance();
            //time.clear();
            //time.setTime(date);
            //int max=time.getActualMaximum(Calendar.DAY_OF_MONTH); //本月份的天数
            //System.out.println(max);

            //获取开始月的最大天数；大月是1，3，5，7，8，10，12；小月是4，6，9，11；特殊月是2
            int max = 0;
            if (startMonth == 1) {
                if (isLeapYear) {
                    max = 29;
                }
                if (!isLeapYear) {
                    max = 28;
                }
            }
            if (startMonth == 3 || startMonth == 5 || startMonth == 8 || startMonth == 10) {
                max = 30;
            }
            if (startMonth == 0 || startMonth == 2 || startMonth == 4 || startMonth == 6 || startMonth == 7 || startMonth == 9 || startMonth == 11) {
                max = 31;
            }

            //循环每个月
            //如果在日期范围内月份循环时自增到了一年的最后一个月就将月份初始化到一月份
            int y = 0;
            //如果是开始日期的第一个年的月数就从开始月数循环
            if (x == startYear) {
                y = startMonth;
            }
            for (; y < 12; y++) {
                //获取当月的最大天数；大月是1，3，5，7，8，10，12；小月是4，6，9，11；特殊月是2
                max = 0;
                if (y == 1) {
                    if (isLeapYear) {
                        max = 29;
                    }
                    if (!isLeapYear) {
                        max = 28;
                    }
                }
                if (y == 3 || y == 5 || y == 8 || y == 10) {
                    max = 30;
                }
                if (y == 0 || y == 2 || y == 4 || y == 6 || y == 7 || y == 9 || y == 11) {
                    max = 31;
                }
                int ty = y + 1;
                //System.out.println(x + "年" + ty + "月");
                //循环每一天
                int z = 1;
                //如果是开始日期的第一个月的天数就从开始天数循环
                if (x == startYear && y == startMonth) {
                    z = startDay;
                }
                for (; z <= max; z++) {
                    count++;
                    //System.out.println(x + "年" + ty + "月" + z + "日");
                    //字符串转换成日期
                    String str_temp = x + "-" + ty + "-" + z;
                    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                    Date date_temp = null;
                    try {
                        date_temp = format.parse(str_temp);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    list.add(date_temp);
                    if (x == endYear && y == endMonth && z == endDay) {
                        break;
                    }
                }
                //如果已经遍历过了截至日期的最后月份就中止月份的循环
                if (x == endYear && y == endMonth) {
                    break;
                }
            }
        }
        //System.out.println(startDate + " 到 " + endDate + " 的天数差：" + count);
        return list;
    }


    /**
     * 遍历两日期的日期列表
     * @param startDate
     * @param endDate
     * @return
     */
    public static List<String> getDaysBetweenList_simple(Date startDate, Date endDate) {
        List<String> list = new ArrayList<String>();
        Long startM = startDate.getTime();
        Long endM = endDate.getTime();
        long result = (endM - startM) / (24 * 60 * 60 * 1000);
        //System.out.println("差:" + result + "天");

        Calendar startTime = Calendar.getInstance();
        startTime.clear();
        startTime.setTime(startDate);
        for (int i = 0; i < (int) result; i++) {
            String str_temp = startTime.get(Calendar.YEAR) + "-"
                    + (startTime.get(Calendar.MONTH) + 1) + "-"
                    + startTime.get(Calendar.DAY_OF_MONTH);
            list.add(str_temp);
            startTime.add(Calendar.DAY_OF_YEAR, 1);
        }
        //System.out.println(startDate + " 到 " + endDate + " 的天数差：" + count);
        return list;
    }

    /**
     * 遍历两日期的日期列表
     * @param startDate
     * @param endDate
     * @return
     */
    public static List<Date> getDaysBetweenDateList_simple(Date startDate, Date endDate) {
        List<Date> list = new ArrayList<Date>();
        Long startM = startDate.getTime();
        Long endM = endDate.getTime();
        long result = (endM - startM) / (24 * 60 * 60 * 1000);
        //System.out.println("差:" + result + "天");

        Calendar startTime = Calendar.getInstance();
        startTime.clear();
        startTime.setTime(startDate);
        for (int i = 0; i < (int) result; i++) {
            String str_temp = startTime.get(Calendar.YEAR) + "-"
                    + (startTime.get(Calendar.MONTH) + 1) + "-"
                    + startTime.get(Calendar.DAY_OF_MONTH);
            //System.out.println(str_temp);
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            Date date_temp = null;
            try {
                date_temp = format.parse(str_temp);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            list.add(date_temp);
            startTime.add(Calendar.DAY_OF_YEAR, 1);
        }
        //System.out.println(startDate + " 到 " + endDate + " 的天数差：" + count);
        return list;
    }

    /**
     * 根据一个日期，返回是星期几的字符串 1==星期日,2,3,4,5,6,7
     * @param datestr
     * @return
     */
    public static String getWeek(String datestr) {
        // 再转换为时间
        Date date = convertStrToDate(datestr, "yyyy-MM-dd");
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        return new SimpleDateFormat("EEEE").format(c.getTime());
    }

    /**
     * 根据一个日期，返回是星期几的字符串 1==星期日,2,3,4,5,6,7
     * @param datestr
     * @return
     */
    public static String getWeek_en(String datestr) {
        // 再转换为时间
        Date date = convertStrToDate(datestr, "yyyy-MM-dd");
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        return new SimpleDateFormat("E", Locale.US).format(c.getTime());
    }

    /**
     * 根据一个日期，返回是星期几的字符串 1==星期日,2,3,4,5,6,7
     * @param date
     * @return
     */
    public static String getWeek(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        return new SimpleDateFormat("E").format(c.getTime());
    }

    /**
     * 根据一个日期，返回是星期几的字符串 1==星期日,2,3,4,5,6,7
     * @param date
     * @return
     */
    public static String getWeek_en(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        return new SimpleDateFormat("E", Locale.US).format(c.getTime());
    }
    /**
     * 根据一个日期，返回是星期几的字符串 1 2 3 4 5 6 7
     * @param date
     * @return
     */
    public static int getWeek2(String str) {
    	Calendar calendar = Calendar.getInstance();
    	// str="2015-05-28";
    	SimpleDateFormat sdfInput = new SimpleDateFormat("yyyy-MM-dd");
    	Date date = new Date();
    	try {
			date=sdfInput.parse(str);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	 calendar.setTime(date);
         int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
         //System.out.println(dayOfWeek-1);
        return dayOfWeek-1;
    }

    /**
     * 获得本季度 开始结束日期
     * @param dateinput
     * @return  开始日期|结束日期
     */
    public static String getThisSeasonTime(Date dateinput) {
        int month = getMonth(dateinput);
        int array[][] = {{1, 2, 3}, {4, 5, 6}, {7, 8, 9}, {10, 11, 12}};
        int season = 1;
        if (month >= 1 && month <= 3) {
            season = 1;
        }
        if (month >= 4 && month <= 6) {
            season = 2;
        }
        if (month >= 7 && month <= 9) {
            season = 3;
        }
        if (month >= 10 && month <= 12) {
            season = 4;
        }
        int start_month = array[season - 1][0];
        int end_month = array[season - 1][2];

        Date date = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy");//可以方便地修改日期格式
        String years = dateFormat.format(date);
        int years_value = Integer.parseInt(years);

        int start_days = 1;//years+"-"+String.valueOf(start_month)+"-1";//getLastDayOfMonth(years_value,start_month);
        int end_days = getDays(years_value, end_month);
        String seasonDate = years_value + "-" + start_month + "-" + start_days + "|" + years_value + "-" + end_month + "-" + end_days;
        return seasonDate;
    }
    /**
     * 给定日期所在周一
    * @Title: genMonday
    * @Description: TODO(这里用一句话描述这个方法的作用)
    * @param @param date    设定文件
    * @return void    返回类型
    * @throws
     */
    public static String getMonday(String date)
    {
     SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
     Date d = null;
     try
     {
      d = format.parse(date);
     }
     catch(Exception e)
     {
      e.printStackTrace();
     }
     Calendar cal = Calendar.getInstance();
     cal.setTime(d);
     cal.set(Calendar.DAY_OF_WEEK,Calendar.MONDAY);
     int a=getWeek2(date);
    // System.out.println(a);
     if(a==0){
    	 cal.add(Calendar.WEEK_OF_MONTH, -1);
     }
     return format.format(cal.getTime());
    }
    /**
     * 给定日期所在周日
    * @Title: genMonday
    * @Description: TODO(这里用一句话描述这个方法的作用)
    * @param @param date    设定文件
    * @return void    返回类型
    * @throws
     */
    public static String getSunday(String date)
    {
     SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
     Date d = null;
     try
     {
      d = format.parse(date);
     }
     catch(Exception e)
     {
      e.printStackTrace();
     }
     Calendar cal = Calendar.getInstance();
     cal.setTime(d);
     cal.add(Calendar.WEEK_OF_MONTH, 1);
     cal.set(Calendar.DAY_OF_WEEK,Calendar.SUNDAY);
     int a=getWeek2(date);
     // System.out.println(a);
      if(a==0){
     	 cal.add(Calendar.WEEK_OF_MONTH, -1);
      }
    // System.out.println(format.format(cal.getTime()));
     return format.format(cal.getTime());
    }
    /**
     * 根据给定日期获得-7天
    * @Title: main
    * @Description: TODO(这里用一句话描述这个方法的作用)
    * @param @param args    设定文件
    * @return void    返回类型
    * @throws
     */
    public static String befWeek(String date)
    {
    	SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
    	Date d = null;
        try
        {
         d = format.parse(date);
        }
        catch(Exception e)
        {
         e.printStackTrace();
        }
    	Calendar c = Calendar.getInstance();
    	c.setTime(d);
        // 减去一个星期
        c.add(Calendar.WEEK_OF_MONTH, -1);
        System.out.println(format.format(c.getTime()));
     return format.format(c.getTime());
    }
    /**
     * 一个月多少天
     * @param date
     * @return
     */
    public static int getMonthdays(String str) {
    	SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
    	Date d = null;
        try
        {
         d = format.parse(str);
        }
        catch(Exception e)
        {
         e.printStackTrace();
        }
    	int year=getYear(d);
    	int month=getMonth(d);
    	Calendar cal = Calendar.getInstance();  //2015-02
        cal.set(Calendar.YEAR, year);     
        cal.set(Calendar.MONTH, month-1);  
       // System.out.println(cal.getActualMaximum(Calendar.DATE));
        return cal.getActualMaximum(Calendar.DATE);
    }
    /**
     * 获取一个月第几周所有日期
     */
    private static final int size = 7;
    public static int[] getWeekDays(String str,int num){
    	SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
    	Date d = null;
        try
        {
         d = format.parse(str);
        }
        catch(Exception e)
        {
         e.printStackTrace();
        }
    	int year=getYear(d);
    	int month=getMonth(d);
    	int[] weekDays = new int[size];

    	Calendar calendar = Calendar.getInstance(Locale.CHINA);
    	calendar.set(Calendar.YEAR, year);     
    	calendar.set(Calendar.MONTH, month-1);  
    	calendar.set(Calendar.WEEK_OF_MONTH, num);

    	String day=getWeek(new Date());
    	int a=0;//当天是星期几
		if(null!=day&&day.equals("星期一")){
			a=-1;
		}else if(day.equals("星期二")){
			a=-2;
		}else if(day.equals("星期三")){
			a=-3;
		}else if(day.equals("星期四")){
			a=-4;
		}else if(day.equals("星期五")){
			a=-5;
		}else if(day.equals("星期六")){
			a=-6;
		}
    	for(int i = Calendar.SUNDAY; i <= Calendar.SATURDAY; i++){
    		if(i==Calendar.SUNDAY){
    			calendar.add(Calendar.DAY_OF_MONTH, a);
    		}
    		weekDays[i-1] = calendar.get(Calendar.DAY_OF_MONTH);
    		calendar.add(Calendar.DAY_OF_MONTH, 1);
    	}
    	return weekDays;
    	}
    /**
     * 一个月有几周
    * @Title: main
    * @Description: TODO(这里用一句话描述这个方法的作用)
    * @param @param args    设定文件
    * @return void    返回类型
    * @throws
     */
    public static int getweeknum(String str){
    	SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
    	Date d = null;
        try
        {
         d = format.parse(str);
        }
        catch(Exception e)
        {
         e.printStackTrace();
        }
    	int year=getYear(d);
    	int month=getMonth(d);
    	Calendar c = Calendar.getInstance(); 
        c.set(Calendar.YEAR, year); 
        c.set(Calendar.MONTH, month-1); 
    	return c.getActualMaximum(Calendar.WEEK_OF_MONTH);
    }
    
    public static void main(String args[])
    {
//    	int weekDays[]=getWeekDays("2015-04-02",1); 
//    	for(int i = 0; i < weekDays.length; i++){
//    		System.out.print(weekDays[i] + ", ");
//    		}
//System.out.println(getWeek2("2015-04-26"));
System.out.println(getSunday("2015-04-26"));
    }
}
