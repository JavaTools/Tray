package module.wallpaper.swing.preview;

import java.awt.event.KeyListener;
import java.awt.event.KeyEvent;

public class PreviewKeyListener implements KeyListener {
    private PreviewFrame previewFrame;

    public PreviewKeyListener(PreviewFrame previewFrame) {
        this.previewFrame = previewFrame;
    }

    public void keyTyped(KeyEvent e) {
    }

    public void keyPressed(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_ESCAPE:
                stop();
                break;
            case KeyEvent.VK_ENTER:
                previewFrame.setWallpaper();
                stop();
                break;
            case KeyEvent.VK_RIGHT:
                previewFrame.rollArt(-1);
                break;
            case KeyEvent.VK_LEFT:
                previewFrame.rollArt(1);
                break;
            case KeyEvent.VK_UP:
                previewFrame.rollBackground(1);
                break;
            case KeyEvent.VK_DOWN:
                previewFrame.rollBackground(-1);
                break;
            case KeyEvent.VK_F1:
                previewFrame.help(true);
                break;
            case KeyEvent.VK_F12:
                loadAudi();
                break;
        }
    }

    private void loadAudi() {

    }

    public void keyReleased(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_F1)
            previewFrame.help(false);
    }

    private void stop() {
        previewFrame.setVisible(false);
    }
}
