package module.tracker.view;

import module.wallpaper.utilities.Config;
import tray.model.Dao;
import tray.model.Period;
import tray.model.Week;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;

public class TrackerModel {

    private ArrayList<TrackerListener> listeners = new ArrayList<TrackerListener>();
    private final Period period = new Period();
    private Integer weekIndex;

    public TrackerModel() {
        load();
        weekIndex = period.getWeeks() - 1;
    }

    public synchronized void refresh() {
        load();
        fireUpdate();
    }

    public synchronized Period getPeriod() {
        return period;
    }

    public synchronized void addListener(TrackerListener listener) {
        listeners.add(listener);
    }

    public synchronized void scroll(Integer delta) {
        weekIndex += delta;
        if (weekIndex < 0) weekIndex = 0;
        if (weekIndex > (period.getWeeks() - 1)) weekIndex = period.getWeeks() - 1;
        fireUpdate();
    }

    public synchronized Week getWeek() {
        return period.get(weekIndex);
    }

    public synchronized void update() {
//        long time = System.currentTimeMillis();
        load();
        fireUpdate();
//        System.out.println("Update took : " + (System.currentTimeMillis()-time)/1000D + " seconds");
    }

    private void fireUpdate() {
        for (TrackerListener listener : listeners) {
            listener.update();
        }
    }

    public synchronized void load() {

        Dao dao = new Dao();

        period.clear();
        File stampDir = new File(Config.getInstance().DIR_STAMPS);
        File[] stamps = stampDir.listFiles(new FilenameFilter() {
            public boolean accept(File dir, String name) {
                return name.endsWith(".txt");
            }
        });
        for (File stamp : stamps) {
            Week week = dao.read(stamp);
            week.calculateValues();
            period.add(week);
        }
    }
}
