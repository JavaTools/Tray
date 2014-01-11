package module.wallpaper.utilities;

import java.awt.*;

public class Theme {
    public Color colorTitle = new Color(0x000000);
    public Color colorDates = new Color(0x000000);
    public Color colorDays = new Color(0x333333);
    public Color colorWeeks = new Color(0x555555);
    public Color colorGradientOuterDark = new Color(0x99A2A9);
    public Color colorGradientOuterLight = new Color(0xCBCED5);
    public Color colorGradientInnerDark = new Color(0xDDDDDD);
    public Color colorGradientInnerLight = new Color(0xFFFFFF);
    public Color colorGradientSundayDark = new Color(0xFFBBBB);
    public Color colorGradientSundayLight = new Color(0xFFDDDD);

    private String fontname = "Consolas";
    public Font fontNormal = new Font(fontname, Font.PLAIN, 14);
    public Font fontWeek = new Font(fontname, Font.BOLD, 14);
    public Font fontDays = new Font(fontname, Font.BOLD, 16);
    public Font fontTitle = new Font("Arial", Font.BOLD, 18);
}
