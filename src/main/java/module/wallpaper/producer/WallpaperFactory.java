package module.wallpaper.producer;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.net.URL;

import module.wallpaper.image.ImageCutter;
import module.wallpaper.image.ImageLoader;
import module.wallpaper.markers.Markers;
import module.wallpaper.producer.decorater.PainterBackground;
import module.wallpaper.producer.decorater.PainterCalendar;
import module.wallpaper.utilities.Config;
import module.wallpaper.utilities.Context;

public class WallpaperFactory {

    private ImageLoader loader = new ImageLoader();

    public BufferedImage buildWallpaper(URL backgroundURL, URL artURL) {
        Markers markers = Config.getInstance().getMarkers();
        PainterCalendar painter = new PainterCalendar(markers);

        // -- Background -----------------------------------------------------------------------------------------

        Config config = Config.getInstance();

        BufferedImage wallpaper = new BufferedImage(config.width,config.height,BufferedImage.TYPE_INT_RGB);
        BufferedImage back = loader.loadImage(backgroundURL);
        Graphics2D g2 = wallpaper.createGraphics();
        g2.drawImage(back,0,0,config.width,config.height,null);
        BufferedImage art = loader.loadImage(artURL);
        Context ctx = new Context(Config.getInstance(), null, wallpaper, art);

        painter.getSize();

        int x = config.width - art.getWidth() - config.passepartout - config.frame * 2;
        int y = config.height - art.getHeight() - config.taskbar - config.passepartout - config.frame * 2;
        BufferedImage image = new PainterBackground(ctx).paint(
                wallpaper,
                painter.getSize().height + config.passepartout * 2,
                art.getWidth() + config.frame * 2, art.getHeight() + config.frame * 2
        );
        image = ImageCutter.paste(image, art, x, y);
        image = ImageCutter.drawInnerFrame(image, art, x, y);

        // -- Calendars ------------------------------------------------------------------------------------------

        ctx.roll(-1);
        for (int index = 0; index < config.calendars; index++) {
            if (index == 1) {
                painter.setTheme(Config.getInstance().themeNow);
            } else {
                painter.setTheme(Config.getInstance().themeNormal);
            }
            image = paintCalendar(image, painter, config.calendars - index, ctx);
            ctx.roll(1);
        }

        return image;
    }

    private BufferedImage paintCalendar(BufferedImage image, PainterCalendar painter, int index, Context ctx) {
        int margin = 30;
        return painter.paint(image, image.getWidth() - painter.getSize().width * index - margin * index, margin, ctx.getMonth(), ctx.getYear());
    }
}
