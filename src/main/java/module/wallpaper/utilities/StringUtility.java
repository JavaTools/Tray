package module.wallpaper.utilities;

import java.util.GregorianCalendar;

public class StringUtility {
    private static String[] monthNames = {
            "januar", "februar", "marts",
            "april", "maj", "juni",
            "juli", "august", "september",
            "oktober", "november", "december"
    };

    public static String getMonthName() {
        GregorianCalendar gc = new GregorianCalendar();
        return monthNames[gc.get(GregorianCalendar.MONTH)];
    }

    public static String getMonthNumberFromName(String name) {
        GregorianCalendar gc = new GregorianCalendar();
        String result = getMonthNumber();
        for (int index = 0; index < monthNames.length; index++) {
            String monthName = monthNames[index];
            if (monthName.toLowerCase().equals(name.toLowerCase())) {
                String mon = "0" + (index + 1);
                mon = mon.substring(mon.length() - 2, mon.length());
                result = "" + gc.get(GregorianCalendar.YEAR) + "-" + mon;
            }
        }
        return result;

    }

    public static String getMonthNumber() {
        GregorianCalendar gc = new GregorianCalendar();
        String month = "0" + gc.get(GregorianCalendar.MONTH);
        month = month.substring(month.length() - 2, month.length());
        return "" + gc.get(GregorianCalendar.YEAR) + "-" + month;
    }
}
