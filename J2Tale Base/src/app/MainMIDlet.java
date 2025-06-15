package app;

import javax.microedition.midlet.*;
import javax.microedition.lcdui.*;
import tools.SimpleMIDIPlayer;
import tools.GameSaveManager;
import ui.AbstractCanvas;
import ui.room_start;

public class MainMIDlet extends MIDlet {

    private SimpleMIDIPlayer player;
    private Display display;
    private AbstractCanvas currentCanvas;
	private GameSaveManager saveManager;
	private boolean onIntro = false;

    public MainMIDlet() {
		saveManager = new GameSaveManager();
		
        display = Display.getDisplay(this);
        switchCanvas(new room_start(this));
		player = new SimpleMIDIPlayer();
    }

    public void startApp() {
    }

    public void pauseApp() {
    }

    public void destroyApp(boolean unconditional) {
        if (currentCanvas != null) {
			currentCanvas.destroy();
			currentCanvas = null;
		}
		if (saveManager != null) {
            saveManager.close();
        }
		if (player != null) {
            stopAllMIDI();
        }
    }
	
	// Работа с фреймами
    public void switchCanvas(AbstractCanvas newCanvas) {
		display.setCurrent(newCanvas);

		if (currentCanvas != null) {
			currentCanvas.stop(); // Останавливаем поток старого Canvas
			currentCanvas.destroy();
			currentCanvas = null;
			System.gc();
		}

		currentCanvas = newCanvas;
		if (currentCanvas != null) {
			currentCanvas.start(); // Запускаем поток нового Canvas
		}
	}
	
	public void cleanupResources() {
		System.gc();
	}
	
	// Работа с MIDI
	public void playMIDI(String name, int replay, boolean onMusic) {
		if (onMusic == true) {
			player.loadBackgroundMusic("midi/" + name + ".mid", replay);
			player.playBackgroundMusic();
		}
	}
	
	public void stopAllMIDI() {
		player.stopAll();
	}
	
	// Работа с сохранениями
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
            System.out.println("Error: saveManager is null in getBooleanData!");
            return 0;
        }
	}
	
	public String getStringData(String key) {
		if (saveManager != null) {
			return saveManager.loadString(key, "");
        } else {
            System.out.println("Error: saveManager is null in getBooleanData!");
            return "";
        }
	}
	
	public boolean getBooleanData(String key) {
        if (saveManager != null) {
            boolean value = saveManager.loadBoolean(key, false);
            return value;
        } else {
            return false;
        }
	}
}
