package tray;

import module.tracker.Mediator;
import module.wallpaper.utilities.Config;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class TrayMouseListener extends MouseAdapter {

    private Mediator mediator;

    public TrayMouseListener(Mediator mediator) {
        this.mediator = mediator;
    }

    public void mouseClicked(MouseEvent e) {
        if (e.getButton() == MouseEvent.BUTTON1) {
            mediator.getFrame().setSize(Config.getInstance().WINDOW_WIDTH, Config.getInstance().WINDOW_HEIGHT);
            mediator.getFrame().setVisible(true);
        }
    }
}
