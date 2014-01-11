package module.wallpaper.producer.decorater;

import module.wallpaper.utilities.Config;

import java.awt.image.BufferedImage;
import java.awt.*;

public class PainterTest {
    public BufferedImage paint(BufferedImage source) {
        Graphics2D g2 = source.createGraphics();

        double fraction = 0.1;
        Config config = Config.getInstance();
        Color c = new Color(0x99A2A9);
        g2.setColor(c);
        g2.fillRect(200, 100, 100, 100);
        g2.setColor(config.lighter(c, fraction));
        g2.fillRect(300, 100, 100, 100);
        g2.setColor(config.darker(c, fraction));
        g2.fillRect(100, 100, 100, 100);
        return source;
    }
}
