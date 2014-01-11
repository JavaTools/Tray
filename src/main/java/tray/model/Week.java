package tray.model;

import common.Time;

import java.util.*;

public class Week {

    private static int MINUTES_PER_DAY = 474;
    private static int MINUTES_PER_WEEK = MINUTES_PER_DAY * 5;

    private String name;
    private Day[] days;
    private Calendar c = new GregorianCalendar(new Locale("da", "DK"));
    private int actualSpan, actualDiff;
    private int simulatedSpan, simulatedDiff;
    private int dayCount = 0;

    public Week(String name, ArrayList<String> stamps) {
        days = new Day[7];
        days[0] = new Day("Mandag");
        days[1] = new Day("Tirsdag");
        days[2] = new Day("Onsdag");
        days[3] = new Day("Torsdag");
        days[4] = new Day("Fredag");
        days[5] = new Day("Lørdag");
        days[6] = new Day("Søndag");
        this.name = name;
        initializeDays();
        parse(stamps);
    }

    private void initializeDays() {
        Calendar calendar = Time.getMondayCalendarFromYearWeek(name);
        for (int index = 0; index < 7; index++) {
            Day day = days[index];
            day.setDate(calendar.getTime());
            calendar.add(Calendar.HOUR, 25); //TODO Add 25 to prevent daylight savings problems - is it working?
        }
    }

    public int getDayCount() {
        return dayCount;
    }

    public String getName() {
        return name;
    }

    public Day getDay(int index) {
        if (index >= 0 && index < days.length)
            return days[index];
        else
            return null;
    }

    /**
     * @return span of hours worked, measured in minutes.
     */
    public double getSpan() {
        return actualSpan;
    }

    public int getActualSpan() {
        return actualSpan;
    }

    public int getActualDiff() {
        return actualDiff;
    }

    /**
     * Parses the list of stamps in order to build this weeks span and
     * work ratio for the remaining days
     *
     * @param stamps
     */
    private void parse(ArrayList<String> stamps) {

        for (String stamp : stamps) {
            try {
                append(stamp);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    /**
     * @param line
     * @throws Exception
     */
    public void append(String line) throws Exception {
        if (line != null && line.contains(";")) {
            String[] parts = line.split(";");
            Day day = days[getDayIndex(Time.parseStamp(parts[1]))];
            //day.setDate(parts[1].substring(0,10));
            int hrs = c.get(Calendar.HOUR_OF_DAY);
            int min = c.get(Calendar.MINUTE);
            int minutes = hrs * 60 + min;
            if (parts[0].equals("start"))
                day.start(minutes);
            else
                day.stop(minutes);
        }
    }


    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (Day day : days) {
            sb.append(day.toString()).append("\n");
        }
        return sb.toString();
    }

    /**
     * @param date
     * @return
     * @throws Exception Calendar.MONDAY    = 2 -> 0
     *                   Calendar.TUESDAY   = 3 -> 1
     *                   Calendar.WEDNESDAY = 4 -> 2
     *                   Calendar.THURSDAY  = 5 -> 3
     *                   Calendar.FRIDAY    = 6 -> 4
     *                   Calendar.SATURDAY  = 7 -> 5
     *                   Calendar.SUNDAY    = 1 -> 6
     */
    private int getDayIndex(Date date) throws Exception {
        c.setTime(date);
        int iday = c.get(Calendar.DAY_OF_WEEK);
        iday = iday == 1 ? 6 : iday - 2;
        return iday;
    }

    public void calculateValues() {

        int futures = 0;
        actualSpan = 0;
        actualDiff = 0;
        dayCount = 5;

        // -------------------------------------------
        // First iteration, calculate ratio
        // -------------------------------------------
        for (Day day : days) {
            if (day.getSpan() > 0 && day.isWeekend())
                dayCount = 7;
            actualSpan += day.getSpan();
            if (day.isFuture() && !day.isWeekend())
                futures += 1;
        }

        actualDiff = MINUTES_PER_WEEK - actualSpan;
        int ratio = futures > 0 ? (MINUTES_PER_WEEK - actualSpan) / futures : 0;
        int rest = futures > 0 ? (MINUTES_PER_WEEK - actualSpan) - futures * ratio : 0;
//        System.out.println("REST="+rest);
        simulatedSpan = 0;

        // ----------------------------------------------
        // Second iteration stamp values on day objects
        // ----------------------------------------------
        for (int index = 0; index < 7; index++) {
            Day day = days[index];
            if (day.isFuture() && !day.isWeekend()) {
                if (rest > 0) {
                    rest--;
                    day.setRatio(ratio + 1);
                } else {
                    day.setRatio(ratio);
                }
            }
            simulatedSpan += day.getDisplaySpan();
            if (index < 5)
                simulatedDiff = simulatedSpan - (index + 1) * Day.MINUTES_PER_DAY;
            else
                simulatedDiff = simulatedSpan - 5 * Day.MINUTES_PER_DAY;
            day.setAccumulatedDiff(simulatedDiff);
        }
    }
}
