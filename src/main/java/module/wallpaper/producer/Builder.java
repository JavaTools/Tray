package module.wallpaper.producer;

import module.wallpaper.producer.decorater.PainterCalendar;
import module.wallpaper.utilities.Theme;
import module.wallpaper.utilities.Settings;
import module.wallpaper.markers.Markers;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Calendar;

public class Builder {
    private Settings settings;
    private PainterCalendar painterNormal, painterNow;
    private BufferedImage wallpaper;
    private int space;

    public Builder(Settings settings, Markers markers) {
        this.settings = settings;
        Theme theme = new Theme();
        Theme themeNow = new Theme();
        painterNormal = new PainterCalendar(theme, markers);
        painterNow = new PainterCalendar(themeNow, markers);
        space = 10;
    }

    public BufferedImage buildImageCut(BufferedImage wp) {
        if (wp == null) {
            wallpaper = settings.getDefaultImage(false);
        } else {
            wallpaper = buildWallpaper(wp);
        }

        paintCalendars();

        return wallpaper;
    }

    public BufferedImage buildImage(BufferedImage wallpaper) {
        this.wallpaper = wallpaper;
        paintCalendars();
        return wallpaper;
    }

    private BufferedImage buildWallpaper(BufferedImage wallpaper) {
        BufferedImage image = settings.getDefaultImage(true);

        Graphics2D g2 = (Graphics2D) image.getGraphics();

        int xs1 = settings.getNumber("file_cut_x1");
        int xs2 = settings.getNumber("file_cut_x2");
        int ys1 = settings.getNumber("file_cut_y1");
        int ys2 = settings.getNumber("file_cut_y2");

        int taskbar = settings.getNumber("taskbar");
        int xd1 = xs1;
        int xd2 = xs2;
        int yd1 = 254;
        int yd2 = yd1 + (ys2 - ys1);
        g2.drawImage(wallpaper, xd1, yd1, xd2, yd2, xs1, ys1, xs2, ys2, new JButton());

        return image;
    }

    public BufferedImage buildImage2() {
        this.wallpaper = new BufferedImage(
                painterNormal.getSize().width * 4 + 5 * space,
                painterNormal.getSize().height * 3 + 4 * space,
                BufferedImage.TYPE_INT_RGB
        );

        paintCalendars2(2008, 7);

        return wallpaper;
    }

    private void paintCalendars() {
        Calendar cNow = Calendar.getInstance();
        cNow.setFirstDayOfWeek(Calendar.MONDAY);
        int month = cNow.get(Calendar.MONTH);
        int year = cNow.get(Calendar.YEAR);
        int dx = painterNormal.getSize().width;
        int xx = wallpaper.getWidth();
        int frame = settings.getNumber("frame");

        paintCalendar(painterNormal, xx - dx * 4 - space * 3 - frame, frame, month - 1, year);
        paintCalendar(painterNow, xx - dx * 3 - space * 2 - frame, frame, month + 0, year);
        paintCalendar(painterNormal, xx - dx * 2 - space * 1 - frame, frame, month + 1, year);
        paintCalendar(painterNormal, xx - dx * 1 - space * 0 - frame, frame, month + 2, year);

    }

    private void paintCalendars2(int year, int month) {
        int dx = painterNormal.getSize().width + space;
        int xx = wallpaper.getWidth();
        int y;

        int yr = year;
        int mn = month;
        paintCalendar(painterNow, xx - dx * 4, space, mn, yr);
        mn++;
        if (mn == 13) {
            mn = 1;
            yr += 1;
        }
        paintCalendar(painterNormal, xx - dx * 3, space, mn, yr);
        mn++;
        if (mn == 13) {
            mn = 1;
            yr += 1;
        }
        paintCalendar(painterNormal, xx - dx * 2, space, mn, yr);
        mn++;
        if (mn == 13) {
            mn = 1;
            yr += 1;
        }
        paintCalendar(painterNormal, xx - dx * 1, space, mn, yr);

        y = space * 2 + painterNormal.getSize().height;
        mn++;
        if (mn == 13) {
            mn = 1;
            yr += 1;
        }
        paintCalendar(painterNormal, xx - dx * 4, y, mn, yr);
        mn++;
        if (mn == 13) {
            mn = 1;
            yr += 1;
        }
        paintCalendar(painterNormal, xx - dx * 3, y, mn, yr);
        mn++;
        if (mn == 13) {
            mn = 1;
            yr += 1;
        }
        paintCalendar(painterNormal, xx - dx * 2, y, mn, yr);
        mn++;
        if (mn == 13) {
            mn = 1;
            yr += 1;
        }
        paintCalendar(painterNormal, xx - dx * 1, y, mn, yr);

        y = space * 3 + painterNormal.getSize().height * 2;
        mn++;
        if (mn == 13) {
            mn = 1;
            yr += 1;
        }
        paintCalendar(painterNormal, xx - dx * 4, y, mn, yr);
        mn++;
        if (mn == 13) {
            mn = 1;
            yr += 1;
        }
        paintCalendar(painterNormal, xx - dx * 3, y, mn, yr);
        mn++;
        if (mn == 13) {
            mn = 1;
            yr += 1;
        }
        paintCalendar(painterNormal, xx - dx * 2, y, mn, yr);
        mn++;
        if (mn == 13) {
            mn = 1;
            yr += 1;
        }
        paintCalendar(painterNormal, xx - dx * 1, y, mn, yr);
    }

    private void paintCalendar(PainterCalendar d, int x, int space, int month, int year) {
        int mon = month;

        if (month < 0) {
            mon = 11;
            year--;
        } else {
            if (month > 11) {
                mon = mon - 12;
                year++;
            }
        }
        d.paint(wallpaper, x, space, mon, year);
    }
}
