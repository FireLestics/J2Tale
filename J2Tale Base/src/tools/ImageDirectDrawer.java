package tools;

// ImageDirectDrawer.java
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;
import javax.microedition.lcdui.game.Sprite;
import java.io.IOException;
import javax.microedition.io.Connector;
import javax.microedition.io.HttpConnection;
import java.io.InputStream;

public class ImageDirectDrawer {

    public static void drawImage(Graphics g, String imagePath, int x, int y, int angle) {
        Image img = null;
        try {
            img = loadImage(imagePath);
        } catch (IOException e) {
            System.err.println("Ошибка загрузки изображения: " + e.getMessage());
            g.drawString("Error loading image", x, y, Graphics.TOP | Graphics.LEFT);
            return;
        }

        if (img != null) {
            drawImageWithRotation(g, img, x, y, angle);
        }
    }

    private static void drawImageWithRotation(Graphics g, Image img, int x, int y, int angle) {
        int width = img.getWidth();
        int height = img.getHeight();

        // Корректировка позиции в зависимости от угла поворота.  Важно!
        int adjustedX = x;
        int adjustedY = y;

        switch (angle) {
            case 90:
                adjustedX = x + height;
                break;
            case 180:
                adjustedX = x + width;
                adjustedY = y + height;
                break;
            case 270:
                adjustedY = y + width;
                break;
        }


        // Если есть поворот
        if (angle != 0) {
            Sprite sprite = new Sprite(img);
            sprite.setRefPixelPosition(width / 2, height / 2); // Центр вращения
            sprite.setTransform(getTransformConstant(angle));

            // Центр вращения
            int centerX = adjustedX;
            int centerY = adjustedY;

            sprite.setPosition(centerX, centerY);

            sprite.paint(g);
        } else {
            g.drawImage(img, x, y, Graphics.TOP | Graphics.LEFT);
        }
    }

    private static int getTransformConstant(int angle) {
        angle = angle % 360;
        switch (angle) {
            case 0:   return Sprite.TRANS_NONE;
            case 90:  return Sprite.TRANS_ROT90;
            case 180: return Sprite.TRANS_ROT180;
            case 270: return Sprite.TRANS_ROT270;
            default:
                System.err.println("Warning: Only 0, 90, 180, and 270 degree rotations are supported in Java ME.");
                return Sprite.TRANS_NONE;
        }
    }

    private static Image loadImage(String imagePath) throws IOException {
        InputStream is = null;
        try {
            if (imagePath.startsWith("http://") || imagePath.startsWith("https://")) {
                HttpConnection connection = (HttpConnection) Connector.open(imagePath);
                is = connection.openInputStream();
            } else {
                is = ImageDirectDrawer.class.getResourceAsStream(imagePath);
                if (is == null) {
                    throw new IOException("Image resource not found: " + imagePath);
                }
            }
            return Image.createImage(is);
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    System.err.println("Error closing input stream: " + e.getMessage());
                }
            }
        }
    }
}