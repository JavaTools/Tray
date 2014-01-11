import common.Log;
import module.tracker.Mediator;
import module.wallpaper.MainWallpaper;
import module.wallpaper.image.AudiParser;
import module.wallpaper.utilities.Config;

public class TestMain {

    public static void main(String[] args) throws Exception {
        /////////////////////////////////////////////////////////////////////////////////////
        //new TrayMain().runProduction(args);
        /////////////////////////////////////////////////////////////////////////////////////
        //new TestMain().runTracker();
        new TestMain().runWallpaper();
        //new TestMain().runMisc();
    }

    private void runTracker() {
        Config.getInstance(); // Using default properties as defined in Config.java
        Mediator mediator = new Mediator();
        mediator.getFrame().setVisible(true);
    }

    private void runWallpaper() {
        MainWallpaper mainWallpaper = new MainWallpaper();
        mainWallpaper.run();
    }

    private void runMisc() {
        AudiParser parser = new AudiParser();
        Log.log(parser.getCurrentUrl());
    }
}