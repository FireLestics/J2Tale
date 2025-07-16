package ui;

import javax.microedition.lcdui.*;
import java.io.IOException;
import app.MainMIDlet;
import java.util.Vector;
import tools.TextBlitter;

public class game_introduction extends AbstractCanvas {
    private TextBlitter textBlitter;
    private Vector items;
    
    private int selIndex = 0;
    private int PressedKey = 0;
    
    private int width;
    private int height;
    
    private int fixedPosX;
    private int fixedPosY;
    
    private String instruction = "";
    private String info1;
    private String info2;

    public game_introduction(MainMIDlet midlet, int sel) {
        super(midlet);
        textBlitter = new TextBlitter();
        try {
            textBlitter.loadFont("fnt_maintext", "default");
            textBlitter.loadFont("fnt_small", "default");
        } catch (IOException e) {}
        
        items = new Vector();
        items.addElement("Begin Game");
        items.addElement("Settings");
        
        this.instruction = " --- Instruction ---\n\n";
        this.instruction = instruction + "[* or ENTER] - Confirm\n";
        this.instruction = instruction + "[0 or RB] - Cancel\n";
        this.instruction = instruction + "[# or LB] - Menu (In-game)\n";
        this.instruction = instruction + "[D-PAD] - Movement\n";
        this.instruction = instruction + "When HP is 0, you lose.\n\n";
        
        this.info1 = "UNDERTALE v1.08 (C) Toby Fox 2015-2017";
        this.info2 = "J2TALE v0.6 (C) IgniteSizzle 2025-202x";
        
        this.selIndex = sel;
    }
	
    protected void update() {}

    protected void paint(Graphics g) {
        this.width = getWidth();
        this.height = getHeight();
        this.fixedPosX = (width / 2) - 78;
        this.fixedPosY = (height / 2) - 92;
        
        g.setColor(0x000000);
        g.fillRect(0, 0, getWidth(), getHeight() + 20);
		
        textBlitter.setFont("fnt_maintext", "gray");
        textBlitter.drawString(g, instruction, fixedPosX, fixedPosY);
        
        for (int i = 0; i < items.size(); i++) {
            String item = (String) items.elementAt(i);
            if (i == selIndex) {
                textBlitter.setFont("fnt_maintext", "yellow");
            } else {
                textBlitter.setFont("fnt_maintext", "white");
            }
            textBlitter.drawString(g, item, fixedPosX, fixedPosY + 128 + (i * 16));
        }
        
        textBlitter.setFont("fnt_small", "gray");
        textBlitter.drawString(g, info1, (width / 2) - (textBlitter.stringWidth(info1) / 2), height - 20);
        textBlitter.drawString(g, info2, (width / 2) - (textBlitter.stringWidth(info2) / 2), height - 12);
        
        // textBlitter.setFont("fnt_maintext", "red");
        // textBlitter.drawString(g, "KeyPressed: " + PressedKey, 10, 10);
    }

    protected void keyPressed(int keyCode) {
        this.PressedKey = keyCode;
        if (keyCode == midlet.getKeyCode("up")) {
            if (selIndex > 0) {
                this.selIndex = selIndex - 1;
            }
        }
        if (keyCode == midlet.getKeyCode("down")) {
            if (selIndex < items.size() - 1) {
                this.selIndex = selIndex + 1;
            }
        }
        if (keyCode == midlet.getKeyCode("z")) {
            String selectedItem = (String) items.elementAt(selIndex);
            if (selectedItem.equals("Begin Game")) {
                midlet.switchCanvas(new game_selectName(midlet, selIndex, ""));
            }
            if (selectedItem.equals("Settings")) {
                midlet.stopAllMIDI();
                midlet.switchCanvas(new game_settings(midlet, selIndex, 0));
            }
        }
    }

    public void destroy() {
        midlet.cleanupResources();
        System.out.println("");
    }
}