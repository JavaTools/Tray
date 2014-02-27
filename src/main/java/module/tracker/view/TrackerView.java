package module.tracker.view;

import common.Time;
import module.wallpaper.utilities.Config;
import tray.model.Day;
import tray.model.Week;

import javax.swing.*;
import java.awt.*;
import java.io.InputStream;

public class TrackerView extends JComponent implements TrackerListener {

    private TrackerModel model;
    private Font font;
    private int textWidth = 0;
    private int cellWidth = 0;

    private static final Color COLOR_BACKGROUND_FROM = Color.white;
    private static final Color COLOR_BACKGROUND_TO = new Color(0xf2f2f2);
    private static final Color COLOR_TEXT_DAY_NORMAL = new Color(0x7796da);
    private static final Color COLOR_TEXT_SPAN = new Color(0x84a6ed);
    private static final Color COLOR_TEXT_POSITIVE = new Color(0x558855);
    private static final Color COLOR_TEXT_NEGATIVE = new Color(0x885555);

    private static final Color COLOR_TOTAL_BACKGROUND_FROM = new Color(0xc4e6f5);
    private static final Color COLOR_TOTAL_BACKGROUND_TO = new Color(0xbccbf4);
    private static final Color COLOR_TOTAL_TEXT = new Color(0x666666);
    private static final Color COLOR_TOTAL_RESULT_BACKGROUND_FROM = new Color(0x88aab9);
    private static final Color COLOR_TOTAL_RESULT_BACKGROUND_TO = new Color(0x808fb8);
    private static final Color COLOR_TOTAL_RESULT_MISSING_HOURS = new Color(0xffffff);
    private static final Color COLOR_TOTAL_RESULT_OVERTIME_HOURS = new Color(0xbbeebb);

    public TrackerView(TrackerModel model) {
        this.model = model;
        loadFont();
        model.addListener(this);
    }

    @Override
    public void paint(Graphics g) {

        Graphics2D g2 = (Graphics2D) g;
        g2.setFont(font);
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        Week week = model.getWeek();

        int space = getHeight() - Config.getInstance().WINDOW_TOTALS_HEIGHT;
        int dy = space / week.getDayCount();   // General delta Y for the rows
        int rest = space % week.getDayCount(); // If row height doesn't match space we have a rest to distribute

        for (int index = 0; index < week.getDayCount(); index++) {
            int push = dy;
            if (rest > 0) {
                push++;
                rest--;
            }
            paintDay(g2, push, index);
            g2.translate(0, push);
        }

        paintTotal(g2, Config.getInstance().WINDOW_TOTALS_HEIGHT);
    }

    private void paintDay(Graphics2D g2, Integer dy, int index) {

        Day day = model.getWeek().getDay(index);
        int M = Config.getInstance().WINDOW_MARGIN;

        // -------------------------------------------------------------------------------------------------------
        // Background
        // -------------------------------------------------------------------------------------------------------

        GradientPaint gp = new GradientPaint(0, 0, COLOR_BACKGROUND_FROM, 0, dy, COLOR_BACKGROUND_TO, true);
        g2.setPaint(gp);
        g2.fill(new Rectangle(0, 0, getBounds().width, dy));

        if (day.isFuture()) {
            g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, .3f));
        }

        FontMetrics fm = g2.getFontMetrics();
        textWidth = fm.stringWidth("Torsdag") + M * 2;
        cellWidth = (getWidth() - textWidth) / 3;

        // -------------------------------------------------------------------------------------------------------
        // Day
        // -------------------------------------------------------------------------------------------------------
        String text = day.getName();
        int textHeight = fm.getAscent() - fm.getDescent();
        int textX = M;
        int textY = (dy - textHeight) / 2 + textHeight;
        g2.setColor(COLOR_TEXT_DAY_NORMAL);
        g2.drawString(text, textX, textY);

        // -------------------------------------------------------------------------------------------------------
        // Span
        // -------------------------------------------------------------------------------------------------------
        text = Time.minutesToHoursFormatted(day.getDisplaySpan());
        textX = textWidth + cellWidth * 1 - fm.stringWidth(text) - M;
        g2.setPaint(COLOR_TEXT_SPAN);
        g2.drawString(text, textX, textY);

        // -------------------------------------------------------------------------------------------------------
        // DayDiff
        // -------------------------------------------------------------------------------------------------------
        text = Time.minutesFormatted(day.getDiff());
        textX = textWidth + cellWidth * 2 - fm.stringWidth(text) - M;
        g2.setPaint(day.getDiff() > 0 ? COLOR_TEXT_POSITIVE : COLOR_TEXT_NEGATIVE);
        g2.drawString(text, textX, textY);

        // -------------------------------------------------------------------------------------------------------
        // AccumulatedDiff
        // -------------------------------------------------------------------------------------------------------
        text = Time.minutesFormatted(day.getAccumulatedDiff());
        textX = textWidth + cellWidth * 3 - fm.stringWidth(text) - M;
        g2.setPaint(day.getAccumulatedDiff() > 0 ? COLOR_TEXT_POSITIVE : COLOR_TEXT_NEGATIVE);
        g2.drawString(text, textX, textY);
    }

    private void paintTotal(Graphics2D g2, int dy) {

        Week week = model.getWeek();
        int M = Config.getInstance().WINDOW_MARGIN;

        // -------------------------------------------------------------------------------------------------------
        // Background
        // -------------------------------------------------------------------------------------------------------

        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));
        Rectangle bounds = getBounds();
        GradientPaint gp = new GradientPaint(0, 0, COLOR_TOTAL_BACKGROUND_FROM, 0, dy, COLOR_TOTAL_BACKGROUND_TO, true);
        g2.setPaint(gp);
        g2.fill(new Rectangle(0, 0, bounds.width, dy));

        // -------------------------------------------------------------------------------------------------------
        // Text
        // -------------------------------------------------------------------------------------------------------
        FontMetrics fm = g2.getFontMetrics();

        int textHeight = fm.getAscent() - fm.getDescent();
        int textX = M;
        int textY = (dy - textHeight) / 2 + textHeight;
        String text = "Samlet";
        g2.setPaint(COLOR_TOTAL_TEXT);
        g2.drawString(text, textX, textY);

        // -------------------------------------------------------------------------------------------------------
        // Total
        // -------------------------------------------------------------------------------------------------------
        text = Time.minutesToHoursFormatted(week.getSpan());
        textX = textWidth + cellWidth * 1 - fm.stringWidth(text) - M;
        g2.drawString(text, textX, textY);

        // -------------------------------------------------------------------------------------------------------
        // AccumulatedDiff
        // -------------------------------------------------------------------------------------------------------
        // Total for day-diff does not make any sense

        // -------------------------------------------------------------------------------------------------------
        // Overall Result (Missing time for the week) + Second-indicator
        // -------------------------------------------------------------------------------------------------------
        int secs = (int) ((System.currentTimeMillis() / 1000)) % 60;
        int target = (int) (((double) cellWidth / 60D) * (double) secs);
        int x = textWidth + cellWidth * 2;

        g2.setPaint(new GradientPaint(0, 0, COLOR_TOTAL_RESULT_BACKGROUND_FROM, x, x + cellWidth, COLOR_TOTAL_RESULT_BACKGROUND_TO, true));
        g2.fillRect(x, 0, cellWidth, dy);
        if (Time.isCurrent(week)) {
            g2.setPaint(Color.RED);
            g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.15F));
            g2.fillRect(x, 0, target, dy);
            g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER));
        }

        if (week.getActualDiff() < 0) {
            text = Time.minutesFormatted(week.getActualDiff() * -1);
            g2.setColor(COLOR_TOTAL_RESULT_OVERTIME_HOURS);
        } else {
            text = Time.minutesFormatted(week.getActualDiff());
            g2.setColor(COLOR_TOTAL_RESULT_MISSING_HOURS);
        }
        textX = textWidth + cellWidth * 2 + ((cellWidth - fm.stringWidth(text)) / 2);

        g2.drawString(text, textX, textY);
    }

    public void update() {
        repaint();
    }

    private void loadFont() {
        try {
            InputStream is = getClass().getClassLoader().getResourceAsStream("Aller_Bd.ttf");
            Font base = Font.createFont(Font.TRUETYPE_FONT, is);
            is.close();
            font = base.deriveFont(22F);
        } catch (Exception ex) {
            font = new Font("Trebuchet MS", Font.BOLD, 20);
        }
    }
}
