package common;

import tray.model.Week;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;

public class Time {
    public static final int MINUTES_DAY = 1440; // midnight = 24 * 60 = 1440
    public static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd(HH:mm)");
    public static SimpleDateFormat sdfTime = new SimpleDateFormat("HH:mm");
    public static SimpleDateFormat sdfShort = new SimpleDateFormat("yyyy-MM-dd");
    public static DecimalFormat df = new DecimalFormat("0.00");
    public static SimpleDateFormat dfDay = new SimpleDateFormat("EEEE");
    private static long timestamp;

    public static Date parseDate(String dateString) throws Exception {
        return sdfShort.parse(dateString);
    }

    public static Date parseStamp(String stamp) throws Exception {
        return sdf.parse(stamp);
    }

    public static String minutesFormatted(double number) {
        String s = "";
        if (number < 0) {
            s += "-";
            number *= -1;
        }
        int minutes = (int) number;
        int h = minutes / 60;
        int m = minutes % 60;
        s += String.format("%d:%02d", h, m);
        return s.equals("0:00") ? "" : s;
    }

    public static String minutesToHoursFormatted(double number) {
        return df.format(number / 60D);
    }

    public static String getStamp() {
        return sdf.format(getNow());
    }

    public static Boolean isToday(Date date) {
        Calendar now = GregorianCalendar.getInstance();
        Calendar other = GregorianCalendar.getInstance();
        other.setTime(date);
        return
                now.get(Calendar.DATE) == other.get(Calendar.DATE) &&
                        now.get(Calendar.MONTH) == other.get(Calendar.MONTH) &&
                        now.get(Calendar.YEAR) == other.get(Calendar.YEAR);
    }

    public static boolean isCurrent(Week week) {
        return getWeek().equals(week.getName());
    }

    public static Date getNow() {
        Calendar c = Calendar.getInstance();
//		c.set(2009,7,4); // Comment out to get the real time. Use for development and debugging
        return c.getTime();
    }

    public static String getWeek() {
        Calendar c = new GregorianCalendar(new Locale("da", "DK"));
        int week = c.get(Calendar.WEEK_OF_YEAR);
        int year = c.get(Calendar.YEAR);
        StringBuilder sb = new StringBuilder();
        Formatter formatter = new Formatter(sb);
        formatter.format("%04d-%02d", year, week);
        return sb.toString();
    }

    public static int getMinutes(Date date) {
        int result = MINUTES_DAY;
        if (isToday(date)) {
            Calendar c = new GregorianCalendar(new Locale("da", "DK"));
            int min = c.get(Calendar.MINUTE);
            int hou = c.get(Calendar.HOUR_OF_DAY);
            result = hou * 60 + min;
        }

        return result;
    }

    public static String toString(Date date) {
        return sdfShort.format(date);
    }

    public String toString() {
        return sdfTime.format(new Date());
    }

    public static void start() {
        timestamp = System.currentTimeMillis();
    }

    public static void stop(String label) {
        Long diff = System.currentTimeMillis() - timestamp;
        Log.log(label + " took: " + diff + " millis.");
    }

    public static Calendar getMondayCalendarFromYearWeek(String yearWeek) {
        int year = Integer.parseInt(yearWeek.substring(0, 4), 10);
        int week = Integer.parseInt(yearWeek.substring(5), 10);
        Calendar calendar = Calendar.getInstance();
        calendar.clear();
        calendar.set(Calendar.WEEK_OF_YEAR, week);
        calendar.set(Calendar.YEAR, year);
        return calendar;
    }

    public static Calendar getFridayCalendarFromYearWeek(String yearWeek) {
        int year = Integer.parseInt(yearWeek.substring(0, 4), 10);
        int week = Integer.parseInt(yearWeek.substring(5), 10);
        Calendar calendar = Calendar.getInstance();
        calendar.clear();
        calendar.set(Calendar.WEEK_OF_YEAR, week);
        calendar.set(Calendar.YEAR, year);
        return calendar;
    }
}

