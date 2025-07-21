package j2tale_base.scenes;

import javax.microedition.lcdui.*;
import java.io.IOException;
import j2tale_base.Midlet;
import j2tale_base.tools.ImageDrawer;
import j2tale_base.tools.TextBlitter;

public class room_introstory extends Scene {
    private Midlet midlet;
    private ImageDrawer imageDrawer;
    private TextBlitter textBlitter;

    private int width;
    private int height;
    private int timer;
    
    private int intro;
    private int introWidth, introHeight;
    private int introX, introY, int11Y;
    private int typingLenght;
    private String[] introText;
    
    
    public room_introstory(SceneManager manager) {
        super(manager);
        this.midlet = manager.getMidlet();
        imageDrawer = new ImageDrawer();
        textBlitter = new TextBlitter();
        try {
            textBlitter.loadFont("fnt_maintext", "default");  // Загружаем шрифт, указывая дефолтный цвет
        } catch (IOException e) {}
        
        // midlet.playMIDI("mus_onceuponatime", 0, midlet.getBooleanData("onMusic"));
        midlet.playMIDI("/midi/mus_onceuponatime.mid", 0, true);
        
        this.intro = 0;
        this.int11Y = -187;
        this.introWidth = 240;
        this.introHeight = 320;
        
        this.introText = new String[] {
            "Long ago, two races\nruled over Earth:\nHUMANS and MONSTERS.",
            "One day, war broke\nout between the two\nraces.",
            "After a long battle,\nthe humans were\nvictorious.",
            "They sealed the monsters\nunderground with a magic\nspell.",
            "Many years later...",
            "               MT. EBOTT\n                   201X",
            "Legends say that those\nwho climb the mountain\nnever return.",
        };
    }
    
    public void update() {
        this.timer++;
        switch (timer) {
            case 220:
                nextIntro();
                break;
            case 390:
                nextIntro();
                break;
            case 530:
                nextIntro();
                break;
            case 700:
                nextIntro();
                break;
            case 930:
                nextIntro();
                break;
            case 1160:
                nextIntro();
                break;
            case 1390:
                nextIntro();
                break;
            case 1520:
                nextIntro();
                break;
            case 1650:
                nextIntro();
                break;
            case 1780:
                nextIntro();
                break;
        }
        
        if (timer > 1900) {
            if (int11Y < 44) {
                this.int11Y = int11Y + 1;
            }
        }
        
        if (timer >= 2600) {
            manager.setScene(new room_introimage(manager));
            midlet.stopAllMIDI();
        }
    }

    public void paint(Graphics g) {
        this.width = getWidth();
        this.height = getHeight();
        this.introX = (width / 2) - (introWidth / 2);
        this.introY = (height / 2) - (introHeight / 2);
        
        g.setColor(0x000000);
        g.fillRect(0, 0, getWidth(), getHeight());
        
        if (intro == 10) {
                imageDrawer.drawImage(g, "/images/intro/intro" + (intro + 1) + ".png", introX, int11Y, 0, 0);
        } else {
                imageDrawer.drawImage(g, "/images/intro/intro" + (intro + 1) + ".png", introX, introY, 0, 0);
        }
        
        g.setColor(0x000000);
        g.fillRect(introX, introY - 20, introWidth, 84);
        g.fillRect(introX, introY + 174, introWidth, width);
        g.fillRect(introX, introY, 20, introHeight);
        g.fillRect(introX + 220, introY, 20, introHeight);
        
        if (intro < 7) {
            if (typingLenght < introText[intro].length() * 4) {
                this.typingLenght++;
            }
            textBlitter.setFont("fnt_maintext", "white");
            textBlitter.drawTypingText(g, introText[intro], introX + 48, introY + 185, typingLenght / 2);
        } else {
            textBlitter.setFont("fnt_maintext", "white");
            textBlitter.drawTypingText(g, "", introX + 48, introY + 185, typingLenght / 2);
        }
        
//        textBlitter.setFont("fnt_maintext", "red");
//        textBlitter.drawString(g, "FPS: " + manager.getFPS() + "\n" + "Timer: " + timer, 10, 10);
    }

    protected void keyPressed(int keyCode) {
        midlet.stopAllMIDI();
        manager.setScene(new room_introimage(manager));
    }
    
    private void nextIntro() {
        this.intro++;
        this.typingLenght = 0;
    }
}
