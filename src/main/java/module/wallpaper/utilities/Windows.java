package module.wallpaper.utilities;

import org.jdesktop.jdic.misc.Wallpaper;
import org.jdesktop.jdic.misc.WallpaperFactory;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;

public class Windows {
    public static void setWallpaper(BufferedImage image) {
        try {
            String file = Config.getInstance().FILE_WALLPAPER;
            ImageIO.write(image, "bmp", new File(file));
            Wallpaper wpaper = WallpaperFactory.createWallpaper();
            wpaper.setBackground(file, 1);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
