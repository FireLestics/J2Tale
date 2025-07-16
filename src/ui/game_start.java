package ui;

import javax.microedition.lcdui.*;
import java.io.IOException;
import app.MainMIDlet;
import tools.TextBlitter;
import tools.ImageDrawer;

public class game_start extends AbstractCanvas {
    private TextBlitter textBlitter;
    private ImageDrawer imageDrawer;

    private int width;
    private int height;

    private int timer = 0;
    private int frame = 0;
    private int logoWidth = 0;
    private int logoHeight = 0;
    private int logoX = 0;
    private int logoY = 0;
    private int introWidth = 240;
    private int introHeight = 320;
    private int introX = 0;
    private int introY = 0;
    private int helpWidth = 139;
    private int helpHeight = 8;
    private int helpX = 0;
    private int helpY = 0;
    private int int11Y = -167;
    private int intro = 0;
    private int BGColor = 0x000000;
    private int typingLenght = 0;
	
    private boolean onIntro = midlet.getBooleanData("onIntro");
    private boolean onMusic = midlet.getBooleanData("onMusic");
    private boolean needRepaint = true;
    
    private String ViewText = "";
    
    private boolean onDEBUG = false;
    private boolean playedMIDI = false;

    public game_start(MainMIDlet midlet) {
        super(midlet);
        setFullScreenMode(true);
        midlet.loadAllKeyCode();
        imageDrawer = new ImageDrawer();
        textBlitter = new TextBlitter();
        try {
            textBlitter.loadFont("fnt_maintext", "default");  // Загружаем шрифт, указывая дефолтный цвет
        } catch (IOException e) {}
    }
	
    protected void update() {
        switch (timer) {
            case 40:
                if (onIntro == false) {
                        this.timer = 1820;
                }
                midlet.cleanupResources();
                this.frame = 1;
                this.intro = 1;
                this.typingLenght = 0;
                this.ViewText = "Long ago, two races\nruled over Earth:\nHUMANS and MONSTERS.";
                break;
            case 200:
                midlet.cleanupResources();
                this.intro = 2;
                this.typingLenght = 0;
                this.ViewText = "One day, war broke\nout between the two\nraces.";
                break;
            case 330:
                midlet.cleanupResources();
                this.intro = 3;
                this.typingLenght = 0;
                this.ViewText = "After a long battle,\nthe humans were\nvictorious.";
                break;
            case 440:
                midlet.cleanupResources();
                this.intro = 4;
                this.typingLenght = 0;
                this.ViewText = "They sealed the monsters\nunderground with a magic\nspell.";
                break;
            case 570:
                midlet.cleanupResources();
                this.intro = 5;
                this.typingLenght = 0;
                this.ViewText = "Many years later...";
                break;
            case 680:
                midlet.cleanupResources();
                this.intro = 6;
                this.typingLenght = 0;
                this.ViewText = "               MT. EBOTT\n                   201X";
                break;
            case 840:
                midlet.cleanupResources();
                this.intro = 7;
                this.typingLenght = 0;
                this.ViewText = "Legends say that those\nwho climb the mountain\nnever return.";
                break;
            case (1010 + (80 * 0)):
                midlet.cleanupResources();
                this.intro = 8;
                this.ViewText = "";
                break;
            case (1010 + (80 * 1)):
                midlet.cleanupResources();
                this.intro = 9;
                break;
            case (1010 + (80 * 2)):
                midlet.cleanupResources();
                this.intro = 10;
                break;
            case (1010 + (80 * 3)):
                midlet.cleanupResources();
                this.intro = 11;
                break;
        }

        if (timer > (1010 + (80 * 4))) {
            if (int11Y < 64) {
                this.int11Y = int11Y + 1;
                needRepaint = true;
            }
        }

        if (frame == 1) {
            if (playedMIDI == false) {
                midlet.playMIDI("mus_onceuponatime", 0, onMusic);
                this.playedMIDI = true;
            }

            if (typingLenght < ViewText.length() * 4) {
                this.typingLenght++;
            }

            if (timer >= 1820) {
                this.frame = 2;
                needRepaint = true;
                midlet.stopAllMIDI();
            }
        } else if (frame == 2) {
            if (timer >= 3000) {
                this.timer = 38;
                this.intro = 0;
                this.playedMIDI = false;
                this.int11Y = -167;
                needRepaint = true;
            }
        }

        this.timer++;
    }

    protected void paint(Graphics g) {
        this.width = getWidth();
        this.height = getHeight() + 20;
        
        g.setColor(BGColor);
        g.fillRect(0, 0, width, height);
		
        switch (frame) {
			case 0:
				drawLogo(g, width, height);
				break;
			case 1:
				drawIntro(g, width, height);
				break;
			case 2:
				drawHelp(g, width, height);
				break;
		}
    }
	
	private void drawLogo(Graphics g, int width, int height) {
		logoView(g, width, height);
	}

	private void drawIntro(Graphics g, int width, int height) {
		int introX = (width / 2) - (introWidth / 2);
		int introY = (height / 2) - (introHeight / 2);

		// Отрисовка intro
		if (intro == 11) {
			imageDrawer.drawImage(g, "/images/intro" + intro + ".png", introX, int11Y, 0, 0);
		} else {
			imageDrawer.drawImage(g, "/images/intro" + intro + ".png", introX, introY, 0, 0);
		}
		

		// Заливка областей фоновым цветом
		g.setColor(BGColor);
		g.fillRect(introX, introY - 20, introWidth, 84);
		g.fillRect(introX, introY + 174, introWidth, width);
		g.fillRect(introX, introY, 20, introHeight);
		g.fillRect(introX + 220, introY, 20, introHeight);

		// Отрисовка текста
		textBlitter.setFont("fnt_maintext", "white");
		textBlitter.drawTypingText(g, ViewText, introX + 48, introY + 185, typingLenght / 2);
	}

	private void drawHelp(Graphics g, int width, int height) {
		logoView(g, width, height);

		int helpX = (width / 2) - (helpWidth / 2);
		int helpY = (((height / 2) - (helpHeight / 2)) + (logoHeight / 2)) + 20;

		if (timer >= 1845) {
			imageDrawer.drawImage(g, "/images/continue.png", helpX, helpY, 0, 0);
		}
	}
	
    private void logoView(Graphics g, int width, int height) {
        if (midlet.ifSmallScreen(width, height)) {
            this.logoWidth = 118;
            this.logoHeight = 12;
            this.logoX = (width / 2) - (logoWidth / 2);
            this.logoY = (height / 2) - (logoHeight / 2);
            imageDrawer.drawImage(g, "/images/logo_new.png", logoX, logoY, 0, 0);
        } else {
            this.logoWidth = 237;
            this.logoHeight = 24;
            this.logoX = (width / 2) - (logoWidth / 2);
            this.logoY = (height / 2) - (logoHeight / 2);
            imageDrawer.drawImage(g, "/images/logo.png", logoX, logoY, 0, 0);
        }
    }

    protected void keyPressed(int keyCode) {
        if (frame == 1) {
            this.timer = 1820;
        } else if (frame == 2) {
            if (keyCode == midlet.getKeyCode("z")) {
                midlet.playMIDI("mus_menu01", -1, onMusic);
                midlet.switchCanvas(new game_introduction(midlet, 0));
            }
        }
        needRepaint = true;
    }

    public void destroy() {
		midlet.cleanupResources();
		System.out.println("");
    }
}