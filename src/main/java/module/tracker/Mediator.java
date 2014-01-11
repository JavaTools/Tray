package module.tracker;

import module.tracker.view.StatusView;
import module.tracker.view.TrackerModel;
import module.tracker.view.TrackerView;
import module.tracker.view.WeekView;
import module.wallpaper.MainWallpaper;

public class Mediator {

    private TrackerModel trackerModel;
    private TrackerView view;
    private TrackerFrame frame;
    private WeekView week;
    private StatusView status;
    private MainWallpaper mainWallpaper;

    public Mediator() {
        setup();
    }

    public MainWallpaper getMainWallpaper() {
        return mainWallpaper;
    }

    public TrackerFrame getFrame() {
        return frame;
    }

    public TrackerView getView() {
        return view;
    }

    public StatusView getStatus() {
        return status;
    }

    public WeekView getWeek() {
        return week;
    }

    private void setup() {
        trackerModel = new TrackerModel();
        view = new TrackerView(trackerModel);
        week = new WeekView(trackerModel);
        status = new StatusView(trackerModel);
        frame = new TrackerFrame(this);
        mainWallpaper = new MainWallpaper();
    }

    public TrackerModel getTrackerModel() {
        return trackerModel;
    }
}
