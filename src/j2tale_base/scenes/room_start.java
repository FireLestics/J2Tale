package j2tale_base.scenes;

import javax.microedition.lcdui.*;
import java.io.IOException;
import j2tale_base.Midlet;
import j2tale_base.tools.ImageDrawer;
import j2tale_base.tools.TextBlitter;

public class room_start extends Scene {
    private Midlet midlet;
    private ImageDrawer imageDrawer;
    private TextBlitter textBlitter;

    private int width;
    private int height;
    private int logoWidth, logoHeight;
    private int logoX, logoY;
    private int timer;
    
    private boolean onIntro;
    
    private boolean error = false;
    private String errorText;
    private int errorCode;
    
    public room_start(SceneManager manager) {
        super(manager);
        this.midlet = manager.getMidlet();
        imageDrawer = new ImageDrawer();
        textBlitter = new TextBlitter();
        try {
            textBlitter.loadFont("fnt_small", "default");  // Загружаем шрифт, указывая дефолтный цвет
        } catch (IOException e) {}
        
        this.onIntro = midlet.getBooleanData("onIntro");
        midlet.loadAllKeyCode();
    }
    
    public void update() {
        this.timer++;
        if (timer >= 30) {
            if ("240x320".equals(width + "x" + height)) {
                pass(onIntro);
            } else if ("240x309".equals(width + "x" + height)) {
                pass(onIntro);
            } else {
                if (!error) {
                    this.errorCode = 0;
                    this.errorText = "Неподходящее разрешение: " + width + "x" + height;
                    System.out.println(errorText);
                    this.error = true;
                }
            }
        }
    }
    
    private void pass(boolean onIntro) {
        if (onIntro) {
            manager.setScene(new room_introstory(manager));
        } else {
            manager.setScene(new room_introimage(manager));
        }
    }

    public void paint(Graphics g) {
        this.width = getWidth();
        this.height = getHeight();
        
        g.setColor(0x000000);
        g.fillRect(0, 0, getWidth(), getHeight());
        
        logoView(g, width, height);
        if (error) {
            if (midlet.ifSmallScreen(width, height)) {
                textBlitter.setFont("fnt_small", "white");
                textBlitter.drawString(g, errorText, (width / 2) - (textBlitter.stringWidth(errorText) / 2), logoY + 40);
            } else {
                textBlitter.setFont("fnt_small", "white");
                textBlitter.drawString(g, errorText, (width / 2) - (textBlitter.stringWidth(errorText) / 2), logoY + 30);
            }
        }
    }

    protected void keyPressed(int keyCode) {
        // manager.setScene(new GameScene(manager));
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
