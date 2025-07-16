package ui;

import javax.microedition.lcdui.Canvas;
import app.MainMIDlet;

public abstract class AbstractCanvas extends Canvas implements Runnable {
    protected final MainMIDlet midlet;
    private Thread gameThread;
    private volatile boolean running = false;

    private static final int TARGET_FPS = 30;
    private static final long OPTIMAL_TIME = 1000L / TARGET_FPS;

    private long fps;
    private long lastFpsTime;
    private long frameCount;

    public AbstractCanvas(MainMIDlet midlet) {
        this.midlet = midlet;
    }

    public synchronized void start() {
        if (running) return; // Предотвращаем двойной запуск
        setFullScreenMode(true);
        running = true;
        gameThread = new Thread(this, "GameThread");
        gameThread.start();
    }

    public synchronized void stop() {
        running = false;
        if (gameThread != null) {
            try {
                gameThread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            gameThread = null;
        }
    }

    public long getFPS() {
        return fps;
    }

    public void destroy() {
        stop();
    }

    public void run() {
        long lastLoopTime = System.currentTimeMillis();
        long now;
        long updateLength;
        long sleepTime;

        lastFpsTime = 0;
        frameCount = 0;

        while (running) {
            now = System.currentTimeMillis();
            updateLength = now - lastLoopTime;
            lastLoopTime = now;

            // Обновляем FPS каждые 1000 мс
            lastFpsTime += updateLength;
            frameCount++;

            if (lastFpsTime >= 1000) {
                fps = frameCount;
                frameCount = 0;
                lastFpsTime = 0;
            }

            update();   // Обновление логики
            repaint();  // Перерисовка

            sleepTime = OPTIMAL_TIME - (System.currentTimeMillis() - lastLoopTime);
            if (sleepTime > 0) {
                try {
                    Thread.sleep(sleepTime);
                } catch (InterruptedException e) {
                    // Поток мог быть остановлен извне
                    break;
                }
            } else {
                // Кадр пропущен: система перегружена
                // Здесь можно логировать или уменьшать нагрузку
            }
        }
    }

    protected abstract void update(); // Игровая логика
}
