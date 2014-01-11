package tray;

import common.Time;
import module.tracker.Mediator;
import module.wallpaper.utilities.Config;
import common.Log;
import tray.model.Dao;

import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

import module.wallpaper.MainWallpaper;

public class TrayDateApplication {
    private TrayIcon trayIcon;
    private Mediator mediator;

    public TrayDateApplication(Mediator mediator) {
        this.mediator = mediator;
    }

    public void refresh() {
        TrayDateIconImage image = (TrayDateIconImage) trayIcon.getImage();
        if (image.shouldChange()) {
            trayIcon.setImage(new TrayDateIconImage());
            image.flush();
            trayIcon.displayMessage(
                    "New Day ! ! !",
                    "Just passed midnight.\nYou should stop working",
                    TrayIcon.MessageType.INFO
            );
        }
    }

    public void run() {
        if (SystemTray.isSupported()) {
            trayIcon = new TrayIcon(new TrayDateIconImage(), Config.getInstance().TOOLTIP + ", week is " + Time.getWeek());
            trayIcon.setImageAutoSize(true);
            trayIcon.setPopupMenu(getMenu(mediator.getMainWallpaper()));
            trayIcon.addMouseListener(new TrayMouseListener(mediator));

            try {
                SystemTray.getSystemTray().add(trayIcon);
            } catch (AWTException e) {
                System.err.println("TrayIcon could not be added.");
            }
        } else {
            Log.log("ERROR: System Tray is not supported");
        }
    }

    private PopupMenu getMenu(final MainWallpaper mainWallpaper) {
        PopupMenu popup = new PopupMenu();

        MenuItem item = new MenuItem("Edit Week");
        item.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    String currentWeek = Config.getInstance().getFilename(Config.getInstance().DIR_STAMPS, Time.getWeek());
                    Runtime.getRuntime().exec("notepad.exe " + currentWeek);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });
        popup.add(item);

        item = new MenuItem("Wallpaper");
        item.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                mainWallpaper.run();
            }
        });
        popup.add(item);
        item = new MenuItem("Wallpaper (force)");
        item.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                mainWallpaper.runForce();
            }
        });
        popup.add(item);

        popup.addSeparator();

        item = new MenuItem("Shutdown");
        item.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    new Dao().stamp(false);
                    Runtime rt = Runtime.getRuntime();
                    rt.exec("shutdown.exe -s -t 0");
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });
        popup.add(item);

        popup.addSeparator();

        item = new MenuItem("Audi.dk");
        item.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    java.awt.Desktop.getDesktop().browse(java.net.URI.create(Config.getInstance().URL_AUDI_IMAGES));
                } catch (java.io.IOException ex) {
                    System.out.println("Exception (TrayDateApplication.java:119): " + ex.getMessage());
                }
            }
        });
        popup.add(item);

        item = new MenuItem("Exit");
        item.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                new Dao().stamp(false);
                System.exit(0);
            }
        });
        popup.add(item);

        return popup;
    }
}