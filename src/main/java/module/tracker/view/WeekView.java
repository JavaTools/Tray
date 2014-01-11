package module.tracker.view;

import module.wallpaper.utilities.Config;

import javax.swing.*;
import java.awt.*;

public class WeekView extends JComponent implements TrackerListener {

    private TrackerModel model;
    private Dimension dimension;
    private Color COLOR = new Color(0x333333);

    public WeekView(TrackerModel trackerModel) {
        this.model = trackerModel;
        model.addListener(this);
    }

    @Override
    public void paint(Graphics g) {

        super.paint(g);

        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setPaint(COLOR);
        g2.setFont(getFont());

        FontMetrics fm = g2.getFontMetrics();
        int textHeight = fm.getAscent() - fm.getDescent();
        int textX = 0;
        int textY = (getHeight() - textHeight) / 2 + textHeight;
        g2.drawString(model.getWeek().getName(), textX, textY);
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
            dimension = new Dimension(fm.stringWidth("3333-333"), fm.getAscent() - fm.getDescent());
        }
        return dimension;
    }
}
