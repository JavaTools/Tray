package module.wallpaper.producer.decorater;

import module.wallpaper.utilities.Config;
import module.wallpaper.utilities.Context;

import java.awt.*;
import java.awt.image.BufferedImage;

public class PainterBackground {
    private BufferedImage image;
    private Context context;

    public PainterBackground(Context context) {
        this.context = context;
        image = new BufferedImage(context.getConfig().width, context.getConfig().height, BufferedImage.TYPE_INT_RGB);
    }

    public BufferedImage paint(BufferedImage source, int top, int w, int h) {

        image.getGraphics().drawImage(source, 0, 0, null);

        Config cfg = Config.getInstance();

        Graphics2D g2 = (Graphics2D) image.getGraphics();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        Composite composite = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.05f);
        g2.setComposite(composite);
        g2.setPaint(Color.black);
        g2.fillRect(0, 0, image.getWidth(), top);

        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.7f));
        int nw = w + 2 * cfg.frame;
        int nh = image.getHeight() - top - cfg.taskbar - cfg.passepartout * 2;
        int x = image.getWidth() - nw - cfg.passepartout;
        int y = top + cfg.passepartout;
        g2.fillRoundRect(x, y, nw, nh, 20, 20);

        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.9f));
        g2.setColor(Color.WHITE);
        g2.setFont(Config.getInstance().getFont());
        FontMetrics fm = g2.getFontMetrics();
        String s = context.getMonthName() + " " + context.getYearName();
        y = y + (nh - context.getArt().getHeight() - cfg.passepartout) / 2 + (fm.getAscent() / 2);
        g2.drawString(s, x + w - fm.stringWidth(s) - Config.getInstance().frame, y);

        return image;
    }
}
