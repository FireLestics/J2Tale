package j2tale_base.scenes;

import javax.microedition.lcdui.Canvas;
import javax.microedition.lcdui.Graphics;

public abstract class Scene extends Canvas {
    protected SceneManager manager;

    public Scene(SceneManager manager) {
        this.manager = manager;
        setFullScreenMode(true); // Автоматически включаем fullscreen
    }

    public abstract void update(); // вызывается из глобального цикла
    public abstract void paint(Graphics g); // вызывается repaint()
}
