package ui;

import app.MainMIDlet;
import javax.microedition.lcdui.*;
import ui.objects.TilesetMap;
import tools.ImageDrawer;

public class room_02_flowey extends RoomBase {
    private ImageDrawer imageDrawer;
    
    // Устанавливаем радиус локации
    private int RenderRadiusX = (width / 20);
    private int RenderRadiusY = (height / 20) + 2;
    
    public room_02_flowey(MainMIDlet midlet) {
        super(midlet);
        imageDrawer = new ImageDrawer();
        
        // Импорт локаций
        loadMapLayers("/maps/ruins19", "/images/tiles/ruins.png", 20, 20, 3);
        for (int i = 0; i < mapLayers.length; i++) {
            if (mapLayers[i] == null) { continue; }
            mapLayers[i].setViewRadiusX(RenderRadiusX);
            mapLayers[i].setViewRadiusY(RenderRadiusY);
            mapLayers[i].setCamera(player.getCameraX(), player.getCameraY());
        }
        
        // Импорт карты колизий
        collisions = new TilesetMap("/maps/ruins19_collisions.csv", null, 20, 20);
        
        // Создание колизии карты
        player.clearCollisionBoxes();
        collisions.generateCollisions(player, new int[]{0});
        player.setMapSize(mapLayers[0].getMapWidth(), mapLayers[0].getMapHeight());
    }
    
    // Функция обновления игры
    protected void update() {
        updatePlayerMovement();
        player.update();
        player.updateCamera();
    }
    
    // Графика
    protected void paint(Graphics g) {
        drawCommon(g);
    }
    
    // Нажата ли кнопка
    protected void keyPressed(int keyCode) {
        handleKeyDown(keyCode);
    }
    
    // Отпущена ли кнопка
    protected void keyReleased(int keyCode) {
        handleKeyUp(keyCode);
    }

// Дополнительные фнкции (закоментированно)
    // Игрок
        // Основные функции 
            // player.addCollisionBox(140, 160, 48, 36);
            // player.addTriggerZone(100, 20, 32, 32, "id", "action");
            // player.addNPC(200, 20, 16, 32, "name");
            // player.clearCollisionBoxes();
            // player.changeAnimation("Idle", "up");
            // player.changeCharacter("Sans");
            // player.pauseMovement();
            // player.resumeMovement();
            // player.moveUp();
            // player.moveDown();
            // player.moveLeft();
            // player.moveRight();
            // player.draw(g, imageDrawer);
            // player.drawDebug(g);

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

        // Сеттеры
            // player.setScreenSize(240, 320);
            // player.setMapSize(640, 640);
            // player.setCollisionRect(4, 8, 24, 24);
            // player.setSpeed(20);
            // player.setAnimationSpeed(20);
            // player.setDebugMode(true);
            // player.setNoClip(true);

    // Карта
        // Основные функции 
            // loadMapLayers("/maps/ruins19", "/images/tiles/ruins.png", 20, 20, 3);
            // drawCommon(g);
            // startDialog(new String[]{"Привет, это стол!"}, false);
            // imageDrawer.drawImage(g, "/images/Players/Frisk/Idle/down/00.png", 20 - player.getCameraX(), 20 - player.getCameraY(), 0, 0);

        // Сеттеры
            // mapLayers[0].setViewRadiusX(RenderRadiusX);
            // mapLayers[0].setViewRadiusY(RenderRadiusY);
            // mapLayers[0].setCamera(player.getCameraX(), player.getCameraY());
}