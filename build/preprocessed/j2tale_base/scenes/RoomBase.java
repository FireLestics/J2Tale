package j2tale_base.scenes;

import javax.microedition.lcdui.*;
import java.util.Hashtable;
import j2tale_base.Midlet;
import j2tale_base.tools.TextBlitter;
import j2tale_base.tools.ImageDrawer;
import j2tale_base.objects.Player;
import j2tale_base.objects.TilesetMap;

public abstract class RoomBase extends Scene {
    protected Player player;
    protected Midlet midlet;
    protected TextBlitter textBlitter;
    protected ImageDrawer imageDrawer = new ImageDrawer();

    protected TilesetMap[] mapLayers;
    protected TilesetMap collisions;

    protected int width = getWidth();
    protected int height = getHeight();
    
    protected int roomId = -1;
    protected String roomName = "Без имени";
    protected Hashtable spawnPoints = new Hashtable();
    
    private String[] playerMenu = new String[] {"ITEM", "STAT" ,"CELL"};
    private int playerMenuSel = 0;

    protected boolean upPressed, downPressed, leftPressed, rightPressed;

    protected int keyCode;
    protected String bgImagePath;

    protected boolean onDialog = false;
    protected String[] dialogs;
    protected int dialogPos, dialogNum, dialogStrMax;
    protected boolean onDownView, onFace;
    
    private int onMenu = 0;
    private int menuYScale = 38;
    
    private String playerName;
    private int playerLV;
    private int playerHP;
    private int playerHPMax;
    private int playerGolds;

    public RoomBase(SceneManager manager, Midlet midlet) {
        super(manager);
        this.midlet = midlet;
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
    
    protected void loadMapLayers(String basePath, String imagePath, String imageMapPath, int tileWidth, int tileHeight, int layerCount) {
        TilesetMap[] layers = new TilesetMap[layerCount + 1];

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
        layers[layerCount] = new TilesetMap(basePath, tileWidth, tileHeight);
        
        this.mapLayers = layers;
    }
    
    protected void createFormedColission(String form, int x, int y, int rotation) {
        int width = 19;
        int height = 19;
        int i;
        if ("form1".equals(form)) {
            if (rotation == 0) {
                player.addCollisionBox(x, (height + y) - 1, width, 1);
                for (i = 0; i <= ((width + height + 2) / 4) - 2; i++) {
                    player.addCollisionBox(x + 2 + (i * 2), (height + y) - 2 - (i * 2), 1, 1);
                }
                player.addCollisionBox(x+18, (height + y) - 19, 1, height);
            } else if (rotation == 1) {
                player.addCollisionBox(x, y, 1, height);
                for (i = 0; i <= ((width + height + 2) / 4) - 2; i++) {
                    player.addCollisionBox(x + 2 + (i * 2), y + 2 + (i * 2), 1, 1);
                }
                player.addCollisionBox(x, y + height, width, 1);
            } else if (rotation == 2) {
                // Повернутый на 180 градусов вариант (пример)
                player.addCollisionBox(x, y, width, 1);
                for (i = 0; i <= ((width + height + 2) / 4) - 2; i++) {
                    player.addCollisionBox(x + width - 3 - i*2, y + 2 + i * 2, 1, 1);
                }
                player.addCollisionBox(x, y, 1, height);
            } else if (rotation == 3) {
                // Повернутый на 270 градусов вариант (пример)
                player.addCollisionBox(x, y, width, 1);
                for (i = 0; i <= ((width + height + 2) / 4) - 2; i++) {
                    player.addCollisionBox(x + width - 2 - i * 2, y + height - 2 - i * 2, 1, 1);
                }
                player.addCollisionBox(x + width, y, 1, height);
            }
        }
    }

    protected void drawMaps(Graphics g) {
        if (mapLayers == null) return;

        for (int i = 0; i < mapLayers.length; i++) {
            TilesetMap map = mapLayers[i];
            if (map != null) {
                map.setCamera(player.getCameraX(), player.getCameraY());
                map.draw(g, getWidth(), getHeight(), player.getX(), player.getY(), width, height);
            }
        }
    }

    protected void drawCommon(Graphics g) {
        width = getWidth();
        height = getHeight();

        g.setColor(0x000000);
        g.fillRect(0, 0, width, height);

        drawMaps(g);
        g.setClip(0, 0, width, height);

        if (bgImagePath != null) {
            imageDrawer.drawImage(g, bgImagePath, 0 - player.getCameraX(), 0 - player.getCameraY(), 0, 0);
        }
        g.setClip(0, 0, width, height);

        player.draw(g, imageDrawer);
        g.setClip(0, 0, width, height);
        // player.drawDebug(g, true, true, true, true);
        // g.setClip(0, 0, width, height);
        drawPlayerMenu(g);
        g.setClip(0, 0, width, height);
        drawDialog(g);
        g.setClip(0, 0, width, height);
        
        String info;
        info = "X/Y: " + player.getX() + "/" + player.getY() + "\n";
        info = info + "Triggers: " + player.getTriggersCount() + "\n";
        info = info + "Collisions: " + player.getColissionsCount() + "\n";
        info = info + "Npcs: " + player.getNpcsCount();
        
//        textBlitter.setFont("fnt_maintext", "yellow");
//        textBlitter.drawString(g, info, 5, 5);
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
    
    public void drawPlayerMenu(Graphics g) {
        if (onMenu >= 1) {
            player.pauseMovement();
            
            g.setColor(0x000000);
            g.fillRect(5, 5, 70, 48);
            g.setColor(0xFFFFFF);
            drawThickRect(g, 5, 5, 70, 48, 3);
            g.setClip(0, 0, getWidth(), getHeight());
            
            g.setColor(0x000000);
            g.fillRect(5, 58, 70, menuYScale);
            g.setColor(0xFFFFFF);
            drawThickRect(g, 5, 58, 70, menuYScale, 3);
            g.setClip(0, 0, getWidth(), getHeight());

            textBlitter.setFont("fnt_maintext", "white");
            textBlitter.drawString(g, playerName, 5 + 8, 5 + 5);
            textBlitter.setFont("fnt_small", "white");
            textBlitter.drawString(g, "LV\nHP\nG", 5 + 8, 5 + 20);
            textBlitter.drawString(g, "" + playerLV + "\n" + playerHP + "/" + playerHPMax + "\n" + playerGolds, 5 + 24, 5 + 20);
            g.setClip(0, 0, getWidth(), getHeight());
            
            for (int i = 0; i < playerMenu.length; i++) {
                this.menuYScale = 38 + (16 * i);
                String item = playerMenu[i];
                textBlitter.setFont("fnt_maintext", "white");
                textBlitter.drawString(g, item, 5 + 28, 58 + 11 + (16 * i));
            }
            g.setClip(0, 0, width, height);
            
            if (onMenu == 1) {
                imageDrawer.drawImage(g, "/images/assets/soul.png", 22, 58 + 19 + (16 * playerMenuSel), 4, 4);
            }
            g.setClip(0, 0, width, height);
        }
        if (onMenu == 2) {
            if (width < 320) {
                g.setColor(0x000000);
                g.fillRect(55, 5, 180, 220);
                g.setColor(0xFFFFFF);
                drawThickRect(g, 55, 5, 180, 220, 3);
                g.setClip(0, 0, width, height);
                
                String playerInfo = "\"" + playerName + "\"\n\n";
                playerInfo = playerInfo + "LV " + playerLV + "\n";
                playerInfo = playerInfo + "HP " + playerHP + " / " + playerHPMax + "\n\n";
                playerInfo = playerInfo + "AT 99 (99)" + "      ";
                playerInfo = playerInfo + "EXP: 10000" + "\n";
                playerInfo = playerInfo + "DF 99 (99)" + "      ";
                playerInfo = playerInfo + "NEXT: 100000" + "\n\n";
                playerInfo = playerInfo + "WEAPON: Stick" + "\n";
                playerInfo = playerInfo + "ARMOR: Bandage" + "\n\n";
                playerInfo = playerInfo + "GOLD: " + playerGolds;
                textBlitter.setFont("fnt_maintext", "white");
                textBlitter.drawString(g, playerInfo, 18+50, 20);
                g.setClip(0, 0, width, height);
            }
        }
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
            if (keyCode == midlet.getKeyCode("c")) { 
                onMenu = 1;
                
                playerName = midlet.getStringData("data_playerName");
                playerLV = midlet.getIntData("data_playerLV");
                playerHP = midlet.getIntData("data_playerHP");
                playerHPMax = midlet.getIntData("data_playerHPMax");
                playerGolds = midlet.getIntData("data_playerGolds");
            }
        } else {
            handleDialogKey(keyCode);
        }
        if (keyCode == midlet.getKeyCode("x") && onMenu == 1) { onMenu = 0; playerMenuSel = 0; player.resumeMovement(); }
        if (keyCode == midlet.getKeyCode("up") && onMenu == 1 && playerMenuSel > 0) playerMenuSel--;
        if (keyCode == midlet.getKeyCode("down") && onMenu == 1 && playerMenuSel < playerMenu.length - 1) playerMenuSel++;
        if (keyCode == midlet.getKeyCode("z") && onMenu == 1 && playerMenuSel == 1) onMenu = 2;
        if (keyCode == midlet.getKeyCode("x") && onMenu == 2 && playerMenuSel == 1) onMenu = 1;
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
    
    public int getRoomId() {
        return roomId;
    }

    public void setRoomId(int roomId) {
        this.roomId = roomId;
    }

    public String getRoomName() {
        return roomName;
    }

    public void setRoomName(String name) {
        this.roomName = name;
    }

    public void addSpawnPoint(String id, int[] pos) {
        spawnPoints.put(id, pos);
    }

    public int[] getSpawnPoint(String id) {
        return (int[]) spawnPoints.get(id);
    }

    public Hashtable getSpawnPoints() {
        return spawnPoints;
    }
    
    public void spawnPlayerAt(String spawnId) {
        int[] pos = getSpawnPoint(spawnId);
        if (pos != null && pos.length >= 2) {
            player.setX(pos[0]);
            player.setY(pos[1]);
        } else {
            System.out.println("Spawn point not found: " + spawnId);
        }
    }
}
