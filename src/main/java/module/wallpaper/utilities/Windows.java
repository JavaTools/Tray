package module.wallpaper.utilities;

import com.sun.jna.Library;
import com.sun.jna.Native;
import com.sun.jna.win32.W32APIOptions;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;

public class Windows {

    public static interface User32 extends Library {
        Windows.User32 INSTANCE = (Windows.User32) Native.loadLibrary("user32", Windows.User32.class,W32APIOptions.DEFAULT_OPTIONS);
        boolean SystemParametersInfo (int one, int two, String s ,int three);
    }

    public static void setWallpaper(BufferedImage image) {
        try {
            String file = Config.getInstance().FILE_WALLPAPER;
            ImageIO.write(image, "bmp", new File(file));
            Windows.User32.INSTANCE.SystemParametersInfo(0x0014, 0, Config.getInstance().FILE_WALLPAPER , 1);

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

}
