package module.wallpaper.producer.decorater;

import java.awt.Color;
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
import module.wallpaper.utilities.Settings;
import module.wallpaper.utilities.Theme;

public class PainterCalendar {
    private Graphics2D g2;
    private Calendar cIterator;
    private Dimension size;
    private int cellW, margin, pad, arc;
    private int line_separator;
    private int heightB, heightN;
    private int titleHeight=45, titleY = 38;
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
        g2.setFont(theme.fontTitle);
        g2.setColor(theme.colorTitle);
        g2.drawString(title, x, titleY);

        // -- DAYS -----------------------------------------------

        y = margin * 2 + titleHeight + heightB - (heightB / 6);
        g2.setFont(theme.fontDays);
        g2.setColor(theme.colorDays);
        for (int i = 0; i < days.length; ++i) {
            x = margin + pad + cellW + i * cellW;
            int push = (cellW - fontMetricsDays.stringWidth(days[i])) / 2;
            g2.drawString(days[i], x + push, y);
        }

        // -- paint week numbers ---------------------------------

        cIterator.set(year, month, 1);
        g2.setFont(theme.fontWeek);
        g2.setColor(theme.colorWeeks);
        for (int i = 0; i < 6; ++i) {
            x = margin;
            y = margin * 2 + titleHeight + pad + heightB + line_separator + i * (heightN+line_separator) + heightN - heightN / 6;
            String s = "" + cIterator.get(Calendar.WEEK_OF_YEAR);
            int push = (cellW - fontMetricsNormal.stringWidth(s)) / 2;
            g2.drawString(s, x + push, y);
            cIterator.add(Calendar.DATE, 7);
        }

        // -- paint dates ----------------------------------------

        cIterator.set(year, month, 1);
        g2.setFont(theme.fontNormal);
        int line = 0;
        while (cIterator.get(Calendar.MONTH) == month) {
            int dt = cIterator.get(Calendar.DATE);
            int d = cIterator.get(Calendar.DAY_OF_WEEK) - 1;

            x = margin + cellW + pad + (d == 0 ? 6 * cellW : (d - 1) * cellW);
            y = margin * 2 + titleHeight + pad + heightB + line_separator + (line * (heightN+line_separator)) + heightN
                    - (heightN / 6); // Sidste heightM/6 er et sjus samlet højde er 1.5*heightN

            String s = "" + dt;
            int push = (cellW - fontMetricsNormal.stringWidth(s)) / 2;

            ColorSet cs = markers.getColor(cIterator);
            if (cs != null) {
                g2.setColor(cs.getBack());
                g2.fill(new RoundRectangle2D.Double(x + 1, y - heightN + 3, cellW - 3, heightN+2 , arc, arc));
                g2.setColor(cs.getFront());
            } else {
                g2.setColor(theme.colorDates);
            }
            g2.drawString(s, x + push, y);

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

        g2.draw(new RoundRectangle2D.Double(
                0, 0, size.width, size.height, arc, arc
        ));

        // -- Title ----------------------------

        g2.setPaint(new GradientPaint(
                margin, margin, theme.colorGradientInnerLight,
                size.width - margin, margin + 16, theme.colorGradientInnerDark, false
        ));
        g2.fill(new RoundRectangle2D.Double(
                margin, margin, size.width - margin * 2, titleHeight, arc, arc)
        );

        // -- Date area ------------------------

        g2.setPaint(new GradientPaint(
                margin + cellW + pad, margin * 2 + titleHeight + pad + heightB, theme.colorGradientInnerLight,
                size.width - (margin * 2 + cellW + pad), size.height - (margin * 2 + titleHeight + pad + heightB),
                theme.colorGradientInnerDark, false)
        );
        g2.fill(new RoundRectangle2D.Double(
                margin + cellW + pad,
                margin * 2 + titleHeight + pad + heightB,
                size.width - (margin * 2 + cellW + pad),
                size.height - (margin * 2 + titleHeight + pad + heightB) - margin,
                arc, arc
        ));

        // -- Sundays ---------------------

        int x = margin + cellW + pad + 5 * cellW;
        int y = margin * 2 + titleHeight;

        g2.setPaint(new GradientPaint(
                x - 1, y, theme.colorGradientSundayDark,
                x + cellW + 1, y + heightN * 7 + pad + 3, theme.colorGradientSundayLight,
                false
        ));
        g2.fill(new RoundRectangle2D.Double(
                x,
                y,
                cellW*2,
                line_separator + (heightN+line_separator) * 6 + pad + heightB,
                arc, arc
        ));
    }

    private void setupMeasures() {
        fontMetricsNormal = g2.getFontMetrics(theme.fontNormal);
        fontMetricsDays = g2.getFontMetrics(theme.fontDays);
        fontMetricsTitle = g2.getFontMetrics(theme.fontTitle);
        cellW = max(fontMetricsNormal.stringWidth("8") * 3, fontMetricsDays.stringWidth("8") * 2);
        heightN = fontMetricsNormal.getAscent() + 2;
        heightB = fontMetricsDays.getAscent() + 2;
        System.out.println("-------------------------------------------------------------");
        System.out.println(fontMetricsTitle.getAscent());
        System.out.println(fontMetricsTitle.getDescent());
        System.out.println(fontMetricsTitle.getHeight());
        margin = Config.getInstance().margin;
        arc = Config.getInstance().arc;
        pad = Config.getInstance().pad;
        line_separator = Config.getInstance().line_separator;

        // Size is the Dimension of one calendar
        size = new Dimension(
            max(
            maxNameLength(fontMetricsTitle, months, " 2002") + margin * 2,
            margin * 2 + cellW + pad + 7 * cellW
            ),
            margin * 3 + titleHeight + pad + heightB + (heightN+line_separator) * 6 + line_separator
        );
        cellW = max(cellW, (size.width - 2 * margin - pad) / 8);
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

            hLine(g2, margin);
            hLine(g2, margin + titleHeight);
            hLine(g2, margin + titleHeight + margin);
            hLine(g2, margin + titleHeight + margin + heightB);
            hLine(g2, margin + titleHeight + margin + heightB + pad);
            hLine(g2, margin + titleHeight + margin + heightB + pad + heightN * 1);
            hLine(g2, margin + titleHeight + margin + heightB + pad + heightN * 2);
            hLine(g2, margin + titleHeight + margin + heightB + pad + heightN * 3);
            hLine(g2, margin + titleHeight + margin + heightB + pad + heightN * 4);
            hLine(g2, margin + titleHeight + margin + heightB + pad + heightN * 5);
            hLine(g2, margin + titleHeight + margin + heightB + pad + heightN * 6);

            vLine(g2, margin);
            vLine(g2, margin + cellW);
            vLine(g2, margin + cellW + pad);
            vLine(g2, margin + cellW + pad + cellW * 1);
            vLine(g2, margin + cellW + pad + cellW * 2);
            vLine(g2, margin + cellW + pad + cellW * 3);
            vLine(g2, margin + cellW + pad + cellW * 4);
            vLine(g2, margin + cellW + pad + cellW * 5);
            vLine(g2, margin + cellW + pad + cellW * 6);
            vLine(g2, margin + cellW + pad + cellW * 7);
        }
    }

    private void hLine(Graphics2D g2, int y) {
        g2.drawLine(0, y, size.width, y);
    }

    private void vLine(Graphics2D g2, int x) {
        g2.drawLine(x, 0, x, size.height);
    }
}
