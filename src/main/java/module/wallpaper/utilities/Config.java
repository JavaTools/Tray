package module.wallpaper.utilities;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Toolkit;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.Properties;
import java.util.Set;

import common.Log;
import module.wallpaper.markers.Markers;

/**
 * Class for holding configuration parameters. The class defines sensible defaults and
 * through the load method, a properties file can be used to overwrite these defaults.
 */
public class Config {

    // --------------------------------------------------------------------------------------------
    // -- Filesystem and URLs ---------------------------------------------------------------------
    // --------------------------------------------------------------------------------------------

    public String DIR_ART = "data\\images\\art";
    public String DIR_BACKGROUND = "data\\images\\background";
    public String DIR_STAMPS = "data\\stamps";

    public String FILE_MARKERS = "markers.xml";
    public String FILE_WALLPAPER = "data\\wallpaper.bmp";

    public String URL_AUDI_IMAGES = "http://www.audi.dk/dk/brand/da/experience/gallery/Billed_kal.html";
    public String URL_AUDI_REGEX = "<a[^>]+href=\"([^\"]+)\"[^>]+title=\"1024x768\"[^>]*>";
    public String URL_ART_DEFAULT = DIR_ART + "\\default.png";

    // --------------------------------------------------------------------------------------------
    // -- Wallpaper, image manipulation constants -------------------------------------------------
    // --------------------------------------------------------------------------------------------

    public int audi_x = 0;
    public int audi_y = 96;
    public int audi_width = 1280;
    public int audi_height = 720;

    public int DEBUG = 1;

    public int calendars = 6;
    public int width = (int)Toolkit.getDefaultToolkit().getScreenSize().getWidth(); //1920;
    public int height = (int)Toolkit.getDefaultToolkit().getScreenSize().getHeight(); //1080;
    public int passepartout = 80;
    public int frame = 14;
    public int innerFrame = 20;
    public int taskbar = 30;
    public int margin = 8;
    public int arc = 8;
    public int pad = 8;
    public int line_separator = 8;


    // --------------------------------------------------------------------------------------------
    // -- Fonts and Colors ------------------------------------------------------------------------
    // --------------------------------------------------------------------------------------------

    public Theme themeNormal = new Theme();
    public Theme themeNow = new Theme();
    private Font font;

    public Integer WINDOW_WIDTH = 396;
    public Integer WINDOW_HEIGHT = 294;

    public Font FONT_WINDOW = new Font("Calibri", Font.BOLD, 24);
    public Font FONT_ICON = new Font("Arial", Font.BOLD, 11);
    public Color COLOR_ICON_BACKGROUND = new Color(0xFFddFF);
    public Color COLOR_ICON_FRAME = new Color(0x476add);
    public Color COLOR_ICON_TEXT = new Color(0x800000);
    public Color COLOR_WINDOW_BACKGROUND = new Color(0xd4d0c8);
    public Integer WINDOW_MARGIN = 8;
    public Integer WINDOW_INNER_MARGIN = 3;
    public Integer WINDOW_TOTALS_HEIGHT = 35;

    public String TOOLTIP = "Tray by Claus Ljunggren";

    // --------------------------------------------------------------------------------------------
    // -- Instance variable -----------------------------------------------------------------------
    // --------------------------------------------------------------------------------------------

    private static Config config = new Config();

    public static Config getInstance(String configFile) {
        config.loadConfig(configFile);
        return config;
    }

    public static Config getInstance() {
        return config;
    }

    private Config() {
        font = loadFont("agel____.pfb",Font.TYPE1_FONT,64F);
//        FONT_ICON = loadFont("OpenSansCondensed-Bold.ttf", Font.TRUETYPE_FONT,16f);
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        width = (int)screenSize.getWidth();
        height = (int)screenSize.getHeight();
    }

    private Font loadFont(String name, int type, float size) {
        Font result = null;
        InputStream is = getClass().getClassLoader().getResourceAsStream("fonts/"+name);
        try {
            result = Font.createFont(type, is).deriveFont((size));
            is.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return result;
    }

    /**
     * Loads configuration file and overrides static members of configuration classes
     *
     * @param config The filename of the config file to read
     */
    public void loadConfig(String config) {
        if (config != null) {
            try {
                File configurationFile = new File(config);
                Log.log("Trying to load configuration. Supplied parameter is: \"" + config + "\" ....");
                if (configurationFile.exists()) {
                    Properties properties = new Properties();
                    properties.load(new FileInputStream(configurationFile));
                    Set names = properties.keySet();
                    Log.log(" -> Okay, found " + names.size() + " properties defined in configuration, overloading defaults");
                    for (Object obj : names) {
                        String name = (String) obj;
                        if (!name.contains(".")) {
                            Field field = getClass().getDeclaredField(name);
                            if (field.getType() == Integer.class || field.getType() == int.class)
                                field.setInt(this, Integer.parseInt(properties.getProperty(name), 10));
                            if (field.getType() == String.class)
                                field.set(this, properties.getProperty(name));
                        } else {
                            int index = name.lastIndexOf(".");
                            String varName = name.substring(0, index);
                            String fieldName = name.substring(index + 1);
                            String fieldValue = properties.getProperty(name);
                            if (fieldValue != null) {
                                setNewValue(varName, fieldName, fieldValue);
                            }
                        }
                    }
                } else {
                    Log.log(" -> Configuration file: \"" + configurationFile.getAbsolutePath() + "\" doesn't exists, using default values.");
                }

            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    private void setNewValue(String varName, String fieldName, String value) {
        try {
            Field field = getClass().getDeclaredField(varName);
            Theme theme = (Theme) field.get(this);
            Field target = theme.getClass().getDeclaredField(fieldName);
            if (target.getType() == Color.class) {
                Color color = getColor(value);
                target.set(theme, color);
            }
            if (target.getType() == String.class) {
                target.set(theme, value);
            }
            if (target.getType() == Integer.class) {
                target.set(theme, Integer.parseInt(value, 10));
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public Color darker(Color color, double fraction) {
        int red = (int) Math.round(color.getRed() * (1.0 - fraction));
        int green = (int) Math.round(color.getGreen() * (1.0 - fraction));
        int blue = (int) Math.round(color.getBlue() * (1.0 - fraction));

        if (red < 0) red = 0;
        else if (red > 255) red = 255;
        if (green < 0) green = 0;
        else if (green > 255) green = 255;
        if (blue < 0) blue = 0;
        else if (blue > 255) blue = 255;

        int alpha = color.getAlpha();

        return new Color(red, green, blue, alpha);
    }

    public Color lighter(Color color, double fraction) {
        int red = (int) Math.round(color.getRed() * (1.0 + fraction));
        int green = (int) Math.round(color.getGreen() * (1.0 + fraction));
        int blue = (int) Math.round(color.getBlue() * (1.0 + fraction));

        if (red < 0) red = 0;
        else if (red > 255) red = 255;
        if (green < 0) green = 0;
        else if (green > 255) green = 255;
        if (blue < 0) blue = 0;
        else if (blue > 255) blue = 255;

        int alpha = color.getAlpha();

        return new Color(red, green, blue, alpha);
    }

    private Color getColor(String scode) {
        return new Color(Integer.parseInt(scode.substring(1), 16));
    }

    public Font getFont() {
        return font;
    }

    public Markers getMarkers() {
        File file = new File(FILE_MARKERS);
        Log.log("Markers file = " + file);
        return new Markers(file);
    }

    public String getFilename(String sub, String base) {
        return sub + "\\" + base + ".txt";
    }
}
