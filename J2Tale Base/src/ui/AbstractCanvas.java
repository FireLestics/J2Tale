package ui;

import javax.microedition.lcdui.Canvas;
import app.MainMIDlet;

public abstract class AbstractCanvas extends Canvas implements Runnable {
    protected MainMIDlet midlet;
	protected Thread gameThread;
    protected boolean running = false;
	
	protected static final int UPDATE_RATE = 30; // 30 обновлений в секунду
    protected static final long UPDATE_PERIOD = 1000 / UPDATE_RATE;
    protected long lag = 0;
	protected long fps;

	public AbstractCanvas(MainMIDlet midlet) {
		super();
		this.midlet = midlet;
	}
	
	public void start() {
		setFullScreenMode(true);
        running = true;
        if (gameThread == null || !gameThread.isAlive()) { // Проверка на запуск
            gameThread = new Thread(this);
            gameThread.start();
        }
    }
	
	public long getFPS() {
		return fps;
	}
	
	public void stop() {
        running = false;
        if (gameThread != null) {
            try {
				gameThread.join(); // Просто ждем завершения потока
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
            gameThread = null;
        }
    }
	
	public void run() {
		long startTime;
		long timeDiff;

		while (running) {
			startTime = System.currentTimeMillis();
			timeDiff = System.currentTimeMillis() - startTime;

			lag += timeDiff;

			while (lag >= UPDATE_PERIOD) {
				lag -= UPDATE_PERIOD;
			}

			repaint();

			try {
				long sleepTime = UPDATE_PERIOD - (System.currentTimeMillis() - startTime);
				if (sleepTime > 0) {
					Thread.sleep(sleepTime);
				}
			
			this.fps = sleepTime;
			} catch (InterruptedException e) {
				e.printStackTrace();
				break; // Выходим из цикла при прерывании
			}
		}
	}
	
    public void destroy() {
        stop(); // Останавливаем поток
        gameThread = null; // Освобождаем ссылку
    }
}