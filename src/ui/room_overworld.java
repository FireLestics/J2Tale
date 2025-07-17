package ui;

import app.MainMIDlet;
import javax.microedition.lcdui.*;
import ui.objects.TilesetMap;
import tools.ImageDrawer;

public class room_overworld extends RoomBase {
    private ImageDrawer imageDrawer;
    
    private int RenderRadiusX = (width / 20);
    private int RenderRadiusY = (height / 20) + 2;

    public room_overworld(MainMIDlet midlet) {
        super(midlet);
        imageDrawer = new ImageDrawer();
        
        loadMapLayers("/maps/ruins19", "/images/tiles/ruins.png", 20, 20, 3);
        for (int i = 0; i < mapLayers.length; i++) {
            if (mapLayers[i] == null) { continue; }
            mapLayers[i].setViewRadiusX(RenderRadiusX);
            mapLayers[i].setViewRadiusY(RenderRadiusY);
            mapLayers[i].setCamera(player.getCameraX(), player.getCameraY());
        }
        
        collisions = new TilesetMap("/maps/ruins19_collisions.csv", null, 20, 20);
        player.clearCollisionBoxes();
        
        collisions.generateCollisions(player, new int[]{0});
        player.setMapSize(mapLayers[0].getMapWidth(), mapLayers[0].getMapHeight());

        // Триггеры
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
        updatePlayerMovement();
        player.update();
        player.updateCamera();

        if (player.isInsideTrigger("dropdown_1")) {
            startDialog(new String[]{"Ты упал!", "Ха-ха!"}, false);
        }
    }

    protected void paint(Graphics g) {
        drawCommon(g); // вызов базовой отрисовки
    }
    
    protected void keyPressed(int keyCode) {
        if (onDialog == false) {
            if (keyCode == midlet.getKeyCode("z")) {
                if (player.isNearNPC("table") && player.isFacingNPC("table")) {
                    startDialog(new String[]{"Привет, это стол!"}, false);
                    keyCode = 0;
                }
            }
        }
        handleKeyDown(keyCode); // вызываем из RoomBase
    }

    protected void keyReleased(int keyCode) {
        handleKeyUp(keyCode); // вызываем из RoomBase
    }
}
