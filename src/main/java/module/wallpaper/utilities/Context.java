package module.wallpaper.utilities;

import module.wallpaper.markers.Markers;
import module.wallpaper.swing.preview.PreviewMaterials;

import java.awt.image.BufferedImage;
import java.util.Calendar;
import java.util.GregorianCalendar;


public class Context {
    private Config config;
    private PreviewMaterials materials;
    private Markers markers = new Markers();
    private BufferedImage back, art;
    private Calendar calendar;
    private String[] months = {
            "Januar", "Februar", "Marts", "April", "Maj", "Juni",
            "Juli", "August", "September", "Oktober", "November", "December"
    };

    public Context(Config config, Markers markers, BufferedImage back, BufferedImage art) {
        this.config = config;
        this.markers = markers;
        this.back = back;
        this.art = art;
        this.calendar = new GregorianCalendar();
    }

    public int getMonth() {
        return calendar.get(Calendar.MONTH);
    }

    public String getMonthName() {
        return months[getMonth()];
    }

    public int getYear() {
        return calendar.get(Calendar.YEAR);
    }

    public String getYearName() {
        return "" + calendar.get(Calendar.YEAR);
    }

    public Config getConfig() {
        return config;
    }

    public void setConfig(Config config) {
        this.config = config;
    }

    public Markers getMarkers() {
        return markers;
    }

    public void setMarkers(Markers markers) {
        this.markers = markers;
    }

    public BufferedImage getBack() {
        return back;
    }

    public void setBack(BufferedImage back) {
        this.back = back;
    }

    public BufferedImage getArt() {
        return art;
    }

    public void setArt(BufferedImage art) {
        this.art = art;
    }

    public String toString() {
        return "";
    }

    public void roll(int delta) {
        calendar.add(Calendar.MONTH, delta);
//		int month = getMonth();
//		return (month + delta) % 12;
    }
}
