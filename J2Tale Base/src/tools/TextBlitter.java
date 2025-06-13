package tools;

import javax.microedition.lcdui.*;
import java.io.*;
import java.util.*;

public class TextBlitter {
    private Hashtable fonts;
    private String currentFontName;
    private String currentFontColor;
    private Hashtable colorImages;
    private Random random;

    public TextBlitter() {
        fonts = new Hashtable();
        colorImages = new Hashtable();
        random = new Random();
    }

    public void loadFont(String fontName) throws IOException {
        loadFont(fontName, null);
    }

    public void loadFont(String fontName, String defaultColor) throws IOException {
        Hashtable fontMap = loadFontMap(fontName);
        Hashtable colorMap = loadFontColors(fontName);

        if (colorMap == null || colorMap.isEmpty()) {
            Image defaultImage = (defaultColor != null && defaultColor.length() > 0)
                    ? Image.createImage("/fonts/" + fontName + "_" + defaultColor + ".png")
                    : Image.createImage("/fonts/" + fontName + ".png");
            colorImages.put(fontName + "_" + "default", defaultImage);
            fonts.put(fontName, new Font(fontMap));
        } else {
            Enumeration keys = colorMap.keys();
            while (keys.hasMoreElements()) {
                String colorName = (String) keys.nextElement();
                String imageName = (String) colorMap.get(colorName);
                Image colorImage = Image.createImage("/fonts/" + imageName + ".png");
                colorImages.put(fontName + "_" + colorName, colorImage);
            }
            fonts.put(fontName, new Font(fontMap));
        }
    }

    private Hashtable loadFontColors(String fontName) throws IOException {
        Hashtable colorMap = new Hashtable();
        String colorFilePath = "/fonts/" + fontName + "_colors.txt";
        InputStream is = getClass().getResourceAsStream(colorFilePath);
        if (is == null) return null;

        Reader reader = new InputStreamReader(is);
        StringBuffer buffer = new StringBuffer();
        int ch;
        while ((ch = reader.read()) != -1) {
            buffer.append((char) ch);
        }
        reader.close();

        String[] lines = split(buffer.toString(), "\n");
        for (int i = 0; i < lines.length; i++) {
            String line = lines[i].trim();
            if (line.length() > 0) {
                String[] parts = split(line, "=");
                if (parts.length == 2) {
                    String colorName = parts[0].trim();
                    String imageName = parts[1].trim();
                    colorMap.put(colorName, imageName);
                }
            }
        }
        return colorMap;
    }

    private Hashtable loadFontMap(String fontName) throws IOException {
        String path = "/fonts/" + fontName + ".txt";
        Hashtable fontMap = new Hashtable();
        InputStream is = getClass().getResourceAsStream(path);
        if (is == null) throw new IOException("Font map file not found: " + path);

        InputStreamReader reader = new InputStreamReader(is, "UTF-8");
        StringBuffer buffer = new StringBuffer();
        int ch;
        while ((ch = reader.read()) != -1) {
            buffer.append((char) ch);
        }
        reader.close();

        String[] lines = split(buffer.toString(), "\n");
        for (int i = 0; i < lines.length; i++) {
            String line = lines[i].trim();
            if (line.length() > 0) {
                String[] parts = split(line, "=");
                if (parts.length == 2) {
                    if (parts[0].length() > 0) {
                        String character = parts[0];
                        if (character.equals("|")) {
                            character = " ";
                        }
                        String[] values = split(parts[1], ",");
                        if (values.length == 5) {
                            try {
                                int x = Integer.parseInt(values[0].trim());
                                int y = Integer.parseInt(values[1].trim());
                                int width = Integer.parseInt(values[2].trim());
                                int height = Integer.parseInt(values[3].trim());
                                int shift = Integer.parseInt(values[4].trim());
                                fontMap.put(character, new Glyph(x, y, width, height, shift));
                            } catch (NumberFormatException e) {
                                System.err.println("Error parsing font map line: " + line);
                            }
                        } else {
                            System.err.println("Invalid font map line: " + line);
                        }
                    } else {
                        System.err.println("Invalid font map line: " + line);
                    }
                } else {
                    System.err.println("Invalid font map line: " + line);
                }
            }
        }
        return fontMap;
    }

    public void setFont(String fontName, String color) {
        currentFontName = fontName;
        currentFontColor = color;
    }

    private Font getFont() {
        return (Font) fonts.get(currentFontName);
    }

    private Image getColorImage() {
        if (currentFontName == null) {
            return null;
        }

        if (currentFontColor == null || currentFontColor.length() == 0) {
            return (Image) colorImages.get(currentFontName + "_" + "default");
        } else {
            return (Image) colorImages.get(currentFontName + "_" + currentFontColor);
        }
    }

     // Убрали вызов Font.drawString отсюда, чтобы можно было правильно обрабатывать команды
    public void drawString(Graphics g, String text, int x, int y) {
        Font font = getFont();
        Image colorImage = getColorImage();

        if (font != null && colorImage != null) {
            // Здесь вызываем новый метод, который умеет обрабатывать команды и эффект тряски
            font.drawStringWithCommands(g, text, x, y, colorImage, this, text.length());
        }
    }

    public void drawTypingText(Graphics g, String text, int x, int y, int displayedChars) {
        Font font = getFont();
        Image colorImage = getColorImage();

        if (font != null && colorImage != null) {
            // Вызываем новый метод, чтобы обрабатывать команды и эффект тряски во время печати
            font.drawStringWithCommands(g, text, x, y, colorImage, this, displayedChars);
        }
    }

    public void setColor(String colorName) {
        currentFontColor = colorName;
    }

    public int stringWidth(String text) {
        Font font = getFont();
        if (font != null) {
            return font.stringWidth(text);
        }
        return 0;
    }

    private class Font {
        private Hashtable glyphs;

        public Font(Hashtable glyphs) {
            this.glyphs = glyphs;
        }

        // Добавлено:  Этот метод будет обрабатывать команды, тряску и ограничение по количеству символов
        public void drawStringWithCommands(Graphics g, String text, int x, int y, Image image, TextBlitter textBlitter, int displayedChars) {
            Vector lines = splitLines(text, "\n");
            int currentY = y;
            String currentColor = textBlitter.currentFontColor;
            boolean shake = false;
            int charIndex = 0;

            for (int i = 0; i < lines.size(); i++) {
                String line = (String) lines.elementAt(i);
                int currentX = x;

                for (int j = 0; j < line.length(); j++) {
                    String character = String.valueOf(line.charAt(j));

                    // Проверка, не превышает ли текущий индекс лимит напечатанных символов
                    if (charIndex >= displayedChars) {
                        return; // Если превышает, просто выходим из метода, не рисуем дальше
                    }

                    if (character.equals("<")) {
                        int endIndex = line.indexOf(">", j);
                        if (endIndex != -1) {
                            String command = line.substring(j + 1, endIndex);
                            String[] parts = split(command, ":");
                            if (parts.length > 0) {
                                String commandName = parts[0].trim();

                                if (commandName.equals("color")) {
                                    if (parts.length > 1) {
                                        currentColor = parts[1].trim();
                                    }
                                } else if (commandName.equals("shake")) {
                                    if (parts.length > 1) {
                                        String shakeValue = parts[1].trim();
                                        if (shakeValue.equals("on")) {
                                            shake = true;
                                        } else if (shakeValue.equals("off")) {
                                            shake = false;
                                        }
                                    }
                                }
                            }
                            j = endIndex;
                            // Команды не увеличивают charIndex, так как они не отображаются
                        }
                    } else {
                        // Если это обычный символ, увеличиваем charIndex
                        Glyph glyph = (Glyph) glyphs.get(character);
                        if (glyph != null) {
                            Image colorImage;
                            if (currentColor != null && currentColor.length() > 0) {
                                colorImage = (Image) textBlitter.colorImages.get(textBlitter.currentFontName + "_" + currentColor);
                                if (colorImage == null) {
                                    colorImage = (Image) textBlitter.colorImages.get(textBlitter.currentFontName + "_" + "default"); // fallback
                                }
                            } else {
                                colorImage = (Image) textBlitter.colorImages.get(textBlitter.currentFontName + "_" + "default");
                            }

                            if (colorImage != null) {
                                int shakeX = 0;
                                int shakeY = 0;
                                if (shake) {
                                    shakeX = textBlitter.random.nextInt(2) - 1;
                                    shakeY = textBlitter.random.nextInt(2) - 1;
                                }

                                g.setClip(currentX + shakeX, currentY + shakeY, glyph.width, glyph.height);
                                g.drawImage(colorImage, (currentX + shakeX) - glyph.x, (currentY + shakeY) - glyph.y, Graphics.TOP | Graphics.LEFT);
                            }
                            currentX += glyph.shift;
                        }
                        charIndex++; // Увеличиваем charIndex только когда рисуем символ
                    }
                }
                currentY += getLineHeight();
                currentColor = textBlitter.currentFontColor;
            }
            textBlitter.currentFontColor = currentColor;
        }

        public int stringWidth(String text) {
            int width = 0;
            for (int i = 0; i < text.length(); i++) {
                String character = String.valueOf(text.charAt(i));

                if (character.equals("<")) {
                    int endIndex = text.indexOf(">", i);
                    if (endIndex != -1) {
                        i = endIndex;
                        continue;
                    }
                }

                Glyph glyph = (Glyph) glyphs.get(character);
                if (glyph != null) {
                    width += glyph.shift;
                } else {
                    width += 5;
                }
            }
            return width;
        }

        private int getLineHeight() {
            if (glyphs != null && !glyphs.isEmpty()) {
                Enumeration e = glyphs.elements();
                if (e.hasMoreElements()) {
                    Glyph glyph = (Glyph) e.nextElement();
                    return glyph.height;
                }
            }
            return 10;
        }

        private Vector splitLines(String text, String delimiter) {
            Vector lines = new Vector();
            if (delimiter == null || delimiter.length() == 0) {
                lines.addElement(text);
                return lines;
            }

            int start = 0;
            int end = text.indexOf(delimiter);
            while (end != -1) {
                lines.addElement(text.substring(start, end));
                start = end + delimiter.length();
                end = text.indexOf(delimiter, start);
            }
            lines.addElement(text.substring(start));
            return lines;
        }
    }

    private class Glyph {
        int x, y, width, height, shift;

        public Glyph(int x, int y, int width, int height, int shift) {
            this.x = x;
            this.y = y;
            this.width = width;
            this.height = height;
            this.shift = shift;
        }

        public int getHeight() {
            return height;
        }
    }

    private String[] split(String str, String delimiter) {
        Vector parts = new Vector();
        int start = 0;
        int end = str.indexOf(delimiter);
        while (end != -1) {
            parts.addElement(str.substring(start, end));
            start = end + delimiter.length();
            end = str.indexOf(delimiter, start);
        }
        parts.addElement(str.substring(start));
        String[] result = new String[parts.size()];
        for (int i = 0; i < parts.size(); i++) {
            result[i] = (String) parts.elementAt(i);
        }
        return result;
    }
}