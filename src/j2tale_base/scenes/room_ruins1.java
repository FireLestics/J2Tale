package j2tale_base.scenes;

import javax.microedition.lcdui.*;
import j2tale_base.Midlet;
import j2tale_base.objects.TilesetMap;
import j2tale_base.tools.ImageDrawer;

public class room_ruins1 extends RoomBase {
    private Midlet midlet;
    private ImageDrawer imageDrawer;
    
    private String Room_name = "";
    private String spawnPos = "null";
    
    private int RenderRadiusX = (width / 20);
    private int RenderRadiusY = (height / 20) + 2;
    
    public room_ruins1(SceneManager manager, String spawnID) {
        super(manager, manager.getMidlet());
        this.midlet = manager.getMidlet();
        imageDrawer = new ImageDrawer();
        
        midlet.cleanupResources();
        
        loadMapLayers("/maps/6_Ruins_Etrance", "/images/tiles/ruins.png", 20, 20, 4);
        for (int i = 0; i < mapLayers.length; i++) {
            if (mapLayers[i] == null) { continue; }
            mapLayers[i].setViewRadiusX(RenderRadiusX);
            mapLayers[i].setViewRadiusY(RenderRadiusY);
            mapLayers[i].setCamera(player.getCameraX(), player.getCameraY());
        }
        
        collisions = new TilesetMap("/maps/6_Ruins_Etrance_collisions.csv", "/images/tiles/ruins.png", 20, 20);
        
        // Создание колизии карты
        player.clearCollisionBoxes();
        collisions.generateCollisions(player, new int[]{0});
        player.setMapSize(320, 480);
        
        setRoomId(6);
        setRoomName("Ruins - Etrance");
        addSpawnPoint("start", new int[]{140, 198});
        addSpawnPoint("pos1", new int[]{140, 425});
        addSpawnPoint("pos2", new int[]{140, 104});
        RoomManager.registerRoom(getRoomId(), this);
        
        spawnPlayerAt(spawnID);
        
        createFormedColission("form1", 220, 180, 0);
        createFormedColission("form1", 240, 160, 0);
        createFormedColission("form1", 240, 100, 3);
        createFormedColission("form1", 220, 400, 0);
        createFormedColission("form1", 40, 400, 1);
        createFormedColission("form1", 60, 120, 0);
        createFormedColission("form1", 200, 120, 1);
        createFormedColission("form1", 200, 140, 2);
        createFormedColission("form1", 180, 160, 2);
        createFormedColission("form1", 80, 160, 3);
        createFormedColission("form1", 60, 140, 3);
        createFormedColission("form1", 40, 180, 1);
        createFormedColission("form1", 20, 160, 1);
        createFormedColission("form1", 20, 100, 2);
        createFormedColission("form1", 100, 420, 1);
        createFormedColission("form1", 160, 420, 0);
    }
    
    private void createFormedColission(String form, int x, int y, int rotation) {
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
    
    public void update() {
        updatePlayerMovement();
        player.update();
        player.updateCamera();
    }

    public void paint(Graphics g) {
        g.setColor(0x000000);
        g.fillRect(0, 0, getWidth(), getHeight());
        drawCommon(g);
    }

    protected void keyPressed(int keyCode) {
        // manager.setScene(new GameScene(manager));
        handleKeyDown(keyCode);
    }
    
    protected void keyReleased(int keyCode) {
        handleKeyUp(keyCode);
    }
}
