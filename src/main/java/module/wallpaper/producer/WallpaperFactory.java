package module.wallpaper.producer;

import module.wallpaper.utilities.Config;
import module.wallpaper.utilities.Context;
import module.wallpaper.markers.Markers;
import module.wallpaper.image.ImageCutter;
import module.wallpaper.image.ImageLoader;
import module.wallpaper.producer.decorater.PainterBackground;
import module.wallpaper.producer.decorater.PainterCalendar;

import java.awt.image.BufferedImage;
import java.net.URL;

public class WallpaperFactory {
    private ImageLoader loader = new ImageLoader();

    public BufferedImage buildWallpaper(URL backgroundURL, URL artURL) {
        Markers markers = Config.getInstance().getMarkers();
        PainterCalendar painter = new PainterCalendar(markers);

        // -- Background -----------------------------------------------------------------------------------------

        Config cfg = Config.getInstance();
        BufferedImage back = loader.loadImage(backgroundURL);
        BufferedImage art = loader.loadImage(artURL);
        Context ctx = new Context(Config.getInstance(), null, back, art);

        int width = back.getWidth();
        int height = back.getHeight();

        int x = width - art.getWidth() - cfg.passepartout - cfg.frame * 2;
        int y = height - art.getHeight() - cfg.taskbar - cfg.passepartout - cfg.frame * 2;
        BufferedImage image = new PainterBackground(ctx).paint(
                back,
                painter.getSize().height + cfg.passepartout * 2,
                art.getWidth() + cfg.frame * 2, art.getHeight() + cfg.frame * 2
        );
        image = ImageCutter.paste(image, art, x, y);
        image = ImageCutter.drawInnerFrame(image, art, x, y);

        // -- Calendars ------------------------------------------------------------------------------------------

        ctx.roll(-1);
        for (int index = 0; index < cfg.calendars; index++) {
            if (index == 1) {
                painter.setTheme(Config.getInstance().themeNow);
            } else {
                painter.setTheme(Config.getInstance().themeNormal);
            }
            image = paintCalendar(image, painter, cfg.calendars - index, ctx);
            ctx.roll(1);
        }

        return image;
    }

    private BufferedImage paintCalendar(BufferedImage image, PainterCalendar painter, int index, Context ctx) {
        int margin = 30;
        return painter.paint(image, image.getWidth() - painter.getSize().width * index - margin * index, margin, ctx.getMonth(), ctx.getYear());
    }
}
