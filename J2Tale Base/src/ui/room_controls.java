package ui;

import javax.microedition.lcdui.*;
import java.io.IOException;
import app.MainMIDlet;

public class room_controls extends AbstractCanvas {

    public room_controls(MainMIDlet midlet) {
        super(midlet);
    }
	
	private void update() {
	}

    protected void paint(Graphics g) {
		update();
		
		g.setColor(0x000000);
        g.fillRect(0, 0, getWidth(), getHeight());
    }

    protected void keyPressed(int keyCode) {
        // midlet.switchCanvas(new SettingsCanvas(midlet));
    }

    public void destroy() {
      System.out.println("");
    }
}