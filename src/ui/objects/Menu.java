package ui.objects;

import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Canvas;
import java.io.IOException;
import tools.TextBlitter;

public class Menu {
    private TextBlitter textBlitter;
    private Canvas canvas;
    
    private int menuYScale = 38;
    
    public Menu() {
        textBlitter = new TextBlitter();
        try {
            textBlitter.loadFont("fnt_maintext", "default");
            textBlitter.loadFont("fnt_small", "default");
        } catch (IOException e) {}
    }
    
    public void drawMenu_1(Graphics g, int x, int y, String playerName, int LV, int HP, int HPMax, int G) {
        g.setColor(0x000000);
        g.fillRect(x, y, 70, 48);
        g.setColor(0xFFFFFF);
        drawThickRect(g, x, y, 70, 48, 3);

        textBlitter.setFont("fnt_maintext", "white");
        textBlitter.drawString(g, playerName, x + 8, y + 5);
        textBlitter.setFont("fnt_small", "white");
        textBlitter.drawString(g, "LV\nHP\nG", x + 8, y + 20);
        textBlitter.drawString(g, "" + LV + "\n" + HP + "/" + HPMax + "\n" + G, x + 24, y + 20);
    }
    
    public void drawMenu_2(Graphics g, int x, int y, int select, String[] menu) {
        g.setColor(0x000000);
        g.fillRect(x, y, 70, menuYScale);
        g.setColor(0xFFFFFF);
        drawThickRect(g, x, y, 70, menuYScale, 3);
        
        for (int i = 0; i < menu.length; i++) {
            this.menuYScale = 38 + (16 * i);
            String item = menu[i];
            textBlitter.setFont("fnt_maintext", "white");
            textBlitter.drawString(g, item, x + 28, y + 11 + (16 * i));
        }
    }
    
    public void drawThickRect(Graphics g, int x, int y, int w, int h, int thickness) {
        for (int i = 0; i < thickness; i++) {
            g.drawRect(x + i, y + i, w - 2 * i, h - 2 * i);
        }
    }
}
