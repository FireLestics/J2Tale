package j2tale_base;

import javax.microedition.midlet.*;
import javax.microedition.lcdui.*;
import j2tale_base.tools.SimpleMIDIPlayer;
import j2tale_base.tools.GameSaveManager;
import j2tale_base.scenes.SceneManager;
import j2tale_base.scenes.room_start;

public class Midlet extends MIDlet {
    private SceneManager sceneManager;
    private SimpleMIDIPlayer player;
    private Display display;
    private GameSaveManager saveManager;
    private boolean onIntro = false;
    
    private int UP = 0;
    private int DOWN = 0;
    private int LEFT = 0;
    private int RIGHT = 0;
    private int Z = 0;
    private int X = 0;
    private int C = 0;
    private int ESC = 0;
    private int F4 = 0;
    private int LB = 0;
    private int RB = 0;
    private int BKSC = 0;

    public Midlet() {
        player = new SimpleMIDIPlayer();
        saveManager = new GameSaveManager();
        if (sceneManager == null) {
            sceneManager = new SceneManager(this);
            sceneManager.setScene(new room_start(sceneManager));
        }
    }

    public void startApp() {
    }

    public void pauseApp() {
    }

    public void destroyApp(boolean unconditional) {
        if (saveManager != null) {
            saveManager.close();
        }
        if (player != null) {
            stopAllMIDI();
        }
    }

    public void cleanupResources() {
        System.gc();
    }

    //Работа с управлением
    public void saveKeyCodes(int up, int down, int left, int right, int z, int x, int c, int esc, int f4, int lb, int rb, int bksc) {
        saveIntData("buttonKeyCode_up", up);
        saveIntData("buttonKeyCode_down", down);
        saveIntData("buttonKeyCode_left", left);
        saveIntData("buttonKeyCode_right", right);
        saveIntData("buttonKeyCode_z", z);
        saveIntData("buttonKeyCode_x", x);
        saveIntData("buttonKeyCode_c", c);
        saveIntData("buttonKeyCode_esc", esc);
        saveIntData("buttonKeyCode_f4", f4);
        saveIntData("buttonKeyCode_lb", lb);
        saveIntData("buttonKeyCode_rb", rb);
        saveIntData("buttonKeyCode_bksc", bksc);
    }

    public void loadAllKeyCode() {
        this.UP = getIntData("buttonKeyCode_up", -1);
        this.DOWN = getIntData("buttonKeyCode_down", -2);
        this.LEFT = getIntData("buttonKeyCode_left", -3);
        this.RIGHT = getIntData("buttonKeyCode_right", -4);
        this.Z = getIntData("buttonKeyCode_z", -5);
        this.X = getIntData("buttonKeyCode_x", -6);
        this.C = getIntData("buttonKeyCode_c", -7);
        this.ESC = getIntData("buttonKeyCode_esc", 55);
        this.F4 = getIntData("buttonKeyCode_f4", 57);
        this.LB = getIntData("buttonKeyCode_lb", -6);
        this.RB = getIntData("buttonKeyCode_rb", -7);
        this.BKSC = getIntData("buttonKeyCode_bksc", 8);
        System.out.println("Midlet: Коды кнопок загружены!");
    }
    
    public int getKeyCode(String key) {
        if ("up".equals(key)) {
            return UP;
        } else if ("down".equals(key)) {
            return DOWN;
        } else if ("left".equals(key)) {
            return LEFT;
        } else if ("right".equals(key)) {
            return RIGHT;
        } else if ("z".equals(key)) {
            return Z;
        } else if ("x".equals(key)) {
            return X;
        } else if ("c".equals(key)) {
            return C;
        } else if ("esc".equals(key)) {
            return ESC;
        } else if ("f4".equals(key)) {
            return F4;
        } else if ("lb".equals(key)) {
            return LB;
        } else if ("rb".equals(key)) {
            return RB;
        } else if ("bksc".equals(key)) {
            return BKSC;
        } else {
            return 0;
        }
    }

    // Работа с MIDI
    public void playMIDI(String name, int replay, boolean onMusic) {
        if (onMusic) {
            player.loadBackgroundMusic(name, replay);
            player.playBackgroundMusic();
        }
    }

    public void stopAllMIDI() {
        player.stopAll();
    }
    
    // Вычисления для устройств с маленьким экраном
    public boolean ifSmallScreen(int width, int height) {
        int dia = getScreenDia(width, height);
        if (dia < 130) {
            return true;
        }
        return false;
    }
    
    public int getScreenDia(int width, int height) {
        return ((width / 2) + (height / 2)) / 2;
    }

    // Геттеры и сеттеры
    public void saveIntData(String key, int value) {
        saveManager.saveInt(key, value);
    }

    public void saveStringData(String key, String value) {
        saveManager.saveString(key, value);
    }

    public void saveBooleanData(String key, boolean value) {
        saveManager.saveBoolean(key, value);
    }

    public int getIntData(String key) {
        if (saveManager != null) {
            return saveManager.loadInt(key, 0);
        } else {
            System.out.println("Error: saveManager is null in getIntData!");
            return 0;
        }
    }
    
    public int getIntData(String key, int defInt) {
        if (saveManager != null) {
            return saveManager.loadInt(key, defInt);
        } else {
            System.out.println("Error: saveManager is null in getIntData!");
            return 0;
        }
    }

    public String getStringData(String key) {
        if (saveManager != null) {
            return saveManager.loadString(key, "");
        } else {
            System.out.println("Error: saveManager is null in getStringData");
            return "";
        }
    }

    public boolean getBooleanData(String key) {
        if (saveManager != null) {
            boolean value = saveManager.loadBoolean(key, false);
            return value;
        } else {
            System.out.println("Error: saveManager is null in getBooleanData");
            return false;
        }
    }
}
