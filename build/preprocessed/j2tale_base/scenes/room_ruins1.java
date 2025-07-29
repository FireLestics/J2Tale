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
        
        player.addCollisionBox(220, 199, 20, 1);
        player.addCollisionBox(221, 198, 1, 1);
        player.addCollisionBox(222, 197, 1, 1);
        player.addCollisionBox(223, 196, 1, 1);
        player.addCollisionBox(224, 195, 1, 1);
        player.addCollisionBox(225, 194, 1, 1);
        player.addCollisionBox(226, 193, 1, 1);
        player.addCollisionBox(227, 192, 1, 1);
        player.addCollisionBox(228, 191, 1, 1);
        player.addCollisionBox(229, 190, 1, 1);
        player.addCollisionBox(230, 189, 1, 1);
        player.addCollisionBox(231, 188, 1, 1);
        player.addCollisionBox(232, 187, 1, 1);
        player.addCollisionBox(233, 186, 1, 1);
        player.addCollisionBox(234, 185, 1, 1);
        player.addCollisionBox(235, 184, 1, 1);
        player.addCollisionBox(236, 183, 1, 1);
        player.addCollisionBox(237, 182, 1, 1);
        player.addCollisionBox(238, 181, 1, 1);
        player.addCollisionBox(239, 180, 1, 1);
        player.addCollisionBox(240, 179, 1, 1);
        player.addCollisionBox(241, 178, 1, 1);
        player.addCollisionBox(242, 177, 1, 1);
        player.addCollisionBox(243, 176, 1, 1);
        player.addCollisionBox(244, 175, 1, 1);
        player.addCollisionBox(245, 174, 1, 1);
        player.addCollisionBox(246, 173, 1, 1);
        player.addCollisionBox(247, 172, 1, 1);
        player.addCollisionBox(248, 171, 1, 1);
        player.addCollisionBox(249, 170, 1, 1);
        player.addCollisionBox(250, 169, 1, 1);
        player.addCollisionBox(251, 168, 1, 1);
        player.addCollisionBox(252, 167, 1, 1);
        player.addCollisionBox(253, 166, 1, 1);
        player.addCollisionBox(254, 165, 1, 1);
        player.addCollisionBox(255, 164, 1, 1);
        player.addCollisionBox(256, 163, 1, 1);
        player.addCollisionBox(257, 162, 1, 1);
        player.addCollisionBox(258, 161, 1, 1);
        player.addCollisionBox(259, 160, 1, 1);
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
