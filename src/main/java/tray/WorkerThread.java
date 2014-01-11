package tray;

import module.tracker.Mediator;

public class WorkerThread implements Runnable {
    private Mediator mediator;
    private boolean running = false;

    public WorkerThread(Mediator mediator) {
        this.running = true;
        this.mediator = mediator;
    }

    public void startup() {
        this.running = true;
        new Thread(this).start();
    }

    public void end() {
        this.running = false;
    }

    public void run() {
        while (running) {
            mediator.getTrackerModel().update();
            try {
                Thread.sleep(1000);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }
}
