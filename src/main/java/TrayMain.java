import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintStream;

import common.Log;
import module.tracker.Mediator;
import module.wallpaper.utilities.Config;
import tray.TrayDateApplication;
import tray.model.Dao;

public class TrayMain {

    public static void main(String[] args) {
        new TrayMain().runProduction(args);
    }

    public void runProduction(String[] args) {

        //setupLog();

        Log.sep();
        Log.log("Starting Tray Application");

        String config = (args.length > 0) ? args[0] : System.getenv("ANT_CONTEXT") + ".properties";
        Log.log("Configuration file: " + config);
        Config.getInstance().loadConfig(config);

        Log.log("Java library path is: " + System.getProperty("java.library.path"));

        new Dao().stamp(true);

        Mediator mediator = new Mediator();
        new TrayDateApplication(mediator).run();
    }

    /**
     * Simple, naive log-system. We simply assign our own PrintStream as
     * System.out. Then all log-messages can be redirected to a file when
     * deployed and in development the return statement can be commented
     * out to get all messages in the console.
     */
    private void setupLog() {
        try {
            File logFile = new File("tray.log");
            if (!logFile.exists())
                logFile.createNewFile();
            PrintStream out = new PrintStream(new FileOutputStream(logFile, true));
            System.setOut(out);
            System.setErr(out);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
