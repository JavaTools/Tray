package module.wallpaper.swing.calendar;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;

public class FrameCalendar extends JFrame {
    private BufferedImage wpaper;

    public FrameCalendar(BufferedImage wallpaper) throws HeadlessException {
        super("Calendar FrameCalendar");
        this.wpaper = wallpaper;
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent we) {
                System.exit(0);
            }
        }
        );
    }

    public void paint(Graphics g) {
        Dimension d = getSize();
        Graphics2D g2 = (Graphics2D) g;
        g2.setColor(new Color(0xd4d0c8));
        g2.fillRect(0, 0, d.width, d.height);
        g2.drawImage(wpaper, (d.width - wpaper.getWidth()) / 2, (d.height - wpaper.getHeight()) / 2, this);
    }
}
