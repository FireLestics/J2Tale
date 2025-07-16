package ui;

import javax.microedition.lcdui.*;
import java.io.IOException;
import app.MainMIDlet;
import java.util.Vector;
import tools.ImageDrawer;
import tools.TextBlitter;

public class game_selectName extends AbstractCanvas {
    private TextBlitter textBlitter;
    private Vector nameItems;

    private int nameSelIndex = 0;
    private String playerName = "";

    private int width;
    private int height;

    private final String helpText = "[8 or BKSC] - Backspace";
    private final String LBText = "Quit";
    private final String RBText = "Done";
    private final String TopBanner = "Name the fallen human.";
    private final String ENTERText = "ENTER";

    private MainMIDlet midlet;
    private int prevSel;

    public game_selectName(MainMIDlet midlet, int prevSel, String name) {
        super(midlet);
        this.midlet = midlet;
        this.prevSel = prevSel;
        this.playerName = name;

        textBlitter = new TextBlitter();
        try {
            textBlitter.loadFont("fnt_maintext", "default");
            textBlitter.loadFont("fnt_main", "default");
            textBlitter.setFont("fnt_maintext", "white");
        } catch (IOException e) {
            e.printStackTrace();
        }

        nameItems = new Vector();
        for (char c = 'A'; c <= 'Z'; c++) {
            nameItems.addElement(String.valueOf(c));
        }
    }

    protected void update() {}

    protected void paint(Graphics g) {
        this.width = getWidth();
        this.height = getHeight();

        g.setColor(0x000000);
        g.fillRect(0, 0, width, height);

        // Easter egg
        if (playerName.equals("GASTER")) {
            midlet.switchCanvas(new game_start(midlet));
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

        for (int i = 0; i < nameItems.size(); i++) {
            String item = (String) nameItems.elementAt(i);
            int row = i / 7;
            int col = i % 7;
            int x = startPosX + (col * 24);
            int y = startPosY + (row * 16);

            String color = (i == nameSelIndex) ? "yellow" : "white";
            textBlitter.setFont("fnt_maintext", color);
            textBlitter.drawString(g, "<shake:1,1>" + item + "<shake:off>", x, y);
        }
    }

    protected void keyPressed(int keyCode) {
        if (keyCode == midlet.getKeyCode("up")) {
            if (nameSelIndex >= 7) nameSelIndex -= 7;
        } else if (keyCode == midlet.getKeyCode("down")) {
            if (nameSelIndex + 7 < nameItems.size()) nameSelIndex += 7;
        } else if (keyCode == midlet.getKeyCode("left")) {
            if (nameSelIndex > 0) nameSelIndex--;
        } else if (keyCode == midlet.getKeyCode("right")) {
            if (nameSelIndex < nameItems.size() - 1) nameSelIndex++;
        } else if (keyCode == midlet.getKeyCode("z")) {
            if (playerName.length() < 6) {
                String selectedItem = (String) nameItems.elementAt(nameSelIndex);
                playerName += selectedItem;
            }
        } else if (keyCode == midlet.getKeyCode("bksc")) {
            if (playerName.length() > 0) {
                playerName = playerName.substring(0, playerName.length() - 1);
            }
        } else if (keyCode == midlet.getKeyCode("lb")) {
            midlet.switchCanvas(new game_introduction(midlet, prevSel));
        } else if (keyCode == midlet.getKeyCode("rb")) {
            if (playerName.length() > 0) {
                midlet.switchCanvas(new game_acceptName(midlet, playerName, nameSelIndex, prevSel));
            }
        }

        // Clamp index
        if (nameSelIndex < 0) nameSelIndex = 0;
        if (nameSelIndex >= nameItems.size()) nameSelIndex = nameItems.size() - 1;
    }

    private void drawText(Graphics g, String font, String color, String text, int x, int y) {
        textBlitter.setFont(font, color);
        textBlitter.drawString(g, text, x, y);
    }

    public void destroy() {
        midlet.cleanupResources();
    }
}
