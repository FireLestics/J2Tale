package j2tale_base.scenes;

import javax.microedition.lcdui.*;
import java.io.IOException;
import j2tale_base.Midlet;
import java.util.Vector;
import j2tale_base.tools.TextBlitter;

public class room_intromenu extends Scene {
    private Midlet midlet;
    private TextBlitter textBlitter;
    private Vector menu_0, menu_1, menu_2, menu_3;
    
    private int menu_0_sel, menu_1_sel, menu_2_sel, menu_3_sel;

    private int width;
    private int height;
    private int fixedPosX;
    private int fixedPosY;
    private int view;
    private String instruction;
    private String[] info;
    private String playerName = "";
    
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
    
    private int templateID;
    private String templateName = "";
    
    private boolean onIntro;
    private boolean onMusic;
    private String TopBanner;
    private String LBText;
    private String RBText;
    
    public room_intromenu(SceneManager manager) {
        super(manager);
        this.midlet = manager.getMidlet();
        textBlitter = new TextBlitter();
        try {
            textBlitter.loadFont("fnt_maintext", "default");
            textBlitter.loadFont("fnt_main", "default");
            textBlitter.loadFont("fnt_small", "default");
        } catch (IOException e) {}
        
        menu_0 = new Vector();
        menu_0.addElement("Begin Game");
        menu_0.addElement("Settings");
        
        menu_1 = new Vector();
        menu_1.addElement("EXIT");
        menu_1.addElement("CONTROL OPTIONS");
        menu_1.addElement("CONTROL TEST <color:gray>(no work)");
        menu_1.addElement("MUSIC:");
        menu_1.addElement("INTRO:");
        
        menu_2 = new Vector();
        menu_2.addElement("BACK");
        menu_2.addElement("EDIT ALL CODES");
        menu_2.addElement("Control Templates:");
        menu_2.addElement("UP Button:");
        menu_2.addElement("DOWN Button:");
        menu_2.addElement("LEFT Button:");
        menu_2.addElement("RIGHT Button:");
        menu_2.addElement("Z Button:");
        menu_2.addElement("X Button:");
        menu_2.addElement("C Button:");
        menu_2.addElement("ESC Button:");
        menu_2.addElement("F4 Button:");
        menu_2.addElement("LB Button:");
        menu_2.addElement("RB Button:");
        menu_2.addElement("BACKSPACE Button:");

        menu_3 = new Vector();
        for (char c = 'A'; c <= 'Z'; c++) {
            menu_3.addElement(String.valueOf(c));
        }
        
        this.instruction = " --- Instruction ---\n\n";
        this.instruction = instruction + "[* or ENTER] - Confirm\n";
        this.instruction = instruction + "[0 or RB] - Cancel\n";
        this.instruction = instruction + "[# or LB] - Menu (In-game)\n";
        this.instruction = instruction + "[D-PAD] - Movement\n";
        this.instruction = instruction + "When HP is 0, you lose.\n\n";
        
        this.info = new String[] {
            "UNDERTALE v1.08 (C) Toby Fox 2015-2017",
            "J2TALE v0.6 (C) IgniteSizzle 2025-202x"
        };
    
        this.onIntro = midlet.getBooleanData("onIntro");
        this.onMusic = midlet.getBooleanData("onMusic");
        this.templateID = midlet.getIntData("templateKeyCodeID", 0);
        
        this.view = 0;
        this.menu_0_sel = 0;
        this.menu_1_sel = 0;
        
        midlet.playMIDI("/midi/mus_menu01.mid", -1, onMusic);
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
    
    public void update() {
    }

    public void paint(Graphics g) {
        this.width = getWidth();
        this.height = getHeight();
        this.fixedPosX = (width / 2) - 78;
        this.fixedPosY = (height / 2) - 92;
        
        g.setColor(0x000000);
        g.fillRect(0, 0, getWidth(), getHeight());
        
        switch (view) {
            case 0:
                introduction(g);
                break;
            case 1:
                settings(g);
                break;
            case 2:
                ControlSett(g);
                break;
            case 3:
                selName(g);
                break;
            case 4:
                comfName(g);
                break;
        }
    }
    
    private void introduction(Graphics g) {
        textBlitter.setFont("fnt_maintext", "gray");
        textBlitter.drawString(g, instruction, fixedPosX, fixedPosY);
        
        for (int i = 0; i < menu_0.size(); i++) {
            String item = (String) menu_0.elementAt(i);
            if (i == menu_0_sel) {
                textBlitter.setFont("fnt_maintext", "yellow");
            } else {
                textBlitter.setFont("fnt_maintext", "white");
            }
            textBlitter.drawString(g, item, fixedPosX, fixedPosY + 128 + (i * 16));
        }
        
        textBlitter.setFont("fnt_small", "gray");
        textBlitter.drawString(g, info[0], (width / 2) - (textBlitter.stringWidth(info[0]) / 2), height - 20);
        textBlitter.drawString(g, info[1], (width / 2) - (textBlitter.stringWidth(info[1]) / 2), height - 12);
    }
    
    private void settings(Graphics g) {
        textBlitter.setFont("fnt_main", "white");
        textBlitter.drawString(g, "SETTINGS", (width / 2) - (textBlitter.stringWidth("SETTINGS") / 2), 10);
        
        int x = width / 14;
        int infX = x + 100;
        
        for (int i = 0; i < menu_1.size(); i++) {
            String item = (String) menu_1.elementAt(i);
            if (i == menu_1_sel) {
                textBlitter.setFont("fnt_maintext", "yellow");
            } else {
                textBlitter.setFont("fnt_maintext", "white");
            }
            if ("MUSIC:".equals(item)) {
                textBlitter.drawString(g, item, x, 50 + (i * 16));
                textBlitter.drawString(g, "" + onMusic, infX, 50 + (i * 16));
            } else if ("INTRO:".equals(item)) {
                textBlitter.drawString(g, item, x, 50 + (i * 16));
                textBlitter.drawString(g, "" + onIntro, infX, 50 + (i * 16));
            } else {
                textBlitter.drawString(g, item, x, 50 + (i * 16));
            }
        }
    }
    
    private void ControlSett(Graphics g) {
        int x = width / 20;
        int infX = x + 140;
        
        textBlitter.setFont("fnt_main", "white");
        textBlitter.drawString(g, "CONTROLS", (width / 2) - (textBlitter.stringWidth("CONTROLS") / 2), 10);
        
        if (templateID == -1) {
            this.templateName = "Custom";
        } else if (templateID == 0) {
            this.templateName = "J2ME";
        } else if (templateID == 1) {
            this.templateName = "PSPKVM (D-PAD)";
        } else if (templateID == 2) {
            this.templateName = "PSPKVM (STICK)";
        }
        
        for (int i = 0; i < menu_2.size(); i++) {
            String item = (String) menu_2.elementAt(i);
            if (i == menu_2_sel) {
                textBlitter.setFont("fnt_maintext", "yellow");
            } else {
                textBlitter.setFont("fnt_maintext", "white");
            }
            if (onEdit == false) {
                if ("UP Button:".equals(item)) {
                    textBlitter.setFont("fnt_maintext", "gray");
                    textBlitter.drawString(g, item, x, 50 + (i * 16));
                    textBlitter.drawString(g, "" + midlet.getKeyCode("up"), infX, 50 + (i * 16));
                } else if ("DOWN Button:".equals(item)) {
                    textBlitter.setFont("fnt_maintext", "gray");
                    textBlitter.drawString(g, item, x, 50 + (i * 16));
                    textBlitter.drawString(g, "" + midlet.getKeyCode("down"), infX, 50 + (i * 16));
                } else if ("LEFT Button:".equals(item)) {
                    textBlitter.setFont("fnt_maintext", "gray");
                    textBlitter.drawString(g, item, x, 50 + (i * 16));
                    textBlitter.drawString(g, "" + midlet.getKeyCode("left"), infX, 50 + (i * 16));
                } else if ("RIGHT Button:".equals(item)) {
                    textBlitter.setFont("fnt_maintext", "gray");
                    textBlitter.drawString(g, item, x, 50 + (i * 16));
                    textBlitter.drawString(g, "" + midlet.getKeyCode("right"), infX, 50 + (i * 16));
                } else if ("Z Button:".equals(item)) {
                    textBlitter.setFont("fnt_maintext", "gray");
                    textBlitter.drawString(g, item, x, 50 + (i * 16));
                    textBlitter.drawString(g, "" + midlet.getKeyCode("z"), infX, 50 + (i * 16));
                } else if ("X Button:".equals(item)) {
                    textBlitter.setFont("fnt_maintext", "gray");
                    textBlitter.drawString(g, item, x, 50 + (i * 16));
                    textBlitter.drawString(g, "" + midlet.getKeyCode("x"), infX, 50 + (i * 16));
                } else if ("C Button:".equals(item)) {
                    textBlitter.setFont("fnt_maintext", "gray");
                    textBlitter.drawString(g, item, x, 50 + (i * 16));
                    textBlitter.drawString(g, "" + midlet.getKeyCode("c"), infX, 50 + (i * 16));
                } else if ("ESC Button:".equals(item)) {
                    textBlitter.setFont("fnt_maintext", "gray");
                    textBlitter.drawString(g, item, x, 50 + (i * 16));
                    textBlitter.drawString(g, "" + midlet.getKeyCode("esc"), infX, 50 + (i * 16));
                } else if ("F4 Button:".equals(item)) {
                    textBlitter.setFont("fnt_maintext", "gray");
                    textBlitter.drawString(g, item, x, 50 + (i * 16));
                    textBlitter.drawString(g, "" + midlet.getKeyCode("f4"), infX, 50 + (i * 16));
                } else if ("LB Button:".equals(item)) {
                    textBlitter.setFont("fnt_maintext", "gray");
                    textBlitter.drawString(g, item, x, 50 + (i * 16));
                    textBlitter.drawString(g, "" + midlet.getKeyCode("lb"), infX, 50 + (i * 16));
                } else if ("RB Button:".equals(item)) {
                    textBlitter.setFont("fnt_maintext", "gray");
                    textBlitter.drawString(g, item, x, 50 + (i * 16));
                    textBlitter.drawString(g, "" + midlet.getKeyCode("rb"), infX, 50 + (i * 16));
                } else if ("BACKSPACE Button:".equals(item)) {
                    textBlitter.setFont("fnt_maintext", "gray");
                    textBlitter.drawString(g, item, x, 50 + (i * 16));
                    textBlitter.drawString(g, "" + midlet.getKeyCode("bksc"), infX, 50 + (i * 16));
                } else if ("Control Templates:".equals(item)) {
                    textBlitter.drawString(g, item, x, 50 + (i * 16));
                    textBlitter.drawString(g, "" + templateName, infX, 50 + (i * 16));
                } else {
                    textBlitter.drawString(g, item, x, 50 + (i * 16));
                }
            } else {
                if ("UP Button:".equals(item)) {
                    textBlitter.drawString(g, item, x, 50 + (i * 16));
                    textBlitter.drawString(g, "" + UP, infX, 50 + (i * 16));
                } else if ("DOWN Button:".equals(item)) {
                    textBlitter.drawString(g, item, x, 50 + (i * 16));
                    textBlitter.drawString(g, "" + DOWN, infX, 50 + (i * 16));
                } else if ("LEFT Button:".equals(item)) {
                    textBlitter.drawString(g, item, x, 50 + (i * 16));
                    textBlitter.drawString(g, "" + LEFT, infX, 50 + (i * 16));
                } else if ("RIGHT Button:".equals(item)) {
                    textBlitter.drawString(g, item, x, 50 + (i * 16));
                    textBlitter.drawString(g, "" + RIGHT, infX, 50 + (i * 16));
                } else if ("Z Button:".equals(item)) {
                    textBlitter.drawString(g, item, x, 50 + (i * 16));
                    textBlitter.drawString(g, "" + Z, infX, 50 + (i * 16));
                } else if ("X Button:".equals(item)) {
                    textBlitter.drawString(g, item, x, 50 + (i * 16));
                    textBlitter.drawString(g, "" + X, infX, 50 + (i * 16));
                } else if ("C Button:".equals(item)) {
                    textBlitter.drawString(g, item, x, 50 + (i * 16));
                    textBlitter.drawString(g, "" + C, infX, 50 + (i * 16));
                } else if ("ESC Button:".equals(item)) {
                    textBlitter.drawString(g, item, x, 50 + (i * 16));
                    textBlitter.drawString(g, "" + ESC, infX, 50 + (i * 16));
                } else if ("F4 Button:".equals(item)) {
                    textBlitter.drawString(g, item, x, 50 + (i * 16));
                    textBlitter.drawString(g, "" + F4, infX, 50 + (i * 16));
                } else if ("LB Button:".equals(item)) {
                    textBlitter.drawString(g, item, x, 50 + (i * 16));
                    textBlitter.drawString(g, "" + LB, infX, 50 + (i * 16));
                } else if ("RB Button:".equals(item)) {
                    textBlitter.drawString(g, item, x, 50 + (i * 16));
                    textBlitter.drawString(g, "" + RB, infX, 50 + (i * 16));
                } else if ("BACKSPACE Button:".equals(item)) {
                    textBlitter.drawString(g, item, x, 50 + (i * 16));
                    textBlitter.drawString(g, "" + BKSC, infX, 50 + (i * 16));
                } else {
                    textBlitter.setFont("fnt_maintext", "gray");
                    textBlitter.drawString(g, item, x, 50 + (i * 16));
                }
            }
        }
    }
    
    private void selName(Graphics g) {
        String helpText = "[BKSC] - Backspace";
        this.LBText = "Quit";
        this.RBText = "Done";
        this.TopBanner = "Name the fallen human.";
        String ENTERText = "ENTER";
        
        if ("GASTER".equals(playerName)) {
            manager.setScene(new room_introstory(manager));
            return;
        }

        // Text measurements
        int helpTextWidth = textBlitter.stringWidth(helpText);
        int helpENTERWidth = textBlitter.stringWidth(ENTERText);
        int helpRBWidth = textBlitter.stringWidth(RBText);
        int TopBannerWidth = textBlitter.stringWidth(TopBanner);

        // Draw static text
        drawText(g, "fnt_maintext", "white", TopBanner, (width - TopBannerWidth) / 2, 8);
        drawText(g, "fnt_maintext", "white", playerName, (width / 2) - 18, 28);
        drawText(g, "fnt_maintext", "gray", helpText, (width - helpTextWidth) / 2, height - 45);
        drawText(g, "fnt_maintext", "white", LBText, 10, height - 25);
        drawText(g, "fnt_maintext", "white", ENTERText, (width - helpENTERWidth) / 2, height - 25);
        drawText(g, "fnt_maintext", "white", RBText, width - 10 - helpRBWidth, height - 25);

        // Draw name selection grid
        int fixedPosX = (width / 2) - 78;
        int fixedPosY = (height / 2) - 92;
        int startPosX = fixedPosX;
        int startPosY = fixedPosY + 50;

        for (int i = 0; i < menu_3.size(); i++) {
            String item = (String) menu_3.elementAt(i);
            int row = i / 7;
            int col = i % 7;
            int x = startPosX + (col * 24);
            int y = startPosY + (row * 16);

            String color = (i == menu_3_sel) ? "yellow" : "white";
            textBlitter.setFont("fnt_maintext", color);
            textBlitter.drawString(g, "<shake:1,1>" + item + "<shake:off>", x, y);
        }
    }
    
    private void comfName(Graphics g) {
        textBlitter.setFont("fnt_maintext", "white");

        int helpRBWidth = textBlitter.stringWidth(RBText);
        int TopBannerWidth = textBlitter.stringWidth("Is this name correct?");

        this.TopBanner = "Is this name correct?";
        if (playerName.equals("AAAAAA")) {
            this.LBText = "No";
            this.RBText = "Yes";
            this.TopBanner = "Not very creative...?";
        } else if (playerName.equals("FLOWEY")) {
            this.TopBanner = "I already CHOSE\nthat name.";
            this.LBText = "Go back";
            this.RBText = "";
        } else if (playerName.equals("SANS")) {
            this.LBText = "Go back";
            this.RBText = "";
            this.TopBanner = "nope.";
        } else if (playerName.equals("TORIEL")) {
            this.LBText = "Go back";
            this.RBText = "";
            this.TopBanner = "I think you should\nthink of your own\nname, my child.";
        } else if (playerName.equals("PAPYRU")) {
            this.LBText = "No";
            this.RBText = "Yes";
            this.TopBanner = "I'LL ALLOW IT!!!!";
        } else if (playerName.equals("ASGORE")) {
            this.LBText = "Go back";
            this.RBText = "";
            this.TopBanner = "You cannot.";
        } else if (playerName.equals("ASRIEL")) {
            this.LBText = "Go back";
            this.RBText = "";
            this.TopBanner = "...";
        } else if (playerName.equals("UNDYNE")) {
            this.LBText = "Go back";
            this.RBText = "";
            this.TopBanner = "Get your OWN name!";
        } else if (playerName.equals("ALPHYS")) {
            this.LBText = "Go back";
            this.RBText = "";
            this.TopBanner = "D-don't do that.";
        } else if (playerName.equals("ALPHY")) {
            this.LBText = "No";
            this.RBText = "Yes";
            this.TopBanner = "Uh... OK?";
        } else if (playerName.equals("CATTY")) {
            this.LBText = "No";
            this.RBText = "Yes";
            this.TopBanner = "Bratty! Bratty!\nThat's MY name!";
        } else if (playerName.equals("BRATTY")) {
            this.LBText = "No";
            this.RBText = "Yes";
            this.TopBanner = "Like, OK I guess.";
        } else if (playerName.equals("METTA")) {
            this.LBText = "Go back";
            this.RBText = "";
            this.TopBanner = "Name it's <color:red>NOT <color:white>done...";
        } else if (playerName.equals("MURDER")) {
            this.LBText = "Go back";
            this.RBText = "";
            this.TopBanner = "Name it's <color:red>NOT <color:white>done...";
        } else if (playerName.equals("MERCY")) {
            this.LBText = "Go back";
            this.RBText = "";
            this.TopBanner = "Name it's <color:red>NOT <color:white>done...";
        } else if (playerName.equals("GERSON")) {
            this.LBText = "Go back";
            this.RBText = "";
            this.TopBanner = "Name it's <color:red>NOT <color:white>done...";
        } else if (playerName.equals("BPANTS")) {
            this.LBText = "Go back";
            this.RBText = "";
            this.TopBanner = "Name it's <color:red>NOT <color:white>done...";
        } else if (playerName.equals("TEMMIE")) {
            this.LBText = "Go back";
            this.RBText = "";
            this.TopBanner = "Name it's <color:red>NOT <color:white>done...";
        } else if (playerName.equals("WOSHA")) {
            this.LBText = "Go back";
            this.RBText = "";
            this.TopBanner = "Name it's <color:red>NOT <color:white>done...";
        } else if (playerName.equals("JERRY")) {
            this.LBText = "Go back";
            this.RBText = "";
            this.TopBanner = "Name it's <color:red>NOT <color:white>done...";
        } else if (playerName.equals("BLOOKY")) {
            this.LBText = "Go back";
            this.RBText = "";
            this.TopBanner = "Name it's <color:red>NOT <color:white>done...";
        } else if (playerName.equals("SHYREN")) {
            this.LBText = "Go back";
            this.RBText = "";
            this.TopBanner = "Name it's <color:red>NOT <color:white>done...";
        } else if (playerName.equals("AARON")) {
            this.LBText = "Go back";
            this.RBText = "";
            this.TopBanner = "Name it's <color:red>NOT <color:white>done...";
        } else if (playerName.equals("CHARA")) {
            this.LBText = "Go back";
            this.RBText = "";
            this.TopBanner = "Name it's <color:red>NOT <color:white>done...";
        } else if (playerName.equals("FRISK")) {
            this.LBText = "Go back";
            this.RBText = "";
            this.TopBanner = "Name it's <color:red>NOT <color:white>done...";
        } else if (playerName.equals("IGNITE")) {
            this.LBText = "Go back";
            this.RBText = "";
            this.TopBanner = "Wow, you <color:yellow>FINALLY<color:white>\nchose my name...\nAnd unfortunately I'll\nhave to <color:red>refuse<color:white>!";
        } else {
            this.LBText = "No";
            this.RBText = "Yes";
        }

        textBlitter.drawString(g, "<color:white>" + TopBanner, (width / 2) - (TopBannerWidth / 2), 28);
        textBlitter.drawString(g, "<color:white>" + LBText, 10,height - 25);
        textBlitter.drawString(g, "<color:white>" + RBText, width - 10 - helpRBWidth, height - 25);

        int nameWidth = textBlitter.stringWidth(playerName);
        textBlitter.setFont("fnt_main", "white");
        textBlitter.drawString(g, "<shake:1,1>" + playerName + "<shake:off>", (width / 2) - nameWidth, (height / 2));
    }

    private void drawText(Graphics g, String font, String color, String text, int x, int y) {
        textBlitter.setFont(font, color);
        textBlitter.drawString(g, text, x, y);
    }

    protected void keyPressed(int keyCode) {
        // manager.setScene(new GameScene(manager));
        switch (view) {
            case 0:
                if (keyCode == midlet.getKeyCode("up")) {
                    if (menu_0_sel > 0) {
                        this.menu_0_sel = menu_0_sel - 1;
                    }
                }
                if (keyCode == midlet.getKeyCode("down")) {
                    if (menu_0_sel < menu_0.size() - 1) {
                        this.menu_0_sel = menu_0_sel + 1;
                    }
                }
                if (keyCode == midlet.getKeyCode("z")) {
                    String selectedItem = (String) menu_0.elementAt(menu_0_sel);
                    if (selectedItem.equals("Begin Game")) {
                        this.view = 3;
                    }
                    if (selectedItem.equals("Settings")) {
                        midlet.stopAllMIDI();
                        this.view = 1;
                    }
                }
                break;
            case 1:
                if (keyCode == midlet.getKeyCode("up")) {
                    if (menu_1_sel > 0) {
                        this.menu_1_sel = menu_1_sel - 1;
                    }
                }
                if (keyCode == midlet.getKeyCode("down")) {
                    if (menu_1_sel < menu_1.size() - 1) {
                        this.menu_1_sel = menu_1_sel + 1;
                    }
                }
                if (keyCode == midlet.getKeyCode("z")) {
                    String selectedItem = (String) menu_1.elementAt(menu_1_sel);
                    if (selectedItem.equals("EXIT")) {
                        midlet.saveBooleanData("onMusic", onMusic);
                        midlet.saveBooleanData("onIntro", onIntro);
                        midlet.playMIDI("/midi/mus_menu01.mid", -1, midlet.getBooleanData("onMusic"));
                        this.view = 0;
                    }
                    if (selectedItem.equals("CONTROL OPTIONS")) {
                        this.view = 2;
                    }
                    if (selectedItem.equals("MUSIC:")) {
                        this.onMusic = !onMusic;
                    }
                    if (selectedItem.equals("INTRO:")) {
                        this.onIntro = !onIntro;
                    }
                }
                break;
            case 2:
                if (onEdit == false) {
                    if (keyCode == midlet.getKeyCode("up")) {
                        if (menu_2_sel > 0) {
                            this.menu_2_sel = menu_2_sel - 1;
                        }
                    }
                    if (keyCode == midlet.getKeyCode("down")) {
                        if (menu_2_sel < 2) {
                            this.menu_2_sel = menu_2_sel + 1;
                        }
                    }
                    if (keyCode == midlet.getKeyCode("z")) {
                        String selectedItem = (String) menu_2.elementAt(menu_2_sel);
                        if (selectedItem.equals("BACK")) {
                            this.view = 1;
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
                break;
            case 3:
                if (keyCode == midlet.getKeyCode("up")) {
                    if (menu_3_sel >= 7) menu_3_sel -= 7;
                } else if (keyCode == midlet.getKeyCode("down")) {
                    if (menu_3_sel + 7 < menu_3.size()) menu_3_sel += 7;
                } else if (keyCode == midlet.getKeyCode("left")) {
                    if (menu_3_sel > 0) menu_3_sel--;
                } else if (keyCode == midlet.getKeyCode("right")) {
                    if (menu_3_sel < menu_3.size() - 1) menu_3_sel++;
                } else if (keyCode == midlet.getKeyCode("z")) {
                    if (playerName.length() < 6) {
                        String selectedItem = (String) menu_3.elementAt(menu_3_sel);
                        playerName += selectedItem;
                    }
                } else if (keyCode == midlet.getKeyCode("bksc")) {
                    if (playerName.length() > 0) {
                        playerName = playerName.substring(0, playerName.length() - 1);
                    }
                } else if (keyCode == midlet.getKeyCode("lb")) {
                    this.view = 0;
                } else if (keyCode == midlet.getKeyCode("rb")) {
                    if (playerName.length() > 0) {
                        this.view = 4;
                    }
                }

                // Clamp index
                if (menu_3_sel < 0) menu_3_sel = 0;
                if (menu_3_sel >= menu_3.size()) menu_3_sel = menu_3.size() - 1;
                break;
            case 4:
                if (keyCode == midlet.getKeyCode("lb")) {
                        this.view = 3;
                }
                if (keyCode == midlet.getKeyCode("rb")) {
                    if (RBText == "Yes") {
                        midlet.stopAllMIDI();
                        midlet.saveBooleanData("data_onSave", false);
                        midlet.saveStringData("data_playerName", playerName);
                        midlet.saveIntData("data_playerLV", 1);
                        midlet.saveIntData("data_playerHP", 20);
                        midlet.saveIntData("data_playerHPMax", 20);
                        midlet.saveIntData("data_playerAT", 0);
                        midlet.saveIntData("data_playerWeaponAT", 0);
                        midlet.saveIntData("data_playerDF", 0);
                        midlet.saveIntData("data_playerArmorDF", 0);
                        midlet.saveStringData("data_playerWeapon", "Stick");
                        midlet.saveStringData("data_playerArmor", "Bandage");
                        midlet.saveIntData("data_playerExp", 0);
                        midlet.saveIntData("data_playerExpNext", 10);
                        midlet.saveIntData("data_playerKills", 0);
                        midlet.saveIntData("data_playerLocation", 0);
                        midlet.saveIntData("data_fun", 0);
                        midlet.saveIntData("data_ruins_kills", 0);
                        midlet.saveIntData("data_ruins_dummy", 0);
                        midlet.saveIntData("data_ruins_toriel", 0);
                        midlet.saveIntData("data_snowdin_kills", 0);
                        midlet.saveIntData("data_snowdin_doggo", 0);
                        midlet.saveIntData("data_snowdin_dogi", 0);
                        midlet.saveIntData("data_snowdin_greaterDog", 0);
                        midlet.saveIntData("data_snowdin_comedian", 0);
                        midlet.saveIntData("data_snowdin_papyrus", 0);
                        midlet.saveIntData("data_waterfall_kills", 0);
                        midlet.saveIntData("data_waterfall_shyren", 0);
                        midlet.saveIntData("data_waterfall_dummy", 0);
                        midlet.saveIntData("data_waterfall_undyne", 0);
                        midlet.saveIntData("data_waterfall_undyneTheUndying", 0);
                        midlet.saveIntData("data_hotland_kills", 0);
                        midlet.saveIntData("data_hotland_broGuards", 0);
                        midlet.saveIntData("data_hotland_muffet", 0);
                        midlet.saveIntData("data_hotland_mettaton", 0);
                        midlet.saveBooleanData("data_cellphone", false);
                        midlet.saveStringData("data_inventorySlot_0", "");
                        midlet.saveStringData("data_inventorySlot_1", "");
                        midlet.saveStringData("data_inventorySlot_2", "");
                        midlet.saveStringData("data_inventorySlot_3", "");
                        midlet.saveStringData("data_inventorySlot_4", "");
                        midlet.saveStringData("data_inventorySlot_5", "");
                        midlet.saveStringData("data_inventorySlot_6", "");
                        midlet.saveStringData("data_inventorySlot_7", "");
                        midlet.saveStringData("data_chestSlot_0", "");
                        midlet.saveStringData("data_chestSlot_1", "");
                        midlet.saveStringData("data_chestSlot_2", "");
                        midlet.saveStringData("data_chestSlot_3", "");
                        midlet.saveStringData("data_chestSlot_4", "");
                        midlet.saveStringData("data_chestSlot_5", "");
                        midlet.saveStringData("data_chestSlot_6", "");
                        midlet.saveStringData("data_chestSlot_7", "");
                        midlet.saveStringData("data_chestSlot_8", "");
                        midlet.saveStringData("data_chestSlot_9", "");
                        midlet.saveStringData("data_twoChestSlot_0", "");
                        midlet.saveStringData("data_twoChestSlot_1", "");
                        midlet.saveStringData("data_twoChestSlot_2", "");
                        midlet.saveStringData("data_twoChestSlot_3", "");
                        midlet.saveStringData("data_twoChestSlot_4", "");
                        midlet.saveStringData("data_twoChestSlot_5", "");
                        midlet.saveStringData("data_twoChestSlot_6", "");
                        midlet.saveStringData("data_twoChestSlot_7", "");
                        midlet.saveStringData("data_twoChestSlot_8", "");
                        midlet.saveStringData("data_twoChestSlot_9", "");
                        midlet.saveStringData("data_cellphoneSlot_0", "");
                        midlet.saveStringData("data_cellphoneSlot_1", "");
                        midlet.saveStringData("data_cellphoneSlot_2", "");
                        midlet.saveStringData("data_cellphoneSlot_3", "");
                        midlet.saveStringData("data_cellphoneSlot_4", "");
                        midlet.saveStringData("data_cellphoneSlot_5", "");
                        midlet.saveStringData("data_cellphoneSlot_6", "");
                        midlet.saveStringData("data_cellphoneSlot_7", "");
                        manager.setScene(new room_area1(manager, "start"));
                        // midlet.saveIntData("data_", 0);
                        // midlet.saveStringData("data_", "");
                        // midlet.saveBooleanData("data_", false);
                    }
                }
                break;
        }
    }
}
