package j2tale_base.scenes;

public class SceneLoop implements Runnable {
    private SceneManager manager;
    private Thread loopThread;
    private boolean running = false;

    private int targetFPS = 30;
    private int currentFPS = 0;

    public SceneLoop(SceneManager manager) {
        this.manager = manager;
    }

    public void setTargetFPS(int fps) {
        this.targetFPS = fps;
    }

    public int getCurrentFPS() {
        return currentFPS;
    }

    public void start() {
        if (running) return;
        running = true;
        loopThread = new Thread(this);
        loopThread.start();
    }

    public void stop() {
        running = false;
    }

    public void run() {
        long frameDuration = 1000 / targetFPS;
        long lastFpsTime = System.currentTimeMillis();
        int frames = 0;

        while (running) {
            long startTime = System.currentTimeMillis();

            Scene current = manager.getCurrentScene();
            if (current != null) {
                current.update();
                current.repaint();
            }

            frames++;
            long now = System.currentTimeMillis();
            if (now - lastFpsTime >= 1000) {
                currentFPS = frames;
                frames = 0;
                lastFpsTime = now;
            }

            long elapsed = System.currentTimeMillis() - startTime;
            long sleep = frameDuration - elapsed;
            if (sleep > 0) {
                try {
                    Thread.sleep(sleep);
                } catch (InterruptedException e) {}
            } else {
                // если просрочили — не спим вообще (нагрузка слишком высокая)
            }
        }
    }
}
