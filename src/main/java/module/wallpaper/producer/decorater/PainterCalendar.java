package module.wallpaper.producer.decorater;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Composite;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.GradientPaint;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;
import java.util.Calendar;

import module.wallpaper.markers.ColorSet;
import module.wallpaper.markers.Markers;
import module.wallpaper.utilities.Config;
import module.wallpaper.utilities.Theme;

public class PainterCalendar {
    private Graphics2D g2;
    private Calendar cIterator;
    private Dimension size;
    private int cellWidth, cellHeight, textHeight, calendar_padding, calendar_spacer, arc;
    private int calendar_title_box_height, calendar_title_text_height;
    private FontMetrics fontMetricsNormal, fontMetricsDays, fontMetricsTitle;
    private String[] months = { "Januar", "Februar", "Marts", "April", "Maj", "Juni", "Juli", "August", "September", "Oktober", "November", "December" };
    private String[] days = { "M", "T", "O", "T", "F", "L", "S" };
    private Theme theme;
    private Markers markers;

    public PainterCalendar(Markers markers) {
        this(new Theme(), markers);
    }

    public PainterCalendar(Theme theme, Markers markers) {
        this.theme = theme;
        this.markers = markers;
    }

    public void setTheme(Theme theme) {
        this.theme = theme;
        setupMeasures();
    }

    public Dimension getSize() {
        if (g2 == null) {
            BufferedImage im = new BufferedImage(200, 200, BufferedImage.TYPE_INT_RGB);
            g2 = im.createGraphics();
        }
        setupMeasures();
        return size;
    }

    public BufferedImage paint(BufferedImage wpaper, int xx, int yy, int month, int year) {
        this.g2 = wpaper.createGraphics();
        setupLogic();
        setupMeasures();
        g2.translate(xx, yy);
        decorate(g2);
        int x, y;

        debugLines(Config.getInstance().DEBUG);

        g2.setColor(theme.colorDates);

        // -- TITLE ----------------------------------------------

        String title = months[month] + " " + year;
        x = (size.width - fontMetricsTitle.stringWidth(title)) / 2;
        y = calendar_padding + calendar_title_box_height - (calendar_title_box_height-calendar_title_text_height)/2;
        g2.setFont(theme.fontTitle);
        g2.setColor(theme.colorTitle);
        g2.drawString(title, x, y);

        // -- DAYS -----------------------------------------------

        y = calendar_padding * 2 + calendar_title_box_height + cellHeight - (cellHeight / 6);
        g2.setFont(theme.fontDays);
        g2.setColor(theme.colorDays);
        for (int i = 0; i < days.length; ++i) {
            x = calendar_padding + calendar_spacer + cellWidth + i * cellWidth;
            int push = (cellWidth - fontMetricsDays.stringWidth(days[i])) / 2;
            g2.drawString(days[i], x + push, y);
        }

        // -- paint week numbers ---------------------------------

        cIterator.set(year, month, 1);
        g2.setFont(theme.fontWeek);
        g2.setColor(theme.colorWeeks);
        for (int i = 0; i < 6; ++i) {
            x = calendar_padding;
            y = calendar_padding * 2 + calendar_title_box_height + cellHeight+ calendar_spacer + i*cellHeight;
            String s = "" + cIterator.get(Calendar.WEEK_OF_YEAR);
            int push = (cellWidth - fontMetricsNormal.stringWidth(s)) / 2;
            g2.drawString(s, x + push, y + cellHeight - (cellHeight-textHeight)/2);
            cIterator.add(Calendar.DATE, 7);
        }

        // -- paint dates ----------------------------------------

        cIterator.set(year, month, 1);
        g2.setFont(theme.fontNormal);
        int line = 0;
        while (cIterator.get(Calendar.MONTH) == month) {
            int dt = cIterator.get(Calendar.DATE);
            int d = cIterator.get(Calendar.DAY_OF_WEEK) - 1;

            x = calendar_padding + cellWidth + calendar_spacer + (d == 0 ? 6 * cellWidth : (d - 1) * cellWidth);
            y = calendar_padding * 2 + calendar_title_box_height + cellHeight + calendar_spacer + (line*cellHeight);

            String s = "" + dt;
            int push = (int) ((cellWidth-fontMetricsNormal.stringWidth(s)) / 2) +1;

            ColorSet cs = markers.getColor(cIterator);
            if (cs != null) {
                g2.setColor(cs.getBack());
                g2.fill(new RoundRectangle2D.Double(x + 2, y+2, cellWidth-3, cellHeight-3 , 5, 5));
//                g2.fill(new Rectangle2D.Double(x + 2, y+2, cellWidth-3, cellHeight-3));
                g2.setColor(cs.getFront());
            } else {
                g2.setColor(theme.colorDates);
            }
            g2.drawString(s, x + push, y + cellHeight - (cellHeight-textHeight)/2);

            line = d == 0 ? line + 1 : line;
            cIterator.add(Calendar.DAY_OF_MONTH, 1);
        }
        return wpaper;
    }

    private void setupLogic() {
        Calendar cNow = Calendar.getInstance();
        cNow.setFirstDayOfWeek(Calendar.MONDAY);
        cIterator = Calendar.getInstance();
        cIterator.setFirstDayOfWeek(Calendar.MONDAY);
    }

    private void decorate(Graphics2D g2) {
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        // -- Background Full -------------

        GradientPaint gp = new GradientPaint(
                0, 0, theme.colorGradientOuterLight, size.width,
                size.height, theme.colorGradientOuterDark, false
        );
        g2.setPaint(gp);
        g2.fill(new RoundRectangle2D.Double(
                0, 0, size.width, size.height, arc, arc
        ));
//        g2.setColor(Color.lightGray);
//        g2.draw(new RoundRectangle2D.Double(
//                0, 0, size.width, size.height, arc, arc
//        ));

        // -- Title ----------------------------

        g2.setPaint(new GradientPaint(
                calendar_padding, calendar_padding, theme.colorGradientInnerLight,
                size.width - calendar_padding, calendar_padding + 16, theme.colorGradientInnerDark, false
        ));
        g2.fill(new RoundRectangle2D.Double(
                calendar_padding, calendar_padding, size.width - calendar_padding * 2, calendar_title_box_height, arc, arc)
        );

        // -- Date area ------------------------

        g2.setPaint(new GradientPaint(
                calendar_padding + cellWidth + calendar_spacer, calendar_padding * 2 + calendar_title_box_height + calendar_spacer + cellHeight, theme.colorGradientInnerLight,
                size.width - (calendar_padding * 2 + cellWidth + calendar_spacer), size.height - (calendar_padding * 2 + calendar_title_box_height + calendar_spacer + cellHeight),
                theme.colorGradientInnerDark, false)
        );
//        Composite composite = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.9f);
//        g2.setComposite(composite);
//        g2.setColor(Color.WHITE);
        g2.fill(new RoundRectangle2D.Double(
                calendar_padding + cellWidth + calendar_spacer,
                calendar_padding * 2 + calendar_title_box_height + calendar_spacer + cellHeight,
                size.width - (calendar_padding * 2 + cellWidth + calendar_spacer),
                size.height - (calendar_padding * 2 + calendar_title_box_height + calendar_spacer + cellHeight) - calendar_padding,
                arc, arc
        ));

        // -- Sundays ---------------------

        int x = calendar_padding + cellWidth + calendar_spacer + 5 * cellWidth;
        int y = calendar_padding * 2 + calendar_title_box_height;

//        g2.setPaint(new GradientPaint(
//                x - 1, y, theme.colorGradientSundayDark,
//                x + cellWidth + 1, y + cellHeight * 7 + calendar_spacer + 3, theme.colorGradientSundayLight,
//                false
//        ));
        Composite composite = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.2f);
        g2.setComposite(composite);
        g2.setColor(Color.RED);
        g2.fill(new RoundRectangle2D.Double(
                x,
                y,
                cellWidth *2,
                cellHeight + calendar_spacer + cellHeight*6,
                arc, arc
        ));
        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER));
    }

    private void setupMeasures() {
        fontMetricsNormal = g2.getFontMetrics(theme.fontNormal);
        fontMetricsDays = g2.getFontMetrics(theme.fontDays);
        fontMetricsTitle = g2.getFontMetrics(theme.fontTitle);

        cellWidth = Config.getInstance().calendar_cell_box_width;
        cellHeight = Config.getInstance().calendar_cell_box_height;
        textHeight = Config.getInstance().textHeight;
        calendar_padding = Config.getInstance().calendar_padding;
        arc = Config.getInstance().arc;
        calendar_spacer = Config.getInstance().calendar_spacer;
        calendar_title_box_height = Config.getInstance().calendar_title_box_height;
        calendar_title_text_height = Config.getInstance().calendar_title_text_height;

        // Size is the Dimension of one calendar
        size = new Dimension(
            max(
            maxNameLength(fontMetricsTitle, months, " 2002") + calendar_padding * 2,
            calendar_padding * 2 + cellWidth + calendar_spacer + 7 * cellWidth
            ),
            calendar_padding * 3 + calendar_title_box_height + calendar_spacer + cellHeight + (cellHeight) * 6
        );
        cellWidth = max(cellWidth, (size.width - 2 * calendar_padding - calendar_spacer) / 8);
    }

    private int maxNameLength(FontMetrics fm, String[] names, String postFix) {
        int len = 0;
        for (String name : names) {
            int l = fm.stringWidth(name + postFix);
            if (l > len)
                len = l;
        }
        return len;
    }

    private int max(int a, int b) {
        return a >= b ? a : b;
    }

    private void debugLines(int enabled) {

        if (enabled>0) {
            g2.setColor(new Color(200,80,200));

            hLine(g2, calendar_padding);
            hLine(g2, calendar_padding + calendar_title_box_height);
            hLine(g2, calendar_padding + calendar_title_box_height + calendar_padding);
            hLine(g2, calendar_padding + calendar_title_box_height + calendar_padding + cellHeight);
            hLine(g2, calendar_padding + calendar_title_box_height + calendar_padding + cellHeight + calendar_spacer);
            hLine(g2, calendar_padding + calendar_title_box_height + calendar_padding + cellHeight + calendar_spacer + cellHeight * 1);
            hLine(g2, calendar_padding + calendar_title_box_height + calendar_padding + cellHeight + calendar_spacer + cellHeight * 2);
            hLine(g2, calendar_padding + calendar_title_box_height + calendar_padding + cellHeight + calendar_spacer + cellHeight * 3);
            hLine(g2, calendar_padding + calendar_title_box_height + calendar_padding + cellHeight + calendar_spacer + cellHeight * 4);
            hLine(g2, calendar_padding + calendar_title_box_height + calendar_padding + cellHeight + calendar_spacer + cellHeight * 5);
            hLine(g2, calendar_padding + calendar_title_box_height + calendar_padding + cellHeight + calendar_spacer + cellHeight * 6);

            vLine(g2, calendar_padding);
            vLine(g2, calendar_padding + cellWidth);
            vLine(g2, calendar_padding + cellWidth + calendar_spacer);
            vLine(g2, calendar_padding + cellWidth + calendar_spacer + cellWidth * 1);
            vLine(g2, calendar_padding + cellWidth + calendar_spacer + cellWidth * 2);
            vLine(g2, calendar_padding + cellWidth + calendar_spacer + cellWidth * 3);
            vLine(g2, calendar_padding + cellWidth + calendar_spacer + cellWidth * 4);
            vLine(g2, calendar_padding + cellWidth + calendar_spacer + cellWidth * 5);
            vLine(g2, calendar_padding + cellWidth + calendar_spacer + cellWidth * 6);
            vLine(g2, calendar_padding + cellWidth + calendar_spacer + cellWidth * 7);
        }
    }

    private void hLine(Graphics2D g2, int y) {
        g2.drawLine(0, y, size.width, y);
    }

    private void vLine(Graphics2D g2, int x) {
        g2.drawLine(x, 0, x, size.height);
    }
}
