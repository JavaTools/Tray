package common;

import java.text.SimpleDateFormat;

public class Log {
    private static SimpleDateFormat sdf = new SimpleDateFormat("[dd-MM-yyyy(HH:mm:ss)]");

    public static void sep() {
        realLog("--------------------------------------------------------------------------------");
    }

    public static void log(String text) {
        log(text, false);
    }

    public static void log(String text, Boolean printSeparator) {
        if (printSeparator)
            sep();
        realLog(text);
    }

    private static void realLog(String text) {
        String date = sdf.format(Time.getNow());
        System.out.println(date + " " + text);
    }
}
