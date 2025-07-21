package j2tale_base.scenes;

import javax.microedition.lcdui.*;
import java.io.IOException;
import j2tale_base.Midlet;
import j2tale_base.tools.ImageDrawer;
import j2tale_base.tools.TextBlitter;

public class room_introimage extends Scene {
    private Midlet midlet;
    private ImageDrawer imageDrawer;
    private TextBlitter textBlitter;

    private int width;
    private int height;
    private int logoWidth, logoHeight;
    private int logoX, logoY;
    private int timer;
    
    private boolean onIntro;
    
    public room_introimage(SceneManager manager) {
        super(manager);
        this.midlet = manager.getMidlet();
        imageDrawer = new ImageDrawer();
        textBlitter = new TextBlitter();
        try {
            textBlitter.loadFont("fnt_small", "default");  // Загружаем шрифт, указывая дефолтный цвет
        } catch (IOException e) {}
        
        this.onIntro = midlet.getBooleanData("onIntro");
    }
    
    public void update() {
        this.timer++;
        if (timer >= 1500) {
            if (onIntro) {
                manager.setScene(new room_introstory(manager));
            } else {
                manager.setScene(new room_introimage(manager));
            }
        }
    }

    public void paint(Graphics g) {
        this.width = getWidth();
        this.height = getHeight();
        
        g.setColor(0x000000);
        g.fillRect(0, 0, getWidth(), getHeight());
        
        String text = "[PRESS Z or ENTER]";
        
        logoView(g, width, height);
        if (timer >= 80) {
            if (midlet.ifSmallScreen(width, height)) {
                textBlitter.setFont("fnt_small", "white");
                textBlitter.drawString(g, text, (width / 2) - (textBlitter.stringWidth(text) / 2), logoY + 40);
            } else {
                textBlitter.setFont("fnt_small", "white");
                textBlitter.drawString(g, text, (width / 2) - (textBlitter.stringWidth(text) / 2), logoY + 30);
            }
        }
    }

    protected void keyPressed(int keyCode) {
        manager.setScene(new room_intromenu(manager));
    }
    
    private void logoView(Graphics g, int width, int height) {
        if (midlet.ifSmallScreen(width, height)) {
            this.logoWidth = 118;
            this.logoHeight = 12;
            this.logoX = (width / 2) - (logoWidth / 2);
            this.logoY = (height / 2) - (logoHeight / 2);
            imageDrawer.drawImage(g, "/images/logo_small.png", logoX, logoY, 0, 0);
        } else {
            this.logoWidth = 237;
            this.logoHeight = 24;
            this.logoX = (width / 2) - (logoWidth / 2);
            this.logoY = (height / 2) - (logoHeight / 2);
            imageDrawer.drawImage(g, "/images/logo.png", logoX, logoY, 0, 0);
        }
    }
}
