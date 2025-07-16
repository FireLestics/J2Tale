package ui;

import javax.microedition.lcdui.*;
import java.io.IOException;
import app.MainMIDlet;
import java.util.Vector;
import tools.TextBlitter;

public class game_settings extends AbstractCanvas {
    private TextBlitter textBlitter;
    private Vector items;
    
    private int settSelIndex = 0;
    
    private int width;
    private int height;
    
    private boolean onIntro = midlet.getBooleanData("onIntro");
    private boolean onMusic = midlet.getBooleanData("onMusic");
    
    private int prevSel = 0;

    public game_settings(MainMIDlet midlet, int prevSel, int prev2Sel) {
        super(midlet);
        textBlitter = new TextBlitter();
        try {
            textBlitter.loadFont("fnt_maintext", "default");
            textBlitter.loadFont("fnt_main", "default");
        } catch (IOException e) {}
        
        this.prevSel = prevSel;
        this.settSelIndex = prev2Sel;
        
        items = new Vector();
        items.addElement("EXIT");
        items.addElement("CONTROL OPTIONS");
        items.addElement("CONTROL TEST <color:gray>(no work)");
        items.addElement("MUSIC:");
        items.addElement("INTRO:");
    }
	
    protected void update() {}

    protected void paint(Graphics g) {
        this.width = getWidth();
        this.height = getHeight();
        
	g.setColor(0x000000);
        g.fillRect(0, 0, getWidth(), getHeight() + 20);
		
	textBlitter.setFont("fnt_main", "white");
        textBlitter.drawString(g, "SETTINGS", (width / 2) - (textBlitter.stringWidth("SETTINGS") / 2), 10);
        
        int x = width / 14;
        int infX = x + 100;
        
        for (int i = 0; i < items.size(); i++) {
            String item = (String) items.elementAt(i);
            if (i == settSelIndex) {
                textBlitter.setFont("fnt_maintext", "yellow");
            } else {
                textBlitter.setFont("fnt_maintext", "white");
            }
            if (item == "MUSIC:") {
                textBlitter.drawString(g, item, x, 50 + (i * 16));
                textBlitter.drawString(g, "" + onMusic, infX, 50 + (i * 16));
            } else if (item == "INTRO:") {
                textBlitter.drawString(g, item, x, 50 + (i * 16));
                textBlitter.drawString(g, "" + onIntro, infX, 50 + (i * 16));
            } else {
                textBlitter.drawString(g, item, x, 50 + (i * 16));
            }
        }
    }

    protected void keyPressed(int keyCode) {
        if (keyCode == midlet.getKeyCode("up")) {
            if (settSelIndex > 0) {
                this.settSelIndex = settSelIndex - 1;
            }
        }
        if (keyCode == midlet.getKeyCode("down")) {
            if (settSelIndex < items.size() - 1) {
                this.settSelIndex = settSelIndex + 1;
            }
        }
        if (keyCode == midlet.getKeyCode("z")) {
            String selectedItem = (String) items.elementAt(settSelIndex);
            if (selectedItem.equals("EXIT")) {
                midlet.saveBooleanData("onMusic", onMusic);
                midlet.saveBooleanData("onIntro", onIntro);
                midlet.playMIDI("mus_menu01", -1, midlet.getBooleanData("onMusic"));
                midlet.switchCanvas(new game_introduction(midlet, prevSel));
            }
            if (selectedItem.equals("CONTROL OPTIONS")) {
                midlet.switchCanvas(new game_controls(midlet, prevSel, settSelIndex));
            }
            if (selectedItem.equals("MUSIC:")) {
                    this.onMusic = !onMusic;
            }
            if (selectedItem.equals("INTRO:")) {
                    this.onIntro = !onIntro;
            }
        }
    }

    public void destroy() {
        midlet.cleanupResources();
      System.out.println("");
    }
}