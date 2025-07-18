package j2tale_base.scenes;

import javax.microedition.lcdui.*;
import java.io.IOException;
import j2tale_base.Midlet;
import j2tale_base.tools.ImageDrawer;
import j2tale_base.tools.TextBlitter;

public class scene_example extends AbstractCanvas {
    private ImageDrawer imageDrawer;
    private TextBlitter textBlitter;
    
    private int width;
    private int height;

    public scene_example(Midlet midlet) {
        super(midlet);
        setFullScreenMode(true);
        imageDrawer = new ImageDrawer();
        textBlitter = new TextBlitter();
        try {
            textBlitter.loadFont("fnt_maintext", "default");
            textBlitter.loadFont("fnt_main", "default");
        } catch (IOException e) {}
    }
	
    protected void update() {}

    protected void paint(Graphics g) {
        this.width = getWidth();
        this.height = getHeight();
        
	g.setColor(0x000000);
        g.fillRect(0, 0, getWidth(), getHeight() + 20);
		
	// textBlitter.setFont("fnt_maintext", "white");
	// textBlitter.drawTypingText(g, "", 0, 0, 999);
	// textBlitter.drawString(g, "", 0, 0);
        // imageDrawer.drawImage(g, "", 0, 0, 0, 0);
        
        // String команды:
        // <color:цвет>
        // <shake:off> или <shake:смещение по x,смещение по y,(опционально)±>
    }

    protected void keyPressed(int keyCode) {
        // midlet.switchCanvas(new SettingsCanvas(midlet));
    }

    public void destroy() {
        midlet.cleanupResources();
        System.out.println("");
    }
}