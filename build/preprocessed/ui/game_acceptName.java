package ui;

import javax.microedition.lcdui.*;
import java.io.IOException;
import app.MainMIDlet;
import tools.ImageDrawer;
import tools.TextBlitter;

public class game_acceptName extends AbstractCanvas {
    private TextBlitter textBlitter;
    private ImageDrawer imageDrawer;
    
    private int width;
    private int height;
    
    private int prevSel = 0;
    private int prev2Sel = 0;
    
    private String playerName = "";
    private String TopBanner = "Is this name correct?";
    
    String LBText = "No";
    String RBText = "Yes";

    public game_acceptName(MainMIDlet midlet, String name, int prevSel, int prev2Sel) {
        super(midlet);
        
        setFullScreenMode(true);
        textBlitter = new TextBlitter();
        try {
            textBlitter.loadFont("fnt_maintext", "default");
            textBlitter.loadFont("fnt_main", "default");
        } catch (IOException e) {}
        
        this.prevSel = prevSel;
        this.prev2Sel = prev2Sel;
        this.playerName = name;
    }
	
    protected void update() {}

    protected void paint(Graphics g) {
        this.width = getWidth();
        this.height = getHeight();
        
	g.setColor(0x000000);
        g.fillRect(0, 0, getWidth(), getHeight() + 20);
		
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

    protected void keyPressed(int keyCode) {
        if (keyCode == midlet.getKeyCode("lb")) {
            midlet.switchCanvas(new game_selectName(midlet, prev2Sel, playerName));
        }
        if (keyCode == midlet.getKeyCode("rb")) {
            if (RBText == "Yes") {
                midlet.stopAllMIDI();
                midlet.saveStringData("playerName", playerName);
                midlet.switchCanvas(new room_overworld(midlet));
            }
        }
    }

    public void destroy() {
        midlet.cleanupResources();
        System.out.println("");
    }
}