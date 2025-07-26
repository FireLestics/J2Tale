package j2tale_base.scenes;

import javax.microedition.lcdui.*;
import j2tale_base.Midlet;
import j2tale_base.objects.TilesetMap;
import j2tale_base.tools.ImageDrawer;

public class room_area1 extends RoomBase {
    private Midlet midlet;
    private ImageDrawer imageDrawer;
    
    
    private String Room_name = "";
    private String spawnPos = "null";
    
    private int RenderRadiusX = (width / 20);
    private int RenderRadiusY = (height / 20) + 2;
    
    public room_area1(SceneManager manager, String spawnID) {
        super(manager, manager.getMidlet());
        this.midlet = manager.getMidlet();
        imageDrawer = new ImageDrawer();
        
        loadMapLayers("/maps/4_Ruins_StartingPoint.png", 20, 20);
        for (int i = 0; i < mapLayers.length; i++) {
            if (mapLayers[i] == null) { continue; }
            mapLayers[i].setViewRadiusX(RenderRadiusX);
            mapLayers[i].setViewRadiusY(RenderRadiusY);
            mapLayers[i].setCamera(player.getCameraX(), player.getCameraY());
        }
        
        collisions = new TilesetMap("/maps/4_Ruins_StartingPoint_collisions.csv", "/images/tiles/ruins.png", 20, 20);
        
        // Создание колизии карты
        player.clearCollisionBoxes();
        collisions.generateCollisions(player, new int[]{0});
        player.setMapSize(680, 260);
        
        setRoomId(4);
        setRoomName("Ruins - Starting Point");
        addSpawnPoint("start", new int[]{150, 143});
        addSpawnPoint("bed", new int[]{618, 170});
        RoomManager.registerRoom(getRoomId(), this);
        
        spawnPlayerAt(spawnID);
        
        player.addTriggerZone(600, 120, 40, 20, "next_room", "next_room");
    }
    
    public void update() {
        updatePlayerMovement();
        player.update();
        player.updateCamera();
        
        if (player.isInsideTrigger("next_room")) {
            manager.setScene(new room_area1_2(manager, "pos1"));
        }
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
