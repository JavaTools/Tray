package module.tracker;

import common.Log;
import common.Time;
import module.tracker.view.StatusView;
import module.tracker.view.TrackerView;
import module.tracker.view.WeekView;
import module.wallpaper.utilities.Config;
import tray.TrayDateIconImage;
import tray.WorkerThread;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class TrackerFrame extends JFrame {
    private WorkerThread workerThread;
    private StatusView status;
    private WeekView weekView;
    private Mediator mediator;

    public TrackerFrame(Mediator mediator) {
        super("Time Tracker");
        this.mediator = mediator;
        setup();
    }

    @Override
    public void setVisible(boolean b) {
        center();
        worker(b);
        super.setVisible(b);
    }

    private void worker(boolean visible) {
        if (visible) {
            Log.log(Time.getStamp() + " start thread");
            workerThread.startup();
        } else {
            workerThread.end();
        }
    }

    public void center() {
        Dimension size = getSize();
        Rectangle base = new Rectangle(Toolkit.getDefaultToolkit().getScreenSize());
        setLocation(base.x + (base.width - size.width) / 2, base.y + (base.height - size.height) / 2);
    }

    private void setup() {
        setSize(Config.getInstance().WINDOW_WIDTH, Config.getInstance().WINDOW_HEIGHT);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                setVisible(false);
            }
        });
        setBackground(Config.getInstance().COLOR_WINDOW_BACKGROUND);
        setIconImage(new TrayDateIconImage());
//        setUndecorated(true);

        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ESCAPE)
                    setVisible(false);
                else if (e.getKeyCode() == KeyEvent.VK_LEFT)
                    mediator.getTrackerModel().scroll(-1);
                else if (e.getKeyCode() == KeyEvent.VK_RIGHT)
                    mediator.getTrackerModel().scroll(1);
            }
        });

        addMouseWheelListener(new MouseWheelListener() {
            public void mouseWheelMoved(MouseWheelEvent e) {
                if (e.getUnitsToScroll() > 0)
                    mediator.getTrackerModel().scroll(1);
                else
                    mediator.getTrackerModel().scroll(-1);
            }
        });

        workerThread = new WorkerThread(mediator);

        TrackerView view = mediator.getView();

        JPanel tableInnerPanel = new JPanel(new BorderLayout());
        tableInnerPanel.setBackground(Color.WHITE);
        tableInnerPanel.setBorder(BorderFactory.createEmptyBorder(Config.getInstance().WINDOW_INNER_MARGIN, Config.getInstance().WINDOW_INNER_MARGIN, Config.getInstance().WINDOW_INNER_MARGIN, Config.getInstance().WINDOW_INNER_MARGIN));
        tableInnerPanel.add(BorderLayout.CENTER, view);

        JPanel tableOuterPanel = new JPanel(new BorderLayout());
        tableOuterPanel.setBackground(Config.getInstance().COLOR_WINDOW_BACKGROUND);
        tableOuterPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createEmptyBorder(Config.getInstance().WINDOW_MARGIN, Config.getInstance().WINDOW_MARGIN, Config.getInstance().WINDOW_MARGIN, Config.getInstance().WINDOW_MARGIN),
                BorderFactory.createLineBorder(Config.getInstance().darker(Config.getInstance().COLOR_WINDOW_BACKGROUND, 0.2))
        ));
        tableOuterPanel.add(BorderLayout.CENTER, tableInnerPanel);

        // -- Dialog top bar ------------------------------------------------------------------------------------

        JPanel top = new JPanel(new BorderLayout());
        top.setBorder(BorderFactory.createEmptyBorder(10, 10, 0, 10));
        top.setBackground(Config.getInstance().COLOR_WINDOW_BACKGROUND);
        top.setOpaque(true);

        weekView = mediator.getWeek();
        weekView.setFont(Config.getInstance().FONT_WINDOW);
        top.add(BorderLayout.WEST, weekView);

        status = mediator.getStatus();
        status.setFont(Config.getInstance().FONT_WINDOW);
        top.add(BorderLayout.CENTER, status);

        Container c = getContentPane();
        c.setLayout(new BorderLayout());
        c.add(BorderLayout.NORTH, top);
        c.add(BorderLayout.CENTER, tableOuterPanel);
    }
}