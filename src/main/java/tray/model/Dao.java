package tray.model;

import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

import common.Time;
import module.wallpaper.utilities.Config;
import common.Log;

public class Dao {
    public void stamp(boolean start) {
        try {
            Config config = Config.getInstance();
            String fileName = Time.getWeek();
            String action = start ? "start" : "stop";
            String stampFileName = Config.getInstance().getFilename(config.DIR_STAMPS, fileName);
            Log.log("Creating <" + action + "> stamp in file: " + stampFileName);
            File file = new File(stampFileName);
            if (!file.exists())
                file.createNewFile();
            PrintWriter pw = new PrintWriter(new FileWriter(file, true));
            pw.println(action + ";" + Time.getStamp());
            pw.flush();
            pw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Week read(File file) {

        // Read stamp file content

        ArrayList<String> stamps = new ArrayList<String>();
        try {
            Scanner scanner = new Scanner(file);
            while (scanner.hasNext()) {
                stamps.add(scanner.nextLine());
            }
            scanner.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        // Construct Week object

        return new Week(file.getName().replace(".txt", ""), stamps);
    }

    public Week read(String base) {
        Config config = Config.getInstance();
        File file = new File(Config.getInstance().getFilename(config.DIR_STAMPS, base));
        return read(file);
    }
}
