package tools;

import javax.microedition.lcdui.*;
import java.io.*;
import java.util.*;

public class TextBlitter {
    private Hashtable fonts = new Hashtable();
    private String currentFontName;
    private String currentFontColor;
    private Hashtable colorImages = new Hashtable();
    private Random random = new Random();

    public void loadFont(String fontName) throws IOException {
        loadFont(fontName, null);
    }

    public void loadFont(String fontName, String defaultColor) throws IOException {
        Hashtable fontMap = loadFontMap(fontName);
        Hashtable colorMap = loadFontColors(fontName);

        if (colorMap == null || colorMap.isEmpty()) {
            Image defaultImage;
            if (defaultColor != null && defaultColor.length() > 0) {
                defaultImage = Image.createImage("/fonts/" + fontName + "_" + defaultColor + ".png");
            } else {
                defaultImage = Image.createImage("/fonts/" + fontName + ".png");
            }
            colorImages.put(fontName + "_default", defaultImage);
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
        InputStream is = getClass().getResourceAsStream("/fonts/" + fontName + "_colors.txt");
        if (is == null) return null;

        StringBuffer buffer = new StringBuffer();
        int ch;
        while ((ch = is.read()) != -1) {
            buffer.append((char) ch);
        }
        is.close();

        Hashtable map = new Hashtable();
        String[] lines = split(buffer.toString(), "\n");
        for (int i = 0; i < lines.length; i++) {
            String line = lines[i].trim();
            if (line.length() > 0) {
                String[] parts = split(line, "=");
                if (parts.length == 2) {
                    map.put(parts[0].trim(), parts[1].trim());
                }
            }
        }
        return map;
    }

    private Hashtable loadFontMap(String fontName) throws IOException {
        InputStream is = getClass().getResourceAsStream("/fonts/" + fontName + ".txt");
        if (is == null) throw new IOException("Font map not found");

        InputStreamReader reader = new InputStreamReader(is, "UTF-8");
        StringBuffer buffer = new StringBuffer();
        int ch;
        while ((ch = reader.read()) != -1) {
            buffer.append((char) ch);
        }
        reader.close();

        Hashtable map = new Hashtable();
        String[] lines = split(buffer.toString(), "\n");
        for (int i = 0; i < lines.length; i++) {
            String line = lines[i].trim();
            if (line.length() == 0) continue;

            String[] parts = split(line, "=");
            if (parts.length != 2 || parts[0].length() == 0) continue;

            String key = parts[0].equals("|") ? " " : parts[0];
            String[] values = split(parts[1], ",");
            if (values.length != 5) continue;

            try {
                int x = Integer.parseInt(values[0].trim());
                int y = Integer.parseInt(values[1].trim());
                int w = Integer.parseInt(values[2].trim());
                int h = Integer.parseInt(values[3].trim());
                int s = Integer.parseInt(values[4].trim());
                map.put(key, new Glyph(x, y, w, h, s));
            } catch (Exception e) {
                // Игнорируем ошибочные строки
            }
        }
        return map;
    }

    public void setFont(String name, String color) {
        currentFontName = name;
        currentFontColor = color;
    }

    private Font getFont() {
        return (Font) fonts.get(currentFontName);
    }
    
    public String getColor() {
        return currentFontColor;
    }

    private Image getColorImage() {
        String key = currentFontName + "_" + (currentFontColor != null && currentFontColor.length() > 0 ? currentFontColor : "default");
        return (Image) colorImages.get(key);
    }

    public void drawString(Graphics g, String text, int x, int y) {
        Font f = getFont();
        Image img = getColorImage();
        if (f != null && img != null) {
            f.draw(g, text, x, y, img, this, text.length());
        }
    }

    public void drawTypingText(Graphics g, String text, int x, int y, int shownChars) {
        Font f = getFont();
        Image img = getColorImage();
        if (f != null && img != null) {
            f.draw(g, text, x, y, img, this, shownChars);
        }
    }

    public void setColor(String colorName) {
        currentFontColor = colorName;
    }

    public int stringWidth(String text) {
        Font f = getFont();
        return f != null ? f.width(text) : 0;
    }

    private static String[] split(String s, String delim) {
        Vector list = new Vector();
        int i = 0, j;
        while ((j = s.indexOf(delim, i)) >= 0) {
            list.addElement(s.substring(i, j));
            i = j + delim.length();
        }
        list.addElement(s.substring(i));
        String[] arr = new String[list.size()];
        for (i = 0; i < arr.length; i++) {
            arr[i] = (String) list.elementAt(i);
        }
        return arr;
    }

    private static class Glyph {
        int x, y, w, h, s;

        Glyph(int x, int y, int w, int h, int s) {
            this.x = x;
            this.y = y;
            this.w = w;
            this.h = h;
            this.s = s;
        }
    }

    private class Font {
        private final Hashtable glyphs;

        Font(Hashtable glyphs) {
            this.glyphs = glyphs;
        }

        void draw(Graphics g, String txt, int x, int y, Image img, TextBlitter tb, int limit) {
            Vector lines = splitLines(txt, "\n");
            int cy = y;
            String color = tb.currentFontColor;
            boolean shake = false;
            boolean shakeSymmetric = false;
            int shakeX = 1;
            int shakeY = 1;
            int idx = 0;

            for (int i = 0; i < lines.size(); i++) {
                String line = (String) lines.elementAt(i);
                int cx = x;

                for (int j = 0; j < line.length(); j++) {
                    if (idx >= limit) return;

                    char ch = line.charAt(j);
                    if (ch == '<') {
                        int e = line.indexOf(">", j);
                        if (e > j) {
                            String cmd = line.substring(j + 1, e);
                            String[] p = split(cmd, ":");
                            if (p.length > 1) {
                                if ("color".equals(p[0])) {
                                    color = p[1];
                                } else if ("shake".equals(p[0])) {
                                    if ("off".equals(p[1])) {
                                        shake = false;
                                    } else {
                                        String[] args = split(p[1], ",");
                                        if (args.length >= 2) {
                                            try {
                                                shakeX = Integer.parseInt(args[0].trim());
                                                shakeY = Integer.parseInt(args[1].trim());
                                                shakeSymmetric = (args.length >= 3) && "±".equals(args[2].trim());
                                                shake = true;
                                            } catch (Exception ex) {
                                                // ошибка парсинга — отключаем shake
                                                shake = false;
                                            }
                                        }
                                    }
                                }
                            }
                            j = e;
                            continue;
                        }
                    }

                    Glyph glyph = (Glyph) glyphs.get(String.valueOf(ch));
                    if (glyph != null) {
                        Image useImg = (Image) tb.colorImages.get(tb.currentFontName + "_" + (color != null ? color : "default"));
                        if (useImg == null) useImg = (Image) tb.colorImages.get(tb.currentFontName + "_default");

                        int sx = 0;
                        int sy = 0;
                        if (shake) {
                            if (shakeSymmetric) {
                                sx = tb.random.nextInt(shakeX * 2 + 1) - shakeX;
                                sy = tb.random.nextInt(shakeY * 2 + 1) - shakeY;
                            } else {
                                sx = tb.random.nextInt(shakeX + 1);
                                sy = tb.random.nextInt(shakeY + 1);
                            }
                        }

                        g.setClip(cx + sx, cy + sy, glyph.w, glyph.h);
                        g.drawImage(useImg, (cx + sx) - glyph.x, (cy + sy) - glyph.y, Graphics.TOP | Graphics.LEFT);
                        cx += glyph.s;
                    }
                    idx++;
                }

                cy += lineHeight();
                color = tb.currentFontColor;
            }
        }

        int width(String txt) {
            int w = 0;
            for (int i = 0; i < txt.length(); i++) {
                char ch = txt.charAt(i);
                if (ch == '<') {
                    int e = txt.indexOf(">", i);
                    if (e > i) {
                        i = e;
                        continue;
                    }
                }
                Glyph g = (Glyph) glyphs.get(String.valueOf(ch));
                w += (g != null) ? g.s : 4;
            }
            return w;
        }

        int lineHeight() {
            if (!glyphs.isEmpty()) {
                Enumeration e = glyphs.elements();
                if (e.hasMoreElements()) {
                    Glyph g = (Glyph) e.nextElement();
                    return g.h;
                }
            }
            return 10;
        }

        Vector splitLines(String txt, String delim) {
            Vector lines = new Vector();
            int i = 0, j;
            while ((j = txt.indexOf(delim, i)) >= 0) {
                lines.addElement(txt.substring(i, j));
                i = j + delim.length();
            }
            lines.addElement(txt.substring(i));
            return lines;
        }
    }
}
