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

    public void drawBackground(Graphics g, int screenWidth, int screenHeight) {
        drawLayer(g, backgroundLayer, screenWidth, screenHeight);
    }

    public void drawForeground(Graphics g, int screenWidth, int screenHeight) {
        drawLayer(g, foregroundLayer, screenWidth, screenHeight);
    }

    public void draw(Graphics g, int screenWidth, int screenHeight) {
        drawLayer(g, backgroundLayer, screenWidth, screenHeight);
        drawLayer(g, foregroundLayer, screenWidth, screenHeight);
    }

    private void drawLayer(Graphics g, int[][] layer, int screen_width, int screen_height) {
        final int screenWidth = screen_width;
        final int screenHeight = screen_height;

        int mapHeight = layer.length;
        int mapWidth = layer[0].length;

        int startX = cameraX / tileWidth;
        int startY = cameraY / tileHeight;
        int endX = Math.min(startX + (screenWidth / tileWidth) + 2, mapWidth);
        int endY = Math.min(startY + (screenHeight / tileHeight) + 2, mapHeight);
        
        // System.out.println("TilesetMap: " + screenWidth + "/" + screenHeight + " - " + mapWidth + "/" + mapHeight + " - " + startX + "/" + startY + " - " + endX + "/" + endY);

        // Ограничим правую и нижнюю границу
        if (endX > mapWidth) endX = mapWidth;
        if (endY > mapHeight) endY = mapHeight;

        for (int y = startY; y < endY; y++) {
            if (y < 0 || y >= mapHeight) continue;

            for (int x = startX; x < endX; x++) {
                if (x < 0 || x >= mapWidth) continue;

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

    public int getMapWidth() {
        return backgroundLayer[0].length * tileWidth;
    }

    public int getMapHeight() {
        return backgroundLayer.length * tileHeight;
    }
}
