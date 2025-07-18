package j2tale_base.objects;

import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;
import java.io.InputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Vector;
import j2tale_base.tools.ImageDrawer;
import j2tale_base.objects.Player;

public class TilesetMap {
    private int[][] backgroundLayer;
    private int tileWidth, tileHeight;
    private int tilesPerRow;

    private Image tilesetAndMapImage;
    private int tilesetRows;
    private int mapRows;

    private int viewRadiusX = 6, viewRadiusY = 6;
    private int cameraX = 0, cameraY = 0;

    private ImageDrawer imageDrawer = new ImageDrawer();

    /**
     * Конструктор для загрузки из одного изображения (тайлсет + карта)
     * @param path путь к png с тайлсетом сверху и картой снизу
     * @param tileWidth ширина тайла
     * @param tileHeight высота тайла
     * @param tilesetRows количество строк тайлов в тайлсете сверху
     */
    public TilesetMap(String imagePath, int tileWidth, int tileHeight) {
        this.tileWidth = tileWidth;
        this.tileHeight = tileHeight;
        loadTilesetAndMapImage(imagePath);
    }

    /**
     * Конструктор для загрузки из CSV карты и отдельного тайлсета
     * @param csvPath путь к CSV с индексами тайлов
     * @param tilesetPath путь к изображению тайлсета
     * @param tileWidth ширина тайла
     * @param tileHeight высота тайла
     */
    public TilesetMap(String csvPath, String tilesetPath, int tileWidth, int tileHeight) {
        if (csvPath == null || tilesetPath == null) {
            throw new IllegalArgumentException("CSV path and tileset path must not be null.");
        }

        this.tileWidth = tileWidth;
        this.tileHeight = tileHeight;

        loadTilesetImage(tilesetPath);
        loadCSVMap(csvPath);
    }

    private void loadTilesetAndMapImage(String path) {
        try {
            System.out.println("Attempting to load image from: " + path);
            InputStream is = getClass().getResourceAsStream(path);
            if (is == null) {
                System.out.println("Image not found: " + path);
                return;
            }

            tilesetAndMapImage = Image.createImage(is);

            int imageWidth = tilesetAndMapImage.getWidth();
            int imageHeight = tilesetAndMapImage.getHeight();

            tilesPerRow = imageWidth / tileWidth;

            int totalRows = imageHeight / tileHeight;

            // Теперь считаем, что вся картинка — карта, тайлсета сверху нет
            tilesetRows = 0;
            mapRows = totalRows;

            if (mapRows <= 0) {
                System.out.println("Image too small or invalid format");
                return;
            }

            backgroundLayer = new int[mapRows][tilesPerRow];

            // Просто присваиваем каждому тайлу карты уникальный индекс (линейный номер по порядку)
            for (int y = 0; y < mapRows; y++) {
                for (int x = 0; x < tilesPerRow; x++) {
                    // Индекс в линейном виде, чтобы потом знать координаты тайла
                    backgroundLayer[y][x] = y * tilesPerRow + x;
                }
            }

        } catch (Exception e) {
            System.out.println("tilesPerRow = " + tilesPerRow);
            System.out.println("mapRows = " + mapRows);
            System.out.println("Failed to load image: " + e);
            e.printStackTrace();
        }
    }

    private void loadTilesetImage(String path) {
        try {
            System.out.println("Trying to load tileset image from: " + path);
            InputStream is = getClass().getResourceAsStream(path);
            if (is == null) {
                System.out.println("Tileset image not found: " + path);
                return;
            }
            tilesetAndMapImage = Image.createImage(is);
            tilesPerRow = tilesetAndMapImage.getWidth() / tileWidth;
            System.out.println("Tileset loaded successfully: " + tilesPerRow + " tiles per row");
        } catch (Exception e) {
            System.out.println("Failed to load tileset image: " + e);
            e.printStackTrace();
        }
    }

    private void loadCSVMap(String path) {
        try {
            InputStream is = getClass().getResourceAsStream(path);
            if (is == null) {
                System.out.println("CSV file not found: " + path);
                return;
            }
            String raw = readInputStream(is);
            Vector linesVec = new Vector();
            int start = 0;
            for (int i = 0; i < raw.length(); i++) {
                if (raw.charAt(i) == '\n' || raw.charAt(i) == '\r') {
                    if (start < i) {
                        linesVec.addElement(raw.substring(start, i));
                    }
                    // Пропускаем последовательные переводы строк
                    while (i < raw.length() && (raw.charAt(i) == '\n' || raw.charAt(i) == '\r')) i++;
                    start = i;
                    i--;
                }
            }
            if (start < raw.length()) {
                linesVec.addElement(raw.substring(start));
            }

            int rows = linesVec.size();
            if (rows == 0) return;

            // Предполагаем, что все строки одинаковой длины
            String firstLine = (String) linesVec.elementAt(0);
            String[] firstTiles = splitCSVLine(firstLine);
            int cols = firstTiles.length;

            backgroundLayer = new int[rows][cols];
            mapRows = rows;

            for (int y = 0; y < rows; y++) {
                String line = (String) linesVec.elementAt(y);
                String[] tiles = splitCSVLine(line);
                for (int x = 0; x < cols && x < tiles.length; x++) {
                    try {
                        backgroundLayer[y][x] = Integer.parseInt(tiles[x]);
                    } catch (NumberFormatException e) {
                        backgroundLayer[y][x] = -1;
                    }
                }
            }

        } catch (Exception e) {
            System.out.println("Failed to load CSV: " + e.getMessage());
        }
    }

    private String[] splitCSVLine(String line) {
        // Простой разбор по запятой, без кавычек и экранирования
        Vector vals = new Vector();
        int start = 0;
        for (int i = 0; i < line.length(); i++) {
            if (line.charAt(i) == ',') {
                vals.addElement(line.substring(start, i));
                start = i + 1;
            }
        }
        if (start <= line.length()) {
            vals.addElement(line.substring(start));
        }

        String[] result = new String[vals.size()];
        for (int i = 0; i < vals.size(); i++) {
            result[i] = (String) vals.elementAt(i);
        }
        return result;
    }

    private String readInputStream(InputStream is) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int read;
        while ((read = is.read(buffer)) != -1) {
            baos.write(buffer, 0, read);
        }
        return new String(baos.toByteArray(), "UTF-8");
    }

    private int[] getTilePixels(Image img, int tileX, int tileY) {
        int[] pixels = new int[tileWidth * tileHeight];
        img.getRGB(pixels, 0, tileWidth, tileX * tileWidth, tileY * tileHeight, tileWidth, tileHeight);
        return pixels;
    }

    private boolean pixelsEqual(int[] a, int[] b) {
        if (a.length != b.length) return false;
        int tolerance = 5; // допустимая разница по цвету

        for (int i = 0; i < a.length; i++) {
            int argbA = a[i];
            int argbB = b[i];

            int rA = (argbA >> 16) & 0xFF;
            int gA = (argbA >> 8) & 0xFF;
            int bA = argbA & 0xFF;

            int rB = (argbB >> 16) & 0xFF;
            int gB = (argbB >> 8) & 0xFF;
            int bB = argbB & 0xFF;

            if (Math.abs(rA - rB) > tolerance ||
                Math.abs(gA - gB) > tolerance ||
                Math.abs(bA - bB) > tolerance) {
                return false;
            }
        }
        return true;
    }

    public void draw(Graphics g, int screenWidth, int screenHeight, int playerX, int playerY) {
        if (backgroundLayer == null || tilesetAndMapImage == null) return;

        int mapHeight = backgroundLayer.length;
        int mapWidth = backgroundLayer[0].length;

        int playerTileX = playerX / tileWidth;
        int playerTileY = playerY / tileHeight;

        int startX = playerTileX - viewRadiusX;
        int endX = playerTileX + viewRadiusX + 1;
        int startY = playerTileY - viewRadiusY;
        int endY = playerTileY + viewRadiusY + 1;

        if (startX < 0) startX = 0;
        if (startY < 0) startY = 0;
        if (endX > mapWidth) endX = mapWidth;
        if (endY > mapHeight) endY = mapHeight;

        for (int y = startY; y < endY; y++) {
            for (int x = startX; x < endX; x++) {
                int tileId = backgroundLayer[y][x];
                if (tileId < 0) continue;

                int sx = (tileId % tilesPerRow) * tileWidth;
                int sy = (tileId / tilesPerRow) * tileHeight;

                int dx = x * tileWidth - cameraX;
                int dy = y * tileHeight - cameraY;

                imageDrawer.drawImageRegion(g, tilesetAndMapImage, sx, sy, tileWidth, tileHeight, dx, dy);
            }
        }
    }
    
    public void generateCollisions(Player player, int[] solidIDs) {
        if (backgroundLayer == null || solidIDs == null) {
            System.out.println("Collision generation skipped due to null backgroundLayer or solidIDs");
            return;
        }

        for (int y = 0; y < backgroundLayer.length; y++) {
            for (int x = 0; x < backgroundLayer[0].length; x++) {
                int id = backgroundLayer[y][x];
                if (id >= 0 && contains(solidIDs, id)) {
                    player.addCollisionBox(x * tileWidth, y * tileHeight, tileWidth, tileHeight);
                }
            }
        }
    }

    // Вспомогательный метод для проверки наличия элемента в массиве
    private boolean contains(int[] arr, int val) {
        for (int i = 0; i < arr.length; i++) {
            if (arr[i] == val) {
                return true;
            }
        }
        return false;
    }

    public void setCamera(int x, int y) {
        this.cameraX = x;
        this.cameraY = y;
    }

    public void setCameraByPlayer(int playerX, int playerY, int screenWidth, int screenHeight) {
        int halfW = screenWidth / 2;
        int halfH = screenHeight / 2;

        int mapW = getMapWidth();
        int mapH = getMapHeight();

        cameraX = playerX - halfW;
        cameraY = playerY - halfH;

        if (cameraX < 0) cameraX = 0;
        if (cameraY < 0) cameraY = 0;
        if (cameraX > mapW - screenWidth) cameraX = mapW - screenWidth;
        if (cameraY > mapH - screenHeight) cameraY = mapH - screenHeight;
    }

    public int getMapWidth() {
        return (backgroundLayer != null && backgroundLayer.length > 0)
                ? backgroundLayer[0].length * tileWidth : 0;
    }

    public int getMapHeight() {
        return (backgroundLayer != null)
                ? backgroundLayer.length * tileHeight : 0;
    }

    public void setViewRadiusX(int x) {
        viewRadiusX = x;
    }

    public void setViewRadiusY(int y) {
        viewRadiusY = y;
    }
}
