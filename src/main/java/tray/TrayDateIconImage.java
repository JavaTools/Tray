package tray;

import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.text.SimpleDateFormat;

import common.Time;
import module.wallpaper.utilities.Config;

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

//        g2.setColor(Config.getInstance().COLOR_ICON_FRAME);
//        g2.drawRect(0, 0, 15, 15);

        // -- Text ------------------------------------------------------------------------

        g2.setFont(Config.getInstance().FONT_ICON);
        Rectangle2D rect = g2.getFontMetrics().getStringBounds(day, 0, day.length(),null);

        int width = g2.getFontMetrics().stringWidth(day);
        int height = g2.getFontMetrics().getAscent()-g2.getFontMetrics().getDescent();
        int x = 8-(width/2);
        int y = 16-((16-height)/2);
        g2.setColor(Config.getInstance().COLOR_ICON_TEXT);
        g2.drawString(day, x, y);
    }
}
