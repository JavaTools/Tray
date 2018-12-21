package module.wallpaper.utilities;

import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Properties;

import javax.imageio.ImageIO;

public class Settings {
    private Properties properties = null;

    public static boolean DEBUG = true;
    public static final String DEFAULT_WALLPAPER = "/default/default.jpg";

    public Settings(String propertiesFileName) {
        this.properties = new Properties();
        try {
            FileInputStream fis = new FileInputStream(propertiesFileName);
            properties.load(fis);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public String getProperty(String key) {
        return properties.getProperty(key);
    }

    public int getNumber(String key) {
        return Integer.parseInt(properties.getProperty(key));
    }

    public Color getColor(String key) {
        return new Color(Integer.decode(properties.getProperty(key)));
    }

    public static void setRenderingHints(Graphics2D g2) {
        g2.setRenderingHint(
                RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY
        );
        g2.setRenderingHint(
                RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON
        );
        g2.setRenderingHint(
                RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON
        );
    }

    public static BufferedImage paintGradient(
            BufferedImage src, int xf, int yf, Color c1, int xt, int yt, Color c2) {
        Graphics2D g2 = src.createGraphics();
        setRenderingHints(g2);
        g2.setPaint(new GradientPaint(xf, yf, c1, xt, yt, c2, false));
        g2.fill(new Rectangle2D.Double(xf, yf, xt, yt));
        return src;
    }

    public BufferedImage getDefaultImage(boolean background) {
        BufferedImage wallpaper = null;

        URL url = getClass().getResource("/default/default-audi.jpg");

        if (background) {
            url = getClass().getResource("/default/background.jpg");
        }

        try {
            InputStream is = url.openStream();
            wallpaper = ImageIO.read(is);
            is.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return wallpaper;

    }
}
