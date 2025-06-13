package ui;

import javax.microedition.lcdui.*;
import java.io.IOException;
import app.MainMIDlet;
import tools.TextBlitter;

public class room_overworld extends AbstractCanvas {
	private TextBlitter textBlitter;
	
	private String InfoText = "";

    public room_overworld(MainMIDlet midlet) {
        super(midlet);
        textBlitter = new TextBlitter();
		try {
            textBlitter.loadFont("fnt_maintext", "default");
            textBlitter.loadFont("fnt_main", "default");
        } catch (IOException e) {}
		
		this.InfoText = "<color:yellow>The overworld <color:white>is currently \n<color:red>NOT <color:white>ready!\n\n";
		this.InfoText = InfoText + "We need to <color:green>wait <color:white>for the \n<color:blue>source update <color:white>to add \n<color:yellow>Overworld<color:white>!";
    }
	
	private void update() {
	}

    protected void paint(Graphics g) {
		update();
		
		g.setColor(0x000000);
        g.fillRect(0, 0, getWidth(), getHeight());
		
		textBlitter.setFont("fnt_maintext", "white");
		textBlitter.drawString(g, InfoText, 5, 5);
    }

    protected void keyPressed(int keyCode) {
        // midlet.switchCanvas(new SettingsCanvas(midlet));
    }

    public void destroy() {
      System.out.println("");
    }
}