package module.wallpaper.producer.decorater;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Composite;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.util.Collections;

import module.wallpaper.utilities.Config;
import module.wallpaper.utilities.Context;

public class PainterBackground {
    private BufferedImage image;
    private Context context;

    public PainterBackground(Context context) {
        this.context = context;
        image = new BufferedImage(context.getConfig().width, context.getConfig().height, BufferedImage.TYPE_INT_RGB);
    }

    public BufferedImage paint(BufferedImage source, int top, int width, int height) {

        image.getGraphics().drawImage(source, 0, 0, null);

        Config config = Config.getInstance();

        Graphics2D g2 = (Graphics2D) image.getGraphics();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // -- Paint Upper Background ----------------------------------------------------------

        Composite composite = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.06f);
        g2.setComposite(composite);
        g2.setPaint(Color.black);
        g2.fillRect(0, 0, image.getWidth(), top);

        // -- Paint Art Background ------------------------------------------------------------

        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.7f));
        int nw = width + config.art_padding;
        int art_background_height = image.getHeight() - top - config.taskbar - config.passepartout * 2;
        int x = image.getWidth() - nw - config.passepartout;
        int y = top + config.passepartout;
        g2.setPaint(Color.black);
        g2.fillRoundRect(x, y, nw, art_background_height, 20, 20);

        // -- Paint Month Name ----------------------------------------------------------------

        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.9f));
        g2.setColor(Color.WHITE);
        g2.setFont(config.getFont());
        FontMetrics fm = g2.getFontMetrics();
        String s = context.getMonthName() + " " + context.getYearName();
        x = image.getWidth() - config.passepartout - config.art_padding - fm.stringWidth(s);
        y = top + config.passepartout + (art_background_height - context.getArt().getHeight() - config.art_padding)/2;
        g2.drawString(s, x, y+fm.getAscent()/2);

        return image;
    }
}
