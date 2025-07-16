package ui;

import javax.microedition.lcdui.*;
import java.io.IOException;
import java.io.InputStream;
import java.util.Vector;
import app.MainMIDlet;
import tools.TextBlitter;
import tools.ImageDrawer;
import ui.objects.Player;
import ui.objects.TilesetMap;

public class room_overworld extends AbstractCanvas {
    private Player player;
    private TextBlitter textBlitter;
    private TilesetMap map1;
    private TilesetMap map2;
    private TilesetMap map3;
    private TilesetMap collisions;
    private ImageDrawer imageDrawer = new ImageDrawer();
    
    private int width = getWidth();
    private int height = getHeight();

    private boolean upPressed = false;
    private boolean downPressed = false;
    private boolean leftPressed = false;
    private boolean rightPressed = false;
    private boolean dropDown = false;
    
    private int onMenu = 0;
    private int dropDownTimer = 0;
    private int menuYScale = 38;
    
    private boolean onDialog = false;
    private boolean onDownView = true;
    private boolean onFace = false;
    private String[] dialogs;
    private int dialogPos;
    private int dialogNum;
    private int dialogStrMax;
    
    private int keyCode;
    
    private int RenderRadiusX = (width / 20);
    private int RenderRadiusY = (height / 20) + 2;
    
    private String playerName = midlet.getStringData("data_playerName");
    private int playerLV = midlet.getIntData("data_playerLV");
    private int playerHP = midlet.getIntData("data_playerHP");
    private int playerHPMax = midlet.getIntData("data_playerHPMax");
    private int playerGolds = midlet.getIntData("data_playerGolds");

    public room_overworld(MainMIDlet midlet) {
        super(midlet);
        setFullScreenMode(true);
        
        midlet.loadAllKeyCode();
        imageDrawer = new ImageDrawer();
        
        textBlitter = new TextBlitter();
        try {
            textBlitter.loadFont("fnt_maintext", "default");
            textBlitter.loadFont("fnt_small", "default");
        } catch (IOException e) {}
        
        player = new Player("Frisk", 60, 20, 3, 10);
        
        player.clearCollisionBoxes();
        map1 = new TilesetMap("/maps/ruins19_map.csv", "/images/tiles/ruins.png", 20, 20);
        map2 = new TilesetMap("/maps/ruins19_map2.csv", "/images/tiles/ruins.png", 20, 20);
        map3 = new TilesetMap("/maps/ruins19_map3.csv", "/images/tiles/ruins.png", 20, 20);
        collisions = new TilesetMap("/maps/ruins19_collisions.csv", null, 20, 20);
        collisions.generateCollisions(player, new int[]{0});
        
        map1.setViewRadiusX(RenderRadiusX);
        map2.setViewRadiusX(RenderRadiusX);
        map3.setViewRadiusX(RenderRadiusX);
        map1.setViewRadiusY(RenderRadiusY);
        map2.setViewRadiusY(RenderRadiusY);
        map3.setViewRadiusY(RenderRadiusY);
        
        CreateColissions();
        
        player.setScreenSize(width, height);
        player.setMapSize(map1.getMapWidth(), map1.getMapHeight());
        
        player.setCollisionRect(-9, -8, 19, 8);
    }
    
    private void CreateColissions() {
        player.addTriggerZone(260, 160, 20, 20, "dropdown_1", "dropdown_1");
        player.addTriggerZone(264, 460, 12, 18, "returnRoom_1", "returnRoom_1");
        player.addTriggerZone(264, 600, 12, 18, "returnRoom_1", "returnRoom_1");
        player.addTriggerZone(424, 460, 12, 18, "returnRoom_2", "returnRoom_2");
        player.addTriggerZone(424, 600, 12, 18, "returnRoom_2", "returnRoom_2");
        player.addTriggerZone(564, 460, 12, 18, "returnRoom_3", "returnRoom_3");
        player.addTriggerZone(564, 600, 12, 18, "returnRoom_3", "returnRoom_3");
        
        player.addNPC(100, 90, 16, 32, "table");
    }

    protected void update() {
        updatePlayerPosition();
        player.update();
        player.updateCamera();
        
        if (player.isInsideTrigger("dropdown_1")) {
            player.pauseMovement();
            player.setNoClip(true);
            if (dropDownTimer >= 20) {
                this.dropDown = true;
            } else {
                this.dropDownTimer++;
            }
        }
        if (dropDown == true) {
            if ("dropdown_1".equals(player.getLastTriggerAction())) {
                player.setY(player.getY() + 4);
                player.pauseMovement();
                player.changeAnimation("DropDown", player.getCurrentDirection());
                if (player.getY() >= 510) {
                    player.setNoClip(false);
                    player.resumeMovement();
                    this.dropDownTimer = 0;
                    this.dropDown = false;
                    player.changeAnimation("Idle", player.getCurrentDirection());
                }
            }
        }
        
        if (player.isInsideTrigger("returnRoom_1")) {
            player.setX(190);
            player.setY(150);
        }
        if (player.isInsideTrigger("returnRoom_2")) {
            player.setX(430);
            player.setY(150);
        }
        if (player.isInsideTrigger("returnRoom_3")) {
            player.setX(570);
            player.setY(150);
        }
        
        if (keyCode == midlet.getKeyCode("z")) {
            if (player.isNearNPC("table")) {
                if (player.isFacingNPC("table")) {
                    startDialog(new String[] {"Привет всем, это тест <color:green><shake:1,1>диалогов<shake:off><color:white>...", "И они работают!!!"}, false);
                    this.keyCode = 0;
                }
            }
        }
    }

    protected void paint(Graphics g) {
        this.width = getWidth();
        this.height = getHeight();
        
        g.setColor(0x000000);
        g.fillRect(0, 0, getWidth(), getHeight());
        player.setDebugMode(false);
        
        map1.setCamera(player.getCameraX(), player.getCameraY());
        map2.setCamera(player.getCameraX(), player.getCameraY());
        map3.setCamera(player.getCameraX(), player.getCameraY());
        collisions.setCamera(player.getCameraX(), player.getCameraY());
        
        map1.draw(g, getWidth(), getHeight(), player.getX(), player.getY());
        map2.draw(g, getWidth(), getHeight(), player.getX(), player.getY());
        map3.draw(g, getWidth(), getHeight(), player.getX(), player.getY());
        player.draw(g, imageDrawer);
        player.drawDebug(g, true, false, false, true);
        
        if (onMenu >= 1) {
            player.pauseMovement();
            
            String[] menu = new String[] {"ITEM", "STAT" ,"CELL", "MAIL", "BAG"};
            
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
            
            for (int i = 0; i < menu.length; i++) {
                this.menuYScale = 38 + (16 * i);
                String item = menu[i];
                textBlitter.setFont("fnt_maintext", "white");
                textBlitter.drawString(g, item, 5 + 28, 58 + 11 + (16 * i));
            }
        }
        if (onMenu == 2) {
            g.setColor(0x000000);
        }
        
        if (onDialog) {
            player.pauseMovement();
            
            int x = 4;
            int y = 4;
            int dWidth = 232;
            int dHeight = 82;
            int textX = 12;
            int textY = 8;
            int dia = midlet.getScreenDia(width, height);
            
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
        
        // Основные функции
        // player.setScreenSize(240, 320);
        // player.setMapSize(640, 640);
        // player.addCollisionBox(140, 160, 48, 36);
        // player.addTriggerZone(100, 20, 32, 32, "id", "action");
        // player.addNPC(200, 20, 16, 32, "name");
        // player.setCollisionRect(4, 8, 24, 24);
        // player.clearCollisionBoxes();
        // player.setSpeed(20);
        // player.setAnimationSpeed(20);
        // player.changeAnimation("Idle", "up");
        // player.changeCharacter("Sans");
        // player.setDebugMode(true);
        // player.setNoClip(true);
        // player.pauseMovement();
        // player.resumeMovement();
        // player.moveUp();
        // player.moveDown();
        // player.moveLeft();
        // player.moveRight();
        // player.draw(g, imageDrawer);
        // player.drawDebug(g);
        // 
        
        // Геттеры
        // player.getCurrentFrameName() - String
        // player.getCurrentDirection() - String
        // player.getLastTriggerAction() - String
        // player.isInsideTrigger("triggerId") - Boolean
        // player.isNearNPC("npcName") - Boolean
        // player.isFacingNPC("npcName") - Boolean
        // player.getX() - Int
        // player.getY() - Int
        // player.getFrameWidth() - Int
        // player.getFrameHeight() - Int
        // player.getCameraX() - Int
        // player.getCameraY() - Int
    }
    
    public void drawThickRect(Graphics g, int x, int y, int w, int h, int thickness) {
        for (int i = 0; i < thickness; i++) {
            g.drawRect(x + i, y + i, w - 2 * i, h - 2 * i);
        }
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

    private void updatePlayerPosition() {
        if (upPressed && downPressed) {
            player.moveUp();
        }
        if (leftPressed && rightPressed) {
            player.moveLeft();
        }
        if (upPressed) {
            player.moveUp();
        }
        if (downPressed) {
            player.moveDown();
        }
        if (leftPressed) {
            player.moveLeft();
        }
        if (rightPressed) {
            player.moveRight();
        }
    }

    protected void keyPressed(int keyCode) {
        if (onMenu == 0 && onDialog == false) {
            if (keyCode == midlet.getKeyCode("up")) {
                upPressed = true;
            }
            if (keyCode == midlet.getKeyCode("down")) {
                downPressed = true;
            }
            if (keyCode == midlet.getKeyCode("left")) {
                leftPressed = true;
            }
            if (keyCode == midlet.getKeyCode("right")) {
                rightPressed = true;
            }
            if (keyCode == midlet.getKeyCode("c")) {
                onMenu = 1;
            }
            this.keyCode = keyCode;
        } else {
            if (onMenu > 0) {
                if (keyCode == midlet.getKeyCode("x")) {
                    onMenu--;
                    player.resumeMovement();
                }
            }
            if (onDialog) {
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
        }
    }

    protected void keyReleased(int keyCode) {
        if (keyCode == midlet.getKeyCode("up")) {
            upPressed = false;
        }
        if (keyCode == midlet.getKeyCode("down")) {
            downPressed = false;
        }
        if (keyCode == midlet.getKeyCode("left")) {
            leftPressed = false;
        }
        if (keyCode == midlet.getKeyCode("right")) {
            rightPressed = false;
        }

        player.changeAnimation("Idle", player.getCurrentDirection());
    }

    public void destroy() {
        midlet.cleanupResources();
        System.out.println("");
    }
}