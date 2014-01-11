package module.wallpaper;

import module.wallpaper.producer.Builder;
import module.wallpaper.swing.calendar.FrameCalendar;
import module.wallpaper.utilities.Settings;
import module.wallpaper.markers.Markers;

import java.awt.image.BufferedImage;
import java.awt.*;
import java.io.File;

public class MainSwingCalendars {
    public static void main(String[] args) {
        if (args.length != 2)
            printSyntax();
        else {
            new MainSwingCalendars().run(args[0], args[1]);
        }
    }

    private void run(String config, String smarkers) {
        Settings constants = new Settings(config);
        Markers markers = new Markers(new File(smarkers));
        BufferedImage wallpaper = new Builder(constants, markers).buildImage2();

        FrameCalendar v = new FrameCalendar(wallpaper);
        Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
        int width = wallpaper.getWidth() + 100;
        int height = wallpaper.getHeight() + 160;
        v.setBounds((screen.width - width) / 2, (screen.height - height) / 2, width, height);
        v.setVisible(true);
    }

    private static void printSyntax() {
        System.out.println("JWallpaper v.1.0");
        System.out.println("java MainWallpaper properties_file");
    }
}
