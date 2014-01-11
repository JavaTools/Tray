package module.wallpaper.swing.preview;

import module.wallpaper.utilities.Windows;
import module.wallpaper.producer.WallpaperFactory;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;


public class PreviewFrame extends JFrame {
    private int indexArts, indexBackgrounds;
    private BufferedImage image;
    private WallpaperFactory factory;
    private Rectangle screen;
    private Font fontSmall = new Font("Consolas", Font.BOLD, 28);
    private PreviewMaterials materials;
    private boolean help;

    public PreviewFrame(WallpaperFactory factory, PreviewMaterials materials) {
        this.factory = factory;
        this.materials = materials;
        setUndecorated(true);
        addKeyListener(new PreviewKeyListener(this));
        buildWallpaper();
    }

    public void help(boolean newHelp) {
        if (help != newHelp) {
            help = newHelp;
            repaint();
        }
    }

    public void rollArt(int delta) {
        indexArts += delta;
        if (indexArts < 0)
            indexArts = materials.getArts().size() - 1;
        else if (indexArts > materials.getArts().size() - 1)
            indexArts = 0;
//		message(getGraphics(),"Loading:");
        buildWallpaper();
        repaint();
    }

    public void rollBackground(int delta) {
        indexBackgrounds += delta;
        if (indexBackgrounds < 0)
            indexBackgrounds = materials.getBacks().size() - 1;
        else if (indexBackgrounds > materials.getBacks().size() - 1)
            indexBackgrounds = 0;
        message(getGraphics(), "Loading:");
        buildWallpaper();
        repaint();
    }

    public void setVisible(boolean b) {
        screen = new Rectangle(Toolkit.getDefaultToolkit().getScreenSize());
        setBounds(0, 0, screen.width, screen.height);
        super.setVisible(b);
    }

    public void paint(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        if (help)
            message(g2, "Files:");
        else {
            g2.setColor(Color.BLACK);
            g2.fillRect(0, 0, getWidth(), getHeight());
            g2.drawImage(image, 0, 0, this);
            g2.dispose();
        }
    }

    private void buildWallpaper() {
        image = factory.buildWallpaper(materials.getBacks().get(indexBackgrounds), materials.getArts().get(indexArts));
    }

    private void message(Graphics g, String title) {
        g.setFont(fontSmall);
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, screen.width, screen.height);
        g.setColor(new Color(0xf4ff97));

        if (title != null) {
            g.drawString(title, 200, 300);
        }

        g.drawString(materials.getBacks().get(indexBackgrounds).toString(), 200, 375);
        g.drawString(materials.getArts().get(indexArts).toString(), 200, 420);
    }

    public void setWallpaper() {
        Windows.setWallpaper(image);
    }
}
