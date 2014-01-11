package module.wallpaper.image;

import module.wallpaper.utilities.Config;

import java.awt.image.BufferedImage;
import java.awt.*;

public class ImageCutter {
    public static BufferedImage paste(BufferedImage source, BufferedImage target, int x, int y) {
        Graphics2D g2 = (Graphics2D) source.getGraphics();
        g2.drawImage(target, x, y, null);
        g2.dispose();
        return source;
    }

    public static BufferedImage cutout(BufferedImage source, int x, int y, int width, int height) {
        BufferedImage result = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics g = result.getGraphics();
        g.drawImage(source, 0, 0, width, height, x, y, x + width, y + height, null);
        g.dispose();
        return result;
    }

    public static BufferedImage drawInnerFrame(BufferedImage source, BufferedImage target, int x, int y) {
        Graphics2D g2 = (Graphics2D) source.getGraphics();
        int frame = Config.getInstance().innerFrame;
        if (frame > 0) {
            Composite composite = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.3f);
            g2.setComposite(composite);
            g2.setStroke(new BasicStroke((float) frame));
            g2.drawRect(x + frame / 2, y + frame / 2, target.getWidth() - frame, target.getHeight() - frame);
        }
        g2.dispose();
        return source;
    }
}
