package tray;

import common.Time;
import module.wallpaper.utilities.Config;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.text.SimpleDateFormat;

public class TrayDateIconImage extends BufferedImage {
    private SimpleDateFormat sdf = new SimpleDateFormat("d");
    private String day = sdf.format(Time.getNow());

    public TrayDateIconImage() {
        super(16, 16, TrayDateIconImage.TYPE_INT_RGB);
        setup();
    }

    public boolean shouldChange() {
        String newday = sdf.format(Time.getNow());
        return !day.equals(newday);
    }

    public void setup() {
        // -- Background ------------------------------------------------------------------

        Graphics2D g2 = (Graphics2D) getGraphics();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(Config.getInstance().COLOR_ICON_BACKGROUND);
        g2.fillRect(0, 0, 16, 16);

        // -- Frame -----------------------------------------------------------------------

        g2.setColor(Config.getInstance().COLOR_ICON_FRAME);
        g2.drawRect(0, 0, 15, 15);

        // -- Text ------------------------------------------------------------------------

        g2.setFont(Config.getInstance().FONT_ICON);
        int width = g2.getFontMetrics().stringWidth(day);
        int push = (16 - width) / 2;
        g2.setColor(Config.getInstance().COLOR_ICON_TEXT);
        g2.drawString(day, push, 12);
    }
}
