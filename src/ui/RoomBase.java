package ui;

import javax.microedition.lcdui.*;
import app.MainMIDlet;
import tools.TextBlitter;
import tools.ImageDrawer;
import ui.objects.Player;
import ui.objects.TilesetMap;

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

    protected boolean onDialog = false;
    protected String[] dialogs;
    protected int dialogPos, dialogNum, dialogStrMax;
    protected boolean onDownView, onFace;

    protected MainMIDlet midlet;

    public RoomBase(MainMIDlet midlet) {
        super(midlet);
        this.midlet = midlet;
        setFullScreenMode(true);
        midlet.loadAllKeyCode();

        textBlitter = new TextBlitter();
        imageDrawer = new ImageDrawer();

        try {
            textBlitter.loadFont("fnt_maintext", "default");
            textBlitter.loadFont("fnt_small", "default");
        } catch (Exception e) {}

        player = new Player("Frisk", 60, 20, 3, 10);
        player.setCollisionRect(-7, -8, 15, 8);
        player.setScreenSize(width, height);
        player.setDebugMode(true);
    }
    
    // Локации и карты
    protected void loadMapLayers(String basePath, String imagePath, int tileWidth, int tileHeight, int layerCount) {
        TilesetMap[] layers = new TilesetMap[layerCount];

        for (int i = 0; i < layerCount; i++) {
            try {
                if (i == 0) {
                    String layerPath = basePath + "_map.csv";
                    layers[i] = new TilesetMap(layerPath, imagePath, tileWidth, tileHeight);
                } else {
                    String layerPath = basePath + "_map" + (i + 1) + ".csv";
                    layers[i] = new TilesetMap(layerPath, imagePath, tileWidth, tileHeight);
                }
            } catch (Exception e) {
                System.out.println("Ошибка при загрузке слоя " + (i + 1) + ": " + e.getMessage());
                layers[i] = null;
            }
        }
        this.mapLayers = layers;
    }
    
    protected void drawMaps(Graphics g) {
        for (int i = 0; i < mapLayers.length; i++) {
            if (mapLayers[i] != null) {
                mapLayers[i].setCamera(player.getCameraX(), player.getCameraY());
                mapLayers[i].draw(g, getWidth(), getHeight(), player.getX(), player.getY());
            }
        }
    }

    // Графика
    protected void drawCommon(Graphics g) {
        width = getWidth();
        height = getHeight();

        g.setColor(0x000000);
        g.fillRect(0, 0, width, height);

        drawMaps(g);
        player.draw(g, imageDrawer);
        player.drawDebug(g, true, false, false, true);
        drawDialog(g);
    }

    // Передвижение игрока
    protected void updatePlayerMovement() {
        if (upPressed) player.moveUp();
        if (downPressed) player.moveDown();
        if (leftPressed) player.moveLeft();
        if (rightPressed) player.moveRight();
    }

    // Диалоги
    protected void drawDialog(Graphics g) {
        if (!onDialog) return;
        
        int x = 4;
        int y = 4;
        int dWidth = 232;
        int dHeight = 82;
        int textX = 12;
        int textY = 8;
        int dia = midlet.getScreenDia(width, height);
        
        player.pauseMovement();

        if (onDownView) {
            y = (height - dHeight) - 4;
        } else {
            y = 4;
        }

        if (onFace) {
            textX = 74;
        } else {
            textX = 12;
        }

        g.setColor(0x000000);
        g.fillRect(x, y, dWidth, dHeight);
        g.setColor(0xFFFFFF);
        drawThickRect(g, x, y, dWidth, dHeight, 3);

        if (dialogPos < dialogs[dialogNum].length()) {
             this.dialogPos++;
        }

        textBlitter.setFont("fnt_maintext", "white");
        textBlitter.drawTypingText(g, dialogs[dialogNum], x + textX, y + textY, dialogPos);

        g.setClip(0, 0, getWidth(), getHeight());
    }
    
    public void startDialog(String[] dialogs, boolean face) {
        this.dialogs = dialogs;
        if (player.getY() < (height / 2)) {
            this.onDownView = false;
        } else {
            this.onDownView = true;
        }
        this.onFace = face;
        this.dialogPos = 0;
        this.dialogNum = 0;
        this.dialogStrMax = dialogs.length - 2;
        this.onDialog = true;
    }

    public void handleDialogKey(int keyCode) {
        if (!onDialog) return;

        if (keyCode == midlet.getKeyCode("z")) {
            if (dialogNum > dialogStrMax) {
                this.dialogs = new String[] {};
                this.onDownView = false;
                this.onFace = false;
                this.dialogPos = 0;
                this.dialogNum = 0;
                this.dialogStrMax = 0;
                this.onDialog = false;
                player.resumeMovement();
            } else if (dialogPos >= textBlitter.stringLength(dialogs[dialogNum])) {
                this.dialogNum = dialogNum + 1;
                this.dialogPos = 0;
            }
        }
        if (keyCode == midlet.getKeyCode("x")) {
            this.dialogPos = dialogs[dialogNum].length();
        }
    }

    // Дополнительные (вспомогательные) функции
    public void drawThickRect(Graphics g, int x, int y, int w, int h, int thickness) {
        for (int i = 0; i < thickness; i++) {
            g.drawRect(x + i, y + i, w - 2 * i, h - 2 * i);
        }
    }

    // Управление
    public void handleKeyDown(int keyCode) {
        this.keyCode = keyCode;

        if (!onDialog) {
            if (keyCode == midlet.getKeyCode("up")) upPressed = true;
            if (keyCode == midlet.getKeyCode("down")) downPressed = true;
            if (keyCode == midlet.getKeyCode("left")) leftPressed = true;
            if (keyCode == midlet.getKeyCode("right")) rightPressed = true;
        }

        if (onDialog) {
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
}
