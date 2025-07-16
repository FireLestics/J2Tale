package ui;

import javax.microedition.lcdui.*;
import java.io.IOException;
import java.io.InputStream;
import java.util.Vector;
import app.MainMIDlet;
import tools.TextBlitter;
import tools.ImageDrawer;
import ui.objects.Player;
import ui.objects.Menu;
import ui.objects.TilesetMap;

public class room_overworld extends AbstractCanvas {
    private Player player;
    private Menu menu;
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

    public room_overworld(MainMIDlet midlet) {
        super(midlet);
        setFullScreenMode(true);
        
        midlet.loadAllKeyCode();
        imageDrawer = new ImageDrawer();
        menu = new Menu();
        
        textBlitter = new TextBlitter();
        try {
            textBlitter.loadFont("fnt_maintext", "default");
        } catch (IOException e) {}
        
        player = new Player("Frisk", 60, 20, 3, 10);
        
        player.clearCollisionBoxes();
        map1 = new TilesetMap("/maps/ruins19_map.csv", "/images/tiles/ruins.png", 20, 20);
        map2 = new TilesetMap("/maps/ruins19_map2.csv", "/images/tiles/ruins.png", 20, 20);
        map3 = new TilesetMap("/maps/ruins19_map3.csv", "/images/tiles/ruins.png", 20, 20);
        collisions = new TilesetMap("/maps/ruins19_collisions.csv", null, 20, 20);
        collisions.generateCollisions(player, new int[]{0});
        
        player.addTriggerZone(260, 160, 20, 20, "dropdown_1", "dropdown_1");
        player.addTriggerZone(380, 160, 20, 20, "dropdown_2", "dropdown_2");
        player.addTriggerZone(500, 160, 20, 20, "dropdown_3", "dropdown_3");
        player.addTriggerZone(260, 280, 20, 20, "dropdown_4", "dropdown_4");
        player.addTriggerZone(380, 280, 20, 20, "dropdown_5", "dropdown_5");
        player.addTriggerZone(500, 280, 20, 20, "dropdown_6", "dropdown_6");
        // player.addNPC(200, 20, 16, 32, "Bob");
        
        player.setScreenSize(width, height);
        player.setMapSize(map1.getMapWidth(), map1.getMapHeight());
        
        player.setCollisionRect(-9, -8, 19, 8);
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
        if (player.isInsideTrigger("dropdown_2")) {
            player.pauseMovement();
            player.setNoClip(true);
            if (dropDownTimer >= 20) {
                this.dropDown = true;
            } else {
                this.dropDownTimer++;
            }
        }
        if (player.isInsideTrigger("dropdown_3")) {
            player.pauseMovement();
            player.setNoClip(true);
            if (dropDownTimer >= 20) {
                this.dropDown = true;
            } else {
                this.dropDownTimer++;
            }
        }
        if (player.isInsideTrigger("dropdown_4")) {
            player.pauseMovement();
            player.setNoClip(true);
            if (dropDownTimer >= 20) {
                this.dropDown = true;
            } else {
                this.dropDownTimer++;
            }
        }
        if (player.isInsideTrigger("dropdown_5")) {
            player.pauseMovement();
            player.setNoClip(true);
            if (dropDownTimer >= 20) {
                this.dropDown = true;
            } else {
                this.dropDownTimer++;
            }
        }
        if (player.isInsideTrigger("dropdown_6")) {
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
                player.changeAnimation("DropDown", player.getCurrentDirection());
                if (player.getY() >= 510) {
                    player.setNoClip(false);
                    player.resumeMovement();
                    this.dropDownTimer = 0;
                    this.dropDown = false;
                    player.changeAnimation("Idle", player.getCurrentDirection());
                }
            }
            if ("dropdown_2".equals(player.getLastTriggerAction())) {
                player.setY(player.getY() + 4);
                player.changeAnimation("DropDown", player.getCurrentDirection());
                if (player.getY() >= 510) {
                    player.setNoClip(false);
                    player.resumeMovement();
                    this.dropDownTimer = 0;
                    this.dropDown = false;
                    player.changeAnimation("Idle", player.getCurrentDirection());
                }
            }
            if ("dropdown_3".equals(player.getLastTriggerAction())) {
                player.setY(player.getY() + 4);
                player.changeAnimation("DropDown", player.getCurrentDirection());
                if (player.getY() >= 510) {
                    player.setNoClip(false);
                    player.resumeMovement();
                    this.dropDownTimer = 0;
                    this.dropDown = false;
                    player.changeAnimation("Idle", player.getCurrentDirection());
                }
            }
            if ("dropdown_4".equals(player.getLastTriggerAction())) {
                player.setY(player.getY() + 4);
                player.changeAnimation("DropDown", player.getCurrentDirection());
                if (player.getY() >= 650) {
                    player.setNoClip(false);
                    player.resumeMovement();
                    this.dropDownTimer = 0;
                    this.dropDown = false;
                    player.changeAnimation("Idle", player.getCurrentDirection());
                }
            }
            if ("dropdown_5".equals(player.getLastTriggerAction())) {
                player.setY(player.getY() + 4);
                player.changeAnimation("DropDown", player.getCurrentDirection());
                if (player.getY() >= 650) {
                    player.setNoClip(false);
                    player.resumeMovement();
                    this.dropDownTimer = 0;
                    this.dropDown = false;
                    player.changeAnimation("Idle", player.getCurrentDirection());
                }
            }
            if ("dropdown_6".equals(player.getLastTriggerAction())) {
                player.setY(player.getY() + 4);
                player.changeAnimation("DropDown", player.getCurrentDirection());
                if (player.getY() >= 650) {
                    player.setNoClip(false);
                    player.resumeMovement();
                    this.dropDownTimer = 0;
                    this.dropDown = false;
                    player.changeAnimation("Idle", player.getCurrentDirection());
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
        
        map1.draw(g, getWidth(), getHeight());
        map2.draw(g, getWidth(), getHeight());
        map3.draw(g, getWidth(), getHeight());
        player.draw(g, imageDrawer);
        player.drawDebug(g, true, false, true, false);
        
        if (onMenu >= 1) {
            menu.drawMenu_1(g, 5, 5, midlet.getStringData("playerName"), 1, 15, 20, 0);
            g.setClip(0, 0, getWidth(), getHeight());
            menu.drawMenu_2(g, 5, 58, 0, new String[]{"ITEM", "STAT" ,"CELL", "MAIL", "BAG"});
            g.setClip(0, 0, getWidth(), getHeight());
            player.pauseMovement();
        }
        if (onMenu == 2) {
            
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
        if (onMenu == 0) {
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
        } else {
            if (keyCode == midlet.getKeyCode("x")) {
                onMenu--;
                player.resumeMovement();
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