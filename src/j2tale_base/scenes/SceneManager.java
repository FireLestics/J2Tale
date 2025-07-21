package j2tale_base.scenes;

import j2tale_base.Midlet;
import javax.microedition.lcdui.Display;

public class SceneManager {
    private Midlet midlet;
    private Display display;
    private Scene currentScene;

    private SceneLoop loop;

    public SceneManager(Midlet midlet) {
        this.midlet = midlet;
        this.display = Display.getDisplay(midlet);
        this.loop = new SceneLoop(this);
        this.loop.setTargetFPS(33); // Устанавливаем начальный FPS
        this.loop.start(); // Запускаем единый игровой цикл
    }

    public void setScene(Scene scene) {
        this.currentScene = scene;
        display.setCurrent(scene);
    }

    public Scene getCurrentScene() {
        return currentScene;
    }
    
    public Midlet getMidlet() {
        return midlet;
    }
    
    public int getFPS() {
        return loop.getCurrentFPS();
    }

    public void setTargetFPS(int fps) {
        loop.setTargetFPS(fps);
    }
}