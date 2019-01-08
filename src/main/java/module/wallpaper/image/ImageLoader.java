package module.wallpaper.image;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

import javax.imageio.ImageIO;

import common.Log;

public class ImageLoader {
    HashMap<URL, BufferedImage> cache = new HashMap<URL, BufferedImage>();

    public List<URL> listLocalImageNames(String directory) {
        ArrayList<URL> list = new ArrayList<URL>();
        try {
            File dir = new File(directory);
            File[] files = dir.listFiles();
            Arrays.sort(files, new Comparator<File>() {
                public int compare(File lhs, File rhs) {
                    return new Long(rhs.lastModified()).compareTo(lhs.lastModified());
                }
            });
            for (File file : files) {
                if (!file.isDirectory())
                    list.add(file.toURI().toURL());
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return list;
    }

    public List<BufferedImage> loadAllImages(List<URL> urls) {
        int size = urls.size();
        ArrayList<BufferedImage> images = new ArrayList<BufferedImage>();
        for (URL url : urls) {
            images.add(loadImage(url));
        }
        return images;
    }

    public BufferedImage loadImage(URL url) {
        BufferedImage image = null;//cache.get(url); TODO: Rethink cache for images. App runs out of heap when on !

        if (image == null) {
            Log.log("Not in cache, trying to load: " + url.toString());

            if (url != null) {
                image = null;

                try {
                    InputStream is = url.openStream();
                    image = ImageIO.read(is);
                    is.close();
                    Log.log("OK! Loaded");
                    cache.put(url, image);
                    Log.log("Added to local cache");
                } catch (Exception ex) {
                    Log.log("Error: " + ex.getMessage());
                }
            } else
                Log.log("Error: URL is null!");
        } else {
            Log.log("Cached image used (" + url.toString() + ")");
        }

        return image;
    }

    public void save(BufferedImage image, File file) throws Exception {
        Log.log("Saving Image : " + file.getName());
        if (!file.exists()) {
            ImageIO.write(image, "png", new FileOutputStream(file));
        }
    }
}