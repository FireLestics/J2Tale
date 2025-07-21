package j2tale_base.scenes;

import javax.microedition.lcdui.*;
import j2tale_base.Midlet;

public class room_empty extends Scene {
    private Midlet midlet;
    
    public room_empty(SceneManager manager) {
        super(manager);
        this.midlet = manager.getMidlet();
    }
    
    public void update() {
    }

    public void paint(Graphics g) {
        g.setColor(0x000000);
        g.fillRect(0, 0, getWidth(), getHeight());
    }

    protected void keyPressed(int keyCode) {
        // manager.setScene(new GameScene(manager));
    }
}
