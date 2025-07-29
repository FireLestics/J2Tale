package j2tale_base.objects;

import javax.microedition.lcdui.*;
import java.io.IOException;
import java.io.InputStream;
import java.util.Vector;
import j2tale_base.tools.ImageDrawer;

class FrameInfo {
    String path;
    int width;
    int height;

    public FrameInfo(String path, int width, int height) {
        this.path = path;
        this.width = width;
        this.height = height;
    }
}

class CollisionBox {
    int x, y, width, height;

    CollisionBox(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    boolean intersects(int px, int py, int pw, int ph) {
        return (px < x + width &&
                px + pw > x &&
                py < y + height &&
                py + ph > y);
    }
}

class TriggerZone {
    int x, y, width, height;
    String id;
    String action;

    TriggerZone(int x, int y, int width, int height, String id, String action) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.id = id;
        this.action = action;
    }

    boolean isPlayerInside(int px, int py, int pw, int ph) {
        return (px < x + width &&
                px + pw > x &&
                py < y + height &&
                py + ph > y);
    }
}

class NPC {
    int x, y, width, height;
    String name;
    int radius;

    NPC(int x, int y, int width, int height, String name) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.name = name;
        this.radius = 48; // стандартный радиус
    }

    boolean isNearPlayer(int px, int py, int radius) {
        int centerX = x + width / 2;
        int centerY = y + height / 2;
        int dx = centerX - px;
        int dy = centerY - py;
        return dx * dx + dy * dy <= radius * radius;
    }
}

public class Player {

    private String characterName;
    private String currentAnimation = "Idle";
    private String currentDirection = "down";
    private String lastTriggerAction = "";

    private int animationSpeed;
    private int animationCounter = 0;
    private int currentFrame = 0;

    private Vector frames = new Vector();
    private Vector collisionBoxes = new Vector();
    private Vector triggerZones = new Vector();
    private Vector npcs = new Vector();

    private int frameWidth;
    private int frameHeight;

    private int x, y;
    private int speed;

    private boolean movementPaused = false;
    private boolean debugMode = false;
    private boolean noClip = false;

    // Камера
    private int cameraX, cameraY;
    private int screenWidth = 240;
    private int screenHeight = 320;
    private int mapWidth = 1024;
    private int mapHeight = 1024;

    // Хитбокс игрока
    private int collisionX, collisionY, collisionW, collisionH;

    public Player(String characterName, int startX, int startY, int speed, int animationSpeed) {
        this.characterName = characterName;
        this.x = startX;
        this.y = startY;
        this.speed = speed;
        this.animationSpeed = animationSpeed;

        loadAnimation(currentAnimation, currentDirection);
        updateFrameSize();
    }

    public void moveUp() {
        if (!movementPaused) {
            int newY = y - speed;
            if (!isCollidingAt(x, newY)) {
                y = newY;
            }
            currentDirection = "up";
            changeAnimation("Walk", currentDirection);
        }
    }

    public void moveDown() {
        if (!movementPaused) {
            int newY = y + speed;
            if (!isCollidingAt(x, newY)) {
                y = newY;
            }
            currentDirection = "down";
            changeAnimation("Walk", currentDirection);
        }
    }

    public void moveLeft() {
        if (!movementPaused) {
            int newX = x - speed;
            if (!isCollidingAt(newX, y)) {
                x = newX;
            }
            currentDirection = "left";
            changeAnimation("Walk", currentDirection);
        }
    }

    public void moveRight() {
        if (!movementPaused) {
            int newX = x + speed;
            if (!isCollidingAt(newX, y)) {
                x = newX;
            }
            currentDirection = "right";
            changeAnimation("Walk", currentDirection);
        }
    }

    public void changeAnimation(String animationName, String direction) {
        if (!currentAnimation.equals(animationName) || !currentDirection.equals(direction)) {
            currentAnimation = animationName;
            currentDirection = direction;
            loadAnimation(animationName, direction);
            currentFrame = 0;
        }
    }

    private void loadAnimation(String animationName, String direction) {
        frames.removeAllElements();
        String basePath = "/images/players/" + characterName + "/" + animationName + "/" + direction + "/";

        int frameIndex = 0;
        while (true) {
            String framePath = basePath + (frameIndex < 10 ? "0" : "") + frameIndex + ".png";

            InputStream is = getClass().getResourceAsStream(framePath);
            if (is == null) break;

            try {
                Image img = Image.createImage(is);
                frames.addElement(new FrameInfo(framePath, img.getWidth(), img.getHeight()));
                is.close();
                frameIndex++;
            } catch (IOException e) {
                System.out.println("Error loading frame: " + framePath);
                break;
            }
        }

        if (frames.isEmpty()) {
            System.out.println("Warning: No frames loaded for " + animationName + ", " + direction);
        }

        updateFrameSize();
    }

    public void update() {
        if (frames.isEmpty()) return;

        animationCounter++;
        if (animationCounter >= (60 / animationSpeed)) {
            animationCounter = 0;
            currentFrame = (currentFrame + 1) % frames.size();
            updateFrameSize();
        }

        if (currentAnimation.equals("Walk") && speed == 0) {
            changeAnimation("Idle", currentDirection);
        }
        
        checkTriggers();
        checkNpcInteraction();
    }

    private void updateFrameSize() {
        if (!frames.isEmpty()) {
            if (currentFrame >= frames.size()) {
                currentFrame = 0;
            }
            FrameInfo frame = (FrameInfo) frames.elementAt(currentFrame);
            frameWidth = frame.width;
            frameHeight = frame.height;
        }
    }
    
    public void pauseMovement() {
        movementPaused = true;
    }
    
    public void resumeMovement() {
        movementPaused = false;
    }

    // ─── Коллизии ─────────────────────────────

    public void setCollisionRect(int offsetX, int offsetY, int width, int height) {
        this.collisionX = offsetX;
        this.collisionY = offsetY;
        this.collisionW = width;
        this.collisionH = height;
    }

    public boolean checkCollision(int otherX, int otherY, int otherW, int otherH) {
        int playerLeft = x + collisionX;
        int playerTop = y + collisionY;
        int playerRight = playerLeft + collisionW;
        int playerBottom = playerTop + collisionH;

        int otherRight = otherX + otherW;
        int otherBottom = otherY + otherH;

         return !(playerRight <= otherX || playerLeft >= otherRight ||
                 playerBottom <= otherY || playerTop >= otherBottom);
    }

    public void addCollisionBox(int x, int y, int w, int h) {
        collisionBoxes.addElement(new CollisionBox(x, y, w, h));
    }

    public void clearCollisionBoxes() {
        collisionBoxes.removeAllElements();
    }

    public boolean isCollidingAt(int testX, int testY) {
        if (noClip == false) {
            int testLeft = testX + collisionX;
            int testTop = testY + collisionY;
            int testRight = testLeft + collisionW;
            int testBottom = testTop + collisionH;

            for (int i = 0; i < collisionBoxes.size(); i++) {
                CollisionBox box = (CollisionBox) collisionBoxes.elementAt(i);
                if (box.intersects(testLeft, testTop, collisionW, collisionH)) {
                    return true;
                }
            }
            return false;
        } else {
            return false;
        }
    }

    public void autoGenerateCollisionBox() {
        int offsetX = frameWidth / 8;
        int offsetY = (int)(frameHeight * 0.6);
        int width = frameWidth - offsetX * 2;
        int height = frameHeight - offsetY;

        setCollisionRect(offsetX, offsetY, width, height);
    }
    
    // ─── Триггеры ────────────────────────────────
    public void addTriggerZone(int x, int y, int width, int height, String id, String action) {
        triggerZones.addElement(new TriggerZone(x, y, width, height, id, action));
    }

    public void checkTriggers() {
        if (noClip == false) {
            int playerLeft = x + collisionX;
            int playerTop = y + collisionY;
            int playerRight = playerLeft + collisionW;
            int playerBottom = playerTop + collisionH;

            for (int i = 0; i < triggerZones.size(); i++) {
                TriggerZone zone = (TriggerZone) triggerZones.elementAt(i);
                if (zone.isPlayerInside(playerLeft, playerTop, collisionW, collisionH)) { lastTriggerAction = zone.action; }
            }
        } else {}
    }

    // ─── NPC ────────────────────────────────
    
    public void addNPC(int x, int y, int width, int height, String name) {
        npcs.addElement(new NPC(x, y, width, height, name));
    }

    public void checkNpcInteraction() {
        if (noClip == false) {
            for (int i = 0; i < npcs.size(); i++) {
                NPC npc = (NPC) npcs.elementAt(i);
                if (npc.isNearPlayer(x + frameWidth / 2, y + frameHeight / 2, 32)) {}
            }
        } else {}
    }

    // ─── Камера ────────────────────────────────

    public void updateCamera() {
//        cameraX = x + frameWidth / 2 - screenWidth / 2;
//        cameraY = y + frameHeight / 2 - screenHeight / 2;
        cameraX = x - screenWidth / 2;
        cameraY = y - screenHeight / 2;

        if (cameraX < 0) cameraX = 0;
        if (cameraY < 0) cameraY = 0;
        if (cameraX > mapWidth - screenWidth) cameraX = mapWidth - screenWidth;
        if (cameraY > mapHeight - screenHeight) cameraY = mapHeight - screenHeight;
    }

    public void setScreenSize(int width, int height) {
        this.screenWidth = width;
        this.screenHeight = height;
    }

    public void setMapSize(int width, int height) {
        this.mapWidth = width;
        this.mapHeight = height;
    }

    // ─── Отрисовка ─────────────────────────────

    public void draw(Graphics g, ImageDrawer imageDrawer) {
        String framePath = getCurrentFrameName();
        if (framePath != null) {
            imageDrawer.drawImage(g, framePath, x - cameraX, y - cameraY, frameWidth / 2, frameHeight);
        }
    }

    public void drawDebug(Graphics g, boolean player, boolean walls, boolean triggers, boolean Rnpcs) {
        if (!debugMode) return;
        
        if (player) { // Отрисовка колизий игрока
            g.setColor(255, 0, 0);
            g.drawRect(x + collisionX - cameraX, y + collisionY - cameraY, collisionW, collisionH);
        }
        
        if (walls) { // Отрисовка колизий
            g.setColor(0, 0, 255);
            for (int i = 0; i < collisionBoxes.size(); i++) {
                CollisionBox box = (CollisionBox) collisionBoxes.elementAt(i);
                g.drawRect(box.x - cameraX, box.y - cameraY, box.width, box.height);
            }   
        }
        
        if (triggers) { // Отрисовка триггер-зон
            g.setColor(255, 255, 0); // Жёлтый
            for (int i = 0; i < triggerZones.size(); i++) {
                TriggerZone zone = (TriggerZone) triggerZones.elementAt(i);
                g.drawRect(zone.x - cameraX, zone.y - cameraY, zone.width, zone.height);
            }
        }
        
        if (Rnpcs) { // Отрисовка радиуса NPC
            g.setColor(0, 255, 255); // Голубой
            for (int i = 0; i < npcs.size(); i++) {
                NPC npc = (NPC) npcs.elementAt(i);
                int centerX = npc.x + npc.width / 2 - cameraX;
                int centerY = npc.y + npc.height / 2 - cameraY;
                int radius = 32;
                g.drawArc(centerX - radius, centerY - radius, radius * 2, radius * 2, 0, 360);
            }
        }
    }

    // ─── Геттеры и сеттеры ─────────────────────

    public String getCurrentFrameName() {
        if (frames.isEmpty()) return null;

        if (currentFrame >= frames.size()) {
            currentFrame = 0;
        }

        FrameInfo frame = (FrameInfo) frames.elementAt(currentFrame);
        return frame.path;
    }
    
    public boolean isInsideTrigger(String triggerId) {
        if (noClip == false) {
            for (int i = 0; i < triggerZones.size(); i++) {
                TriggerZone tz = (TriggerZone) triggerZones.elementAt(i);
                if (tz.id.equals(triggerId) &&
                    checkCollision(tz.x, tz.y, tz.width, tz.height)) {
                    return true;
                }
            }
        } else {}
        return false;
    }
    
    public boolean isNearNPC(String npcName) {
        if (noClip == false) {
            for (int i = 0; i < npcs.size(); i++) {
                NPC npc = (NPC) npcs.elementAt(i);
                if (npc.name.equals(npcName)) {
                    int dx = (x + collisionX + collisionW / 2) - (npc.x + npc.width / 2);
                    int dy = (y + collisionY + collisionH / 2) - (npc.y + npc.height / 2);
                    int distanceSquared = dx * dx + dy * dy;
                    return distanceSquared <= npc.radius * npc.radius;
                }
            }
        } else {}
        return false;
    }
    
    public boolean isFacingNPC(String npcName) {
        if (noClip == false) {
            for (int i = 0; i < npcs.size(); i++) {
                NPC npc = (NPC) npcs.elementAt(i);
                if (npc.name.equals(npcName)) {
                    int npcCenterX = npc.x + npc.width / 2;
                    int npcCenterY = npc.y + npc.height / 2;
                    int playerCenterX = x + collisionX + collisionW / 2;
                    int playerCenterY = y + collisionY + collisionH / 2;

                    if (currentDirection.equals("up")) {
                        return npcCenterY < playerCenterY &&
                               Math.abs(npcCenterX - playerCenterX) < collisionW;
                    } else if (currentDirection.equals("down")) {
                        return npcCenterY > playerCenterY &&
                               Math.abs(npcCenterX - playerCenterX) < collisionW;
                    } else if (currentDirection.equals("left")) {
                        return npcCenterX < playerCenterX &&
                               Math.abs(npcCenterY - playerCenterY) < collisionH;
                    } else if (currentDirection.equals("right")) {
                        return npcCenterX > playerCenterX &&
                               Math.abs(npcCenterY - playerCenterY) < collisionH;
                    }
                }
            }
        } else {}
        return false;
    }
    
    public void setX(int X) { this.x = X; }
    public void setY(int Y) { this.y = Y; }
    public int getX() { return x; }
    public int getY() { return y; }
    public int getFrameWidth() { return frameWidth; }
    public int getFrameHeight() { return frameHeight; }
    public String getCurrentDirection() { return currentDirection; }
    public void setAnimationSpeed(int animationSpeed) { this.animationSpeed = animationSpeed; }
    public void setSpeed(int speed) { this.speed = speed; }
    public void setDebugMode(boolean enabled) { this.debugMode = enabled; }
    public void setNoClip(boolean enabled) { this.noClip = enabled; }
    public int getCameraX() { return cameraX; }
    public int getCameraY() { return cameraY; }
    public String getLastTriggerAction() { return lastTriggerAction; }
    public int getColissionsCount() { return collisionBoxes.size(); }
    public int getTriggersCount() { return triggerZones.size(); }
    public int getNpcsCount() { return npcs.size(); }
}