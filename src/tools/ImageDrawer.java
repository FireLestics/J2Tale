package tools;

import javax.microedition.lcdui.*;
import java.io.IOException;
import java.io.InputStream;

public class ImageDrawer {
    private Image image;
    private int spawnX;
    private int spawnY;
    private int hotspotX;
    private int hotspotY;

    public void drawImage(Graphics g, String imagePath, int spawnX, int spawnY, int hotspotX, int hotspotY) {
        try {
            InputStream is = getClass().getResourceAsStream(imagePath);
            if (is == null) {
                System.err.println("Resource not found: " + imagePath);
                return;
            }
            this.image = Image.createImage(is);
            is.close();

            this.spawnX = spawnX;
            this.spawnY = spawnY;
            this.hotspotX = hotspotX;
            this.hotspotY = hotspotY;
            if (image != null) {
                g.drawImage(image, spawnX - hotspotX, spawnY - hotspotY, Graphics.TOP | Graphics.LEFT);
            }
        } catch (IOException e) {
            System.err.println("Error loading image: " + imagePath);
            e.printStackTrace();
        }
    }

    // 🔧 Добавь этот метод для отрисовки региона (тайла из tileset)
    public void drawImageRegion(Graphics g, Image source, int srcX, int srcY, int width, int height, int destX, int destY) {
        g.setClip(destX, destY, width, height); // Ограничиваем область вывода
        g.drawImage(source, destX - srcX, destY - srcY, Graphics.TOP | Graphics.LEFT);
        g.setClip(0, 0, 240, 320); // Восстанавливаем Clip (можно сделать динамически, если экран больше)
    }
}
