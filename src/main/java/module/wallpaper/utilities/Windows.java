package module.wallpaper.utilities;

import com.sun.jna.Library;
import com.sun.jna.Native;
import com.sun.jna.win32.W32APIOptions;

import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;

public class Windows {

    public static final String wallpaper = Config.getInstance().FILE_WALLPAPER;

    public static interface User32 extends Library {
        User32 INSTANCE = (Windows.User32) Native.loadLibrary("user32", Windows.User32.class, W32APIOptions.DEFAULT_OPTIONS);
        boolean SystemParametersInfo (int one, int two, String s ,int three);
    }

    public static void setWallpaper(BufferedImage image) {
        try {
            ImageIO.write(image, "PNG", new File(wallpaper));
            User32.INSTANCE.SystemParametersInfo(0x0014, 0, wallpaper , 1);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

}
