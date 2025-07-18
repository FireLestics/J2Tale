package j2tale_base.scenes;

import javax.microedition.lcdui.*;
import j2tale_base.Midlet;
import j2tale_base.tools.TextBlitter;
import j2tale_base.tools.ImageDrawer;
import j2tale_base.objects.Player;
import j2tale_base.objects.TilesetMap;

public abstract class RoomBase extends AbstractCanvas {
    protected Player player;
    protected TextBlitter textBlitter;
    protected ImageDrawer imageDrawer = new ImageDrawer();

    protected TilesetMap[] mapLayers;
    protected TilesetMap collisions;

    protected int width = getWidth();
    protected int height = getHeight();

    protected boolean upPressed, downPressed, leftPressed, rightPressed;

    protected int keyCode;
    protected String bgImagePath;

    protected boolean onDialog = false;
    protected String[] dialogs;
    protected int dialogPos, dialogNum, dialogStrMax;
    protected boolean onDownView, onFace;

    protected Midlet midlet;

    public RoomBase(Midlet midlet) {
        super(midlet);
        this.midlet = midlet;
        setFullScreenMode(true);
        midlet.loadAllKeyCode();

        textBlitter = new TextBlitter();
        imageDrawer = new ImageDrawer();

        try {
            textBlitter.loadFont("fnt_maintext", "default");
            textBlitter.loadFont("fnt_small", "default");
        } catch (Exception e) {
            System.out.println("Ошибка загрузки шрифтов: " + e.getMessage());
        }

        player = new Player("Frisk", 60, 20, 3, 10);
        player.setCollisionRect(-7, -8, 15, 8);
        player.setScreenSize(width, height);
        player.setDebugMode(true);
    }

    protected void loadMapLayers(String basePath, String imagePath, int tileWidth, int tileHeight, int layerCount) {
        TilesetMap[] layers = new TilesetMap[layerCount];

        for (int i = 0; i < layerCount; i++) {
            try {
                String layerPath = (i == 0)
                        ? basePath + "_map.csv"
                        : basePath + "_map" + (i + 1) + ".csv";

                layers[i] = new TilesetMap(layerPath, imagePath, tileWidth, tileHeight);
            } catch (Exception e) {
                System.out.println("Ошибка при загрузке слоя " + (i + 1) + ": " + e.getMessage());
                layers[i] = null;
            }
        }

        this.mapLayers = layers;
    }
    
    protected void loadMapLayers(String basePath, int tileWidth, int tileHeight) {
        TilesetMap[] layers = new TilesetMap[1];
        layers[0] = new TilesetMap(basePath, tileWidth, tileHeight);
        this.mapLayers = layers;
    }

    protected void drawMaps(Graphics g) {
        if (mapLayers == null) return;

        for (int i = 0; i < mapLayers.length; i++) {
            TilesetMap map = mapLayers[i];
            if (map != null) {
                map.setCamera(player.getCameraX(), player.getCameraY());
                map.draw(g, getWidth(), getHeight(), player.getX(), player.getY());
            }
        }
    }

    protected void drawCommon(Graphics g) {
        width = getWidth();
        height = getHeight();

        g.setColor(0x000000);
        g.fillRect(0, 0, width, height);

        drawMaps(g);

        if (bgImagePath != null) {
            imageDrawer.drawImage(g, bgImagePath, 0 - player.getCameraX(), 0 - player.getCameraY(), 0, 0);
        }

        player.draw(g, imageDrawer);
        player.drawDebug(g, true, true, false, false);
        drawDialog(g);
    }

    protected void updatePlayerMovement() {
        if (upPressed) player.moveUp();
        if (downPressed) player.moveDown();
        if (leftPressed) player.moveLeft();
        if (rightPressed) player.moveRight();
    }

    protected void drawDialog(Graphics g) {
        if (!onDialog || dialogs == null || dialogNum >= dialogs.length) return;

        int x = 4;
        int y = 4;
        int dWidth = 232;
        int dHeight = 82;
        int textX = 12;
        int textY = 8;

        player.pauseMovement();

        y = (onDownView) ? (height - dHeight - 4) : 4;
        textX = (onFace) ? 74 : 12;

        g.setColor(0x000000);
        g.fillRect(x, y, dWidth, dHeight);
        g.setColor(0xFFFFFF);
        drawThickRect(g, x, y, dWidth, dHeight, 3);

        if (dialogPos < dialogs[dialogNum].length()) {
            dialogPos++;
        }

        textBlitter.setFont("fnt_maintext", "white");
        textBlitter.drawTypingText(g, dialogs[dialogNum], x + textX, y + textY, dialogPos);

        g.setClip(0, 0, getWidth(), getHeight());
    }

    public void startDialog(String[] dialogs, boolean face) {
        this.dialogs = dialogs;
        this.onFace = face;
        this.onDownView = player.getY() >= (height / 2);
        this.dialogNum = 0;
        this.dialogPos = 0;
        this.dialogStrMax = dialogs.length - 2;
        this.onDialog = true;
    }

    public void handleDialogKey(int keyCode) {
        if (!onDialog || dialogs == null || dialogNum >= dialogs.length) return;

        if (keyCode == midlet.getKeyCode("z")) {
            if (dialogNum > dialogStrMax) {
                this.onDialog = false;
                this.dialogPos = 0;
                this.dialogNum = 0;
                this.dialogs = new String[] {};
                this.onFace = false;
                this.onDownView = false;
                player.resumeMovement();
            } else if (dialogPos >= textBlitter.stringLength(dialogs[dialogNum])) {
                dialogNum++;
                dialogPos = 0;
            }
        }

        if (keyCode == midlet.getKeyCode("x")) {
            dialogPos = dialogs[dialogNum].length();
        }
    }

    public void drawThickRect(Graphics g, int x, int y, int w, int h, int thickness) {
        for (int i = 0; i < thickness; i++) {
            g.drawRect(x + i, y + i, w - 2 * i, h - 2 * i);
        }
    }

    public void handleKeyDown(int keyCode) {
        this.keyCode = keyCode;

        if (!onDialog) {
            if (keyCode == midlet.getKeyCode("up")) upPressed = true;
            if (keyCode == midlet.getKeyCode("down")) downPressed = true;
            if (keyCode == midlet.getKeyCode("left")) leftPressed = true;
            if (keyCode == midlet.getKeyCode("right")) rightPressed = true;
        } else {
            handleDialogKey(keyCode);
        }
    }

    public void handleKeyUp(int keyCode) {
        if (keyCode == midlet.getKeyCode("up")) upPressed = false;
        if (keyCode == midlet.getKeyCode("down")) downPressed = false;
        if (keyCode == midlet.getKeyCode("left")) leftPressed = false;
        if (keyCode == midlet.getKeyCode("right")) rightPressed = false;

        player.changeAnimation("Idle", player.getCurrentDirection());
    }

    public void setBgImagePath(String path) {
        this.bgImagePath = path;
    }
}
