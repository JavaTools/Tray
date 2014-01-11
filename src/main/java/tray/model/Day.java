package tray.model;

import common.Time;

import java.util.Date;

/**
 * This class represents a day in the stamp statistics.
 * <p/>
 * span  = the total minutes worked this day
 * ratio = the calculated ratio, that must be worked to balance the week
 * This variable only gets set when the day does not have any recorded stamps.
 * This can occur when the day is in the future or in case of absence.
 * The variable gets set by the week class when the stamp-file is fully
 * processed.
 * <p/>
 * The getSpan method returns the total span in minutes of this day. The method
 * is dynamic in the sense that it calculates the total span on the fly in two
 * cases:
 * 1) No stop was detected since the last start && today is NOT the day
 * 2) No stop was detected since the last start && today IS the day
 * States:
 * 0: Balanced (either no stamps at all or some, latest a stop)
 * 1: Skew     (Maybe some pairs, but latest s start)
 */
public class Day implements Comparable {
    public static int MINUTES_PER_DAY = 474;
    private Date date;
    private String name;

    private int span = 0;
    private int ratio = 0;
    private int accumulatedDiff = 0;

    private int start = -1;

    public Day(String name) {
        this.name = name;
    }

    public void setDate(Date date) {
        this.date = date;
        //this.name = Config.getInstance().getDay(date);
        //TODO Check that the day match this Day's name!!!
    }

    public void setRatio(int ratio) {
        this.ratio = ratio;
    }

    public String getName() {
        return name;
    }

    public Date getDate() {
        return date;
    }

    public int getAccumulatedDiff() {
        return accumulatedDiff;
    }

    public void setAccumulatedDiff(int accumulatedDiff) {
        this.accumulatedDiff = accumulatedDiff;
    }

    public Boolean isFuture() {
        Boolean result = false;
        try {
            result = date == null || (Time.getNow().compareTo(date) < 0);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return result;
    }

    public Boolean isWeekend() {
        return "Lørdag".equals(name) || "Søndag".equals(name);
    }

    public int getDiff() {
        if (isFuture())
            return ratio - MINUTES_PER_DAY;
        else if (!isWeekend())
            return getSpan() - MINUTES_PER_DAY;
        else
            return 0;
    }

    public int getSpan() {
        if (isset(start) && Time.isToday(date))
            return span + (Time.getMinutes(date) - start);
        else
            return span;
    }

    public int getDisplaySpan() {
        if (isFuture())
            return ratio;
        else
            return getSpan();
    }

    public void start(int minutes) {
        if (!isset(start)) {
            start = minutes;
        }
    }

    public void stop(int minutes) {
        if (isset(start)) {
            span += minutes - start;
            start = -1;
        } else {
            span += minutes;
        }
    }

    @Override
    public String toString() {
        double amount = (double) getSpan();
        return "Day(name: '" + name + "', date: " + Time.toString(date) + ", hours: " + Time.minutesToHoursFormatted(amount) + "')";
    }

    public int compareTo(Object object) {
        Day other = (Day) object;
        return getDate().compareTo(other.getDate());
    }

    private boolean isset(int minutes) {
        return minutes > -1;
    }
}
