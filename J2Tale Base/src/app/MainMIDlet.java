package app;

import javax.microedition.midlet.*;
import javax.microedition.lcdui.*;
import tools.SimpleMIDIPlayer;
import ui.AbstractCanvas;
import ui.room_start;

public class MainMIDlet extends MIDlet {

    private SimpleMIDIPlayer player;
    private Display display;
    private AbstractCanvas currentCanvas;

    public MainMIDlet() {
    }

    public void startApp() {
        display = Display.getDisplay(this);
        switchCanvas(new room_start(this));
		player = new SimpleMIDIPlayer();
    }

    public void pauseApp() {
    }

    public void destroyApp(boolean unconditional) {
        if (currentCanvas != null) {
			currentCanvas.destroy();
			currentCanvas = null;
		}
		stopAllMIDI();
    }

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
	
	public void playMIDI(String name, int replay) {
		player.loadBackgroundMusic("midi/" + name + ".mid", replay);
		player.playBackgroundMusic();
	}
	
	public void stopAllMIDI() {
		System.out.println("Clean");
		player.stopAll();
	}
}
