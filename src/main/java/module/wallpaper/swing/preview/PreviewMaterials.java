package module.wallpaper.swing.preview;

import module.wallpaper.image.ImageLoader;
import module.wallpaper.utilities.Config;

import java.util.List;
import java.net.URL;

public class PreviewMaterials {
    private List<URL> backs, arts;

    public PreviewMaterials() {
        Config config = Config.getInstance();
        ImageLoader loader = new ImageLoader();
        backs = loader.listLocalImageNames(config.DIR_BACKGROUND);
        arts = loader.listLocalImageNames(config.DIR_ART);
    }

    public List<URL> getBacks() {
        return backs;
    }

    public List<URL> getArts() {
        return arts;
    }
}
