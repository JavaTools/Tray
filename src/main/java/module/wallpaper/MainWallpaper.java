package module.wallpaper;

import common.Log;
import module.wallpaper.image.ImageLoader;
import module.wallpaper.producer.WallpaperFactory;
import module.wallpaper.swing.preview.PreviewFrame;
import module.wallpaper.swing.preview.PreviewMaterials;
import module.wallpaper.utilities.Config;
import module.wallpaper.utilities.StringUtility;

import java.io.File;

public class MainWallpaper {
    public void run() {

        PreviewMaterials materials = new PreviewMaterials();
        WallpaperFactory factory = new WallpaperFactory();

        Log.log("Wallpaper, Launch Preview Frame");
        PreviewFrame previewFrame = new PreviewFrame(factory, materials);
        previewFrame.setVisible(true);
    }

    public void runForce() {
        Config config = Config.getInstance();
        String filename = config.DIR_ART + "\\" + StringUtility.getMonthNumber() + ".png";
        File file = new File(filename);
        try {
            Log.log("Wallpaper, Forcing new art download.");
            Log.log(" --> Trying to delete art file: " + file.getCanonicalPath());
            boolean deletion = file.delete();
            Log.log(" --> File Deletion (file.delete()) returned: " + deletion);
        } catch (Exception ex) {
            Log.log(" --> Exception: " + ex.getMessage());
        }
        run();
    }
}