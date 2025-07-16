package ui.objects;

import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;
import tools.ImageDrawer;
import java.io.IOException;
import java.io.InputStream;

public class TilesetMap {
    private int[][] backgroundLayer;
    private int[][] foregroundLayer;

    private Image tileset;
    private int tileWidth;
    private int tileHeight;
    private int tilesPerRow;
    
    private int viewRadiusX = 6;
    private int viewRadiusY = 6;

    private int cameraX = 0;
    private int cameraY = 0;

    private ImageDrawer imageDrawer = new ImageDrawer();

    public TilesetMap(String csvPath, String tilesetPath, int tileWidth, int tileHeight) {
        this.tileWidth = tileWidth;
        this.tileHeight = tileHeight;

        // Загружаем tileset
        if (tilesetPath != null) {
            InputStream is = getClass().getResourceAsStream(tilesetPath);
            if (is != null) {
                try {
                    tileset = Image.createImage(is);
                    if (tileWidth > 0) {
                        tilesPerRow = tileset.getWidth() / tileWidth;
                    } else {
                        throw new IllegalArgumentException("tileWidth must be > 0");
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                System.out.println("Tileset image not found: " + tilesetPath);
            }
        }

        // Загружаем карту
        loadCSV(csvPath);
    }

    private void loadCSV(String path) {
        try {
            InputStream is = getClass().getResourceAsStream(path);
            byte[] data = new byte[is.available()];
            is.read(data);
            is.close();

            String csv = new String(data);
            String[] lines = split(csv.trim(), "\n");

            int height = lines.length;
            int width = split(lines[0], ",").length;

            backgroundLayer = new int[height][width];
            foregroundLayer = new int[height][width];

            for (int y = 0; y < height; y++) {
                String[] cols = split(lines[y], ",");
                for (int x = 0; x < width; x++) {
                    int id = Integer.parseInt(cols[x].trim());
                    backgroundLayer[y][x] = id;
                    foregroundLayer[y][x] = -1;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void generateCollisions(ui.objects.Player player, int[] solidIDs) {
        int height = backgroundLayer.length;
        int width = backgroundLayer[0].length;

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int id1 = backgroundLayer[y][x];
                int id2 = foregroundLayer[y][x];

                if ((id1 >= 0 && contains(solidIDs, id1)) ||
                    (id2 >= 0 && contains(solidIDs, id2))) {
                    player.addCollisionBox(x * tileWidth, y * tileHeight, tileWidth, tileHeight);
                }
            }
        }
    }

    private static boolean contains(int[] arr, int value) {
        for (int i = 0; i < arr.length; i++) {
            if (arr[i] == value) return true;
        }
        return false;
    }

    public void setCamera(int x, int y) {
        this.cameraX = x;
        this.cameraY = y;
    }

    public void drawBackground(Graphics g, int screenWidth, int screenHeight, int playerX, int playerY) {
        drawLayer(g, backgroundLayer, screenWidth, screenHeight, playerX, playerY);
    }

    public void drawForeground(Graphics g, int screenWidth, int screenHeight, int playerX, int playerY) {
        drawLayer(g, foregroundLayer, screenWidth, screenHeight, playerX, playerY);
    }

    public void draw(Graphics g, int screenWidth, int screenHeight, int playerX, int playerY) {
        drawLayer(g, backgroundLayer, screenWidth, screenHeight, playerX, playerY);
        drawLayer(g, foregroundLayer, screenWidth, screenHeight, playerX, playerY);
    }

    private void drawLayer(Graphics g, int[][] layer, int screenWidth, int screenHeight, int playerX, int playerY) {
        int mapHeight = layer.length;
        int mapWidth = layer[0].length;

        // Координаты игрока в тайлах
        int playerTileX = playerX / tileWidth;
        int playerTileY = playerY / tileHeight;

        // Радиус видимости в тайлах
        int radiusX = viewRadiusX;
        int radiusY = viewRadiusY;

        int startX = Math.max(0, playerTileX - radiusX);
        int endX = Math.min(mapWidth, playerTileX + radiusX + 1);

        int startY = Math.max(0, playerTileY - radiusY);
        int endY = Math.min(mapHeight, playerTileY + radiusY + 1);

        for (int y = startY; y < endY; y++) {
            for (int x = startX; x < endX; x++) {
                int tileId = layer[y][x];
                if (tileId < 0) continue;

                int sx = (tileId % tilesPerRow) * tileWidth;
                int sy = (tileId / tilesPerRow) * tileHeight;

                int dx = x * tileWidth - cameraX;
                int dy = y * tileHeight - cameraY;

                imageDrawer.drawImageRegion(g, tileset, sx, sy, tileWidth, tileHeight, dx, dy);
            }
        }
    }

    private String[] split(String s, String delimiter) {
        int count = 1;
        int idx = 0;
        while ((idx = s.indexOf(delimiter, idx)) >= 0) {
            count++;
            idx += delimiter.length();
        }

        String[] result = new String[count];
        int start = 0;
        int i = 0;
        int end;
        while ((end = s.indexOf(delimiter, start)) >= 0) {
            result[i++] = s.substring(start, end);
            start = end + delimiter.length();
        }
        result[i] = s.substring(start);
        return result;
    }

    public void setCameraByPlayer(int playerX, int playerY, int screenWidth, int screenHeight) {
        // Камера следует за игроком, но ограничена по краям карты
        int halfScreenWidth = screenWidth / 2;
        int halfScreenHeight = screenHeight / 2;

        int mapWidthPixels = backgroundLayer[0].length * tileWidth;
        int mapHeightPixels = backgroundLayer.length * tileHeight;

        cameraX = playerX - halfScreenWidth;
        cameraY = playerY - halfScreenHeight;

        // Ограничиваем камеру, чтобы не выйти за границы карты
        if (cameraX < 0) cameraX = 0;
        if (cameraY < 0) cameraY = 0;
        if (cameraX > mapWidthPixels - screenWidth - 120) cameraX = mapWidthPixels - screenWidth - 120;
        if (cameraY > mapHeightPixels - screenHeight) cameraY = mapHeightPixels - screenHeight;
    }

    public int getMapWidth() {
        return backgroundLayer[0].length * tileWidth;
    }

    public int getMapHeight() {
        return backgroundLayer.length * tileHeight;
    }
    
    public void setViewRadiusX(int x) { this.viewRadiusX = x; }
    public void setViewRadiusY(int y) { this.viewRadiusY = y; }
}
