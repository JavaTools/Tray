package module.tracker.view;

import common.Time;
import module.wallpaper.utilities.Config;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class StatusView extends JComponent implements TrackerListener {

    private TrackerModel model;
    private Dimension dimension;
    private static final Color COLOR_MISSING_HOURS = new Color(0xffffff);
    private static final Color COLOR_OVERTIME_HOURS = new Color(0x558855);
    private static final Color COLOR_WEEK_COUNT = new Color(0x555588);
    private static final Color COLOR_WEEK_COUNT_BACKGROUND = new Color(0xEFEBE3);

    public StatusView(TrackerModel trackerModel) {
        this.model = trackerModel;
        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                model.refresh();
            }
        });
        model.addListener(this);
    }

    @Override
    public void paint(Graphics g) {

        super.paint(g);

        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setFont(getFont());

        FontMetrics fm = g2.getFontMetrics();

        // Paint average time

        String text = "";

        int diff = model.getPeriod().getTotalDiff();

        if (diff < 0) {
            g2.setPaint(COLOR_OVERTIME_HOURS);
            text = Time.minutesFormatted(diff * -1);
        } else {
            g2.setPaint(COLOR_MISSING_HOURS);
            text = Time.minutesFormatted(diff);
        }

        int textHeight = fm.getAscent() - fm.getDescent();
        int width = fm.stringWidth(text);
        int textX = getWidth() - width;
        int textY = (getHeight() - textHeight) / 2 + textHeight;
        g2.drawString(text, textX, textY);

        // Paint Number of Weeks

        text = "" + model.getPeriod().getWeeks();
        width = fm.stringWidth(text);
        textX = (textX - width) - 20;

        g2.setPaint(COLOR_WEEK_COUNT_BACKGROUND);
        g2.fillRoundRect(textX, 1, width + 10, getHeight(), 5, 5);

        g2.setPaint(COLOR_WEEK_COUNT);
        g2.drawString(text, textX + 5, textY);
    }

    @Override
    public Dimension getPreferredSize() {
        return getDimension();
    }

    @Override
    public Dimension getMinimumSize() {
        return getDimension();
    }

    @Override
    public Dimension getMaximumSize() {
        return getDimension();
    }

    public void update() {
        repaint();
    }

    private Dimension getDimension() {
        if (dimension == null) {
            Graphics2D g2 = (Graphics2D) getGraphics();
            g2.setFont(Config.getInstance().FONT_WINDOW);
            FontMetrics fm = g2.getFontMetrics();
            dimension = new Dimension(fm.stringWidth("3333-333"), fm.getAscent() - fm.getDescent() + 7);
        }
        return dimension;
    }
}
