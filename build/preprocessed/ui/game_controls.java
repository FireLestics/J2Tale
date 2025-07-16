package ui;

import javax.microedition.lcdui.*;
import java.io.IOException;
import java.util.Vector;
import app.MainMIDlet;
import tools.ImageDrawer;
import tools.TextBlitter;

public class game_controls extends AbstractCanvas {
    private ImageDrawer imageDrawer;
    private TextBlitter textBlitter;
    private Vector items;
    
    private int selIndex = 0;
    
    private int prevSel = 0;
    private int prev2Sel = 0;
    
    private boolean onEdit = false;
    private int keysDone = 0;
    private int keysMax;
    
    private int UP = 0;
    private int DOWN = 0;
    private int LEFT = 0;
    private int RIGHT = 0;
    private int Z = 0;
    private int X = 0;
    private int C = 0;
    private int ESC = 0;
    private int F4 = 0;
    private int LB = 0;
    private int RB = 0;
    private int BKSC = 0;
    
    private int templateID = midlet.getIntData("templateKeyCodeID", 0);
    private String templateName = "";
    
    private int baseY;
    
    private int width;
    private int height;

    public game_controls(MainMIDlet midlet, int prevSel, int prev2Sel) {
        super(midlet);
        setFullScreenMode(true);
        
        this.prevSel = prevSel;
        this.prev2Sel = prev2Sel;
        
        imageDrawer = new ImageDrawer();
        textBlitter = new TextBlitter();
        try {
            textBlitter.loadFont("fnt_maintext", "default");
            textBlitter.loadFont("fnt_main", "default");
        } catch (IOException e) {}
        
        items = new Vector();
        items.addElement("BACK");
        items.addElement("EDIT ALL CODES");
        items.addElement("Control Templates:");
        items.addElement("UP Button:");
        items.addElement("DOWN Button:");
        items.addElement("LEFT Button:");
        items.addElement("RIGHT Button:");
        items.addElement("Z Button:");
        items.addElement("X Button:");
        items.addElement("C Button:");
        items.addElement("ESC Button:");
        items.addElement("F4 Button:");
        items.addElement("LB Button:");
        items.addElement("RB Button:");
        items.addElement("BACKSPACE Button:");
        
    }
	
    private void setTemplate(int templateid) {
        if (templateid == 0) {
            midlet.saveKeyCodes(-1, -2, -3, -4, -5, -6, -7, 42, 35, -6, -7, -8);
            midlet.loadAllKeyCode();
        }
        if (templateid == 1) {
            midlet.saveKeyCodes(50, 56, 52, 54, 48, -5, 49, 0, 0, 21, 22, 51);
            midlet.loadAllKeyCode();
        }
        if (templateid == 2) {
            midlet.saveKeyCodes(-1, -2, -3, -4, 48, -5, 49, 0, 0, 21, 22, 51);
            midlet.loadAllKeyCode();
        }
    }
    
    protected void update() {}

    protected void paint(Graphics g) {
        this.width = getWidth();
        this.height = getHeight();
        
	g.setColor(0x000000);
        g.fillRect(0, 0, getWidth(), getHeight() + 20);
        
        int x = width / 20;
        int infX = x + 140;
        this.baseY = 50;
        
        textBlitter.setFont("fnt_main", "white");
        textBlitter.drawString(g, "CONTROLS", (width / 2) - (textBlitter.stringWidth("CONTROLS") / 2), baseY - 40);
        
        if (templateID == -1) {
            this.templateName = "Custom";
        } else if (templateID == 0) {
            this.templateName = "J2ME";
        } else if (templateID == 1) {
            this.templateName = "PSPKVM (D-PAD)";
        } else if (templateID == 2) {
            this.templateName = "PSPKVM (STICK)";
        }
        
        for (int i = 0; i < items.size(); i++) {
            String item = (String) items.elementAt(i);
            if (i == selIndex) {
                textBlitter.setFont("fnt_maintext", "yellow");
            } else {
                textBlitter.setFont("fnt_maintext", "white");
            }
            if (onEdit == false) {
                if ("UP Button:".equals(item)) {
                    textBlitter.setFont("fnt_maintext", "gray");
                    textBlitter.drawString(g, item, x, 50 + (i * 16));
                    textBlitter.drawString(g, "" + midlet.getKeyCode("up"), infX, baseY + (i * 16));
                } else if ("DOWN Button:".equals(item)) {
                    textBlitter.setFont("fnt_maintext", "gray");
                    textBlitter.drawString(g, item, x, 50 + (i * 16));
                    textBlitter.drawString(g, "" + midlet.getKeyCode("down"), infX, baseY + (i * 16));
                } else if ("LEFT Button:".equals(item)) {
                    textBlitter.setFont("fnt_maintext", "gray");
                    textBlitter.drawString(g, item, x, 50 + (i * 16));
                    textBlitter.drawString(g, "" + midlet.getKeyCode("left"), infX, baseY + (i * 16));
                } else if ("RIGHT Button:".equals(item)) {
                    textBlitter.setFont("fnt_maintext", "gray");
                    textBlitter.drawString(g, item, x, 50 + (i * 16));
                    textBlitter.drawString(g, "" + midlet.getKeyCode("right"), infX, baseY + (i * 16));
                } else if ("Z Button:".equals(item)) {
                    textBlitter.setFont("fnt_maintext", "gray");
                    textBlitter.drawString(g, item, x, 50 + (i * 16));
                    textBlitter.drawString(g, "" + midlet.getKeyCode("z"), infX, baseY + (i * 16));
                } else if ("X Button:".equals(item)) {
                    textBlitter.setFont("fnt_maintext", "gray");
                    textBlitter.drawString(g, item, x, 50 + (i * 16));
                    textBlitter.drawString(g, "" + midlet.getKeyCode("x"), infX, baseY + (i * 16));
                } else if ("C Button:".equals(item)) {
                    textBlitter.setFont("fnt_maintext", "gray");
                    textBlitter.drawString(g, item, x, 50 + (i * 16));
                    textBlitter.drawString(g, "" + midlet.getKeyCode("c"), infX, baseY + (i * 16));
                } else if ("ESC Button:".equals(item)) {
                    textBlitter.setFont("fnt_maintext", "gray");
                    textBlitter.drawString(g, item, x, 50 + (i * 16));
                    textBlitter.drawString(g, "" + midlet.getKeyCode("esc"), infX, baseY + (i * 16));
                } else if ("F4 Button:".equals(item)) {
                    textBlitter.setFont("fnt_maintext", "gray");
                    textBlitter.drawString(g, item, x, 50 + (i * 16));
                    textBlitter.drawString(g, "" + midlet.getKeyCode("f4"), infX, baseY + (i * 16));
                } else if ("LB Button:".equals(item)) {
                    textBlitter.setFont("fnt_maintext", "gray");
                    textBlitter.drawString(g, item, x, 50 + (i * 16));
                    textBlitter.drawString(g, "" + midlet.getKeyCode("lb"), infX, baseY + (i * 16));
                } else if ("RB Button:".equals(item)) {
                    textBlitter.setFont("fnt_maintext", "gray");
                    textBlitter.drawString(g, item, x, 50 + (i * 16));
                    textBlitter.drawString(g, "" + midlet.getKeyCode("rb"), infX, baseY + (i * 16));
                } else if ("BACKSPACE Button:".equals(item)) {
                    textBlitter.setFont("fnt_maintext", "gray");
                    textBlitter.drawString(g, item, x, 50 + (i * 16));
                    textBlitter.drawString(g, "" + midlet.getKeyCode("bksc"), infX, baseY + (i * 16));
                } else if ("Control Templates:".equals(item)) {
                    textBlitter.drawString(g, item, x, 50 + (i * 16));
                    textBlitter.drawString(g, "" + templateName, infX, baseY + (i * 16));
                } else {
                    textBlitter.drawString(g, item, x, baseY + (i * 16));
                }
            } else {
                if ("UP Button:".equals(item)) {
                    textBlitter.drawString(g, item, x, 50 + (i * 16));
                    textBlitter.drawString(g, "" + UP, infX, baseY + (i * 16));
                } else if ("DOWN Button:".equals(item)) {
                    textBlitter.drawString(g, item, x, 50 + (i * 16));
                    textBlitter.drawString(g, "" + DOWN, infX, baseY + (i * 16));
                } else if ("LEFT Button:".equals(item)) {
                    textBlitter.drawString(g, item, x, 50 + (i * 16));
                    textBlitter.drawString(g, "" + LEFT, infX, baseY + (i * 16));
                } else if ("RIGHT Button:".equals(item)) {
                    textBlitter.drawString(g, item, x, 50 + (i * 16));
                    textBlitter.drawString(g, "" + RIGHT, infX, baseY + (i * 16));
                } else if ("Z Button:".equals(item)) {
                    textBlitter.drawString(g, item, x, 50 + (i * 16));
                    textBlitter.drawString(g, "" + Z, infX, baseY + (i * 16));
                } else if ("X Button:".equals(item)) {
                    textBlitter.drawString(g, item, x, 50 + (i * 16));
                    textBlitter.drawString(g, "" + X, infX, baseY + (i * 16));
                } else if ("C Button:".equals(item)) {
                    textBlitter.drawString(g, item, x, 50 + (i * 16));
                    textBlitter.drawString(g, "" + C, infX, baseY + (i * 16));
                } else if ("ESC Button:".equals(item)) {
                    textBlitter.drawString(g, item, x, 50 + (i * 16));
                    textBlitter.drawString(g, "" + ESC, infX, baseY + (i * 16));
                } else if ("F4 Button:".equals(item)) {
                    textBlitter.drawString(g, item, x, 50 + (i * 16));
                    textBlitter.drawString(g, "" + F4, infX, baseY + (i * 16));
                } else if ("LB Button:".equals(item)) {
                    textBlitter.drawString(g, item, x, 50 + (i * 16));
                    textBlitter.drawString(g, "" + LB, infX, baseY + (i * 16));
                } else if ("RB Button:".equals(item)) {
                    textBlitter.drawString(g, item, x, 50 + (i * 16));
                    textBlitter.drawString(g, "" + RB, infX, baseY + (i * 16));
                } else if ("BACKSPACE Button:".equals(item)) {
                    textBlitter.drawString(g, item, x, 50 + (i * 16));
                    textBlitter.drawString(g, "" + BKSC, infX, baseY + (i * 16));
                } else {
                    textBlitter.setFont("fnt_maintext", "gray");
                    textBlitter.drawString(g, item, x, baseY + (i * 16));
                }
            }
        }
		
	// textBlitter.setFont("fnt_maintext", "white");
	// textBlitter.drawTypingText(g, "", 0, 0, 999);
	// textBlitter.drawString(g, "", 0, 0);
        // imageDrawer.drawImage(g, "", 0, 0, 0, 0);
    }

    protected void keyPressed(int keyCode) {
        if (onEdit == false) {
            if (keyCode == midlet.getKeyCode("up")) {
                if (selIndex > 0) {
                    this.selIndex = selIndex - 1;
                }
            }
            if (keyCode == midlet.getKeyCode("down")) {
                if (selIndex < 2) {
                    this.selIndex = selIndex + 1;
                }
            }
            if (keyCode == midlet.getKeyCode("z")) {
                String selectedItem = (String) items.elementAt(selIndex);
                if (selectedItem.equals("BACK")) {
                    midlet.switchCanvas(new game_settings(midlet, prevSel, prev2Sel));
                }
                if (selectedItem.equals("EDIT ALL CODES")) {
                    this.templateID = -1;
                    this.keysDone = 0;
                    this.onEdit = true;
                }
                if (selectedItem.equals("Control Templates:")) {
                    this.templateID++;
                    if (templateID > 2) {
                        this.templateID = 0;
                    }
                    setTemplate(templateID);
                }
            }
        } else {
            if (keysDone < 12) {
                if (keysDone == 0 && keyCode != 0) {
                    this.UP = keyCode;
                    this.keysDone++;
                } else if (keysDone == 1 && keyCode != 0) {
                    this.DOWN = keyCode;
                    this.keysDone++;
                } else if (keysDone == 2 && keyCode != 0) {
                    this.LEFT = keyCode;
                    this.keysDone++;
                } else if (keysDone == 3 && keyCode != 0) {
                    this.RIGHT = keyCode;
                    this.keysDone++;
                } else if (keysDone == 4 && keyCode != 0) {
                    this.Z = keyCode;
                    this.keysDone++;
                } else if (keysDone == 5 && keyCode != 0) {
                    this.X = keyCode;
                    this.keysDone++;
                } else if (keysDone == 6 && keyCode != 0) {
                    this.C = keyCode;
                    this.keysDone++;
                } else if (keysDone == 7 && keyCode != 0) {
                    this.ESC = keyCode;
                    this.keysDone++;
                } else if (keysDone == 8 && keyCode != 0) {
                    this.F4 = keyCode;
                    this.keysDone++;
                } else if (keysDone == 9 && keyCode != 0) {
                    this.LB = keyCode;
                    this.keysDone++;
                } else if (keysDone == 10 && keyCode != 0) {
                    this.RB = keyCode;
                    this.keysDone++;
                } else if (keysDone == 11 && keyCode != 0) {
                    this.BKSC = keyCode;
                    midlet.saveKeyCodes(UP, DOWN, LEFT, RIGHT, Z, X, C, ESC, F4, LB, RB, BKSC);
                    midlet.loadAllKeyCode();
                    midlet.saveIntData("templateKeyCodeID", templateID);
                    this.onEdit = false;
                }
            }
        }
    }

    public void destroy() {
        midlet.cleanupResources();
        System.out.println("");
    }
}