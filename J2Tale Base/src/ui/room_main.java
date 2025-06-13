package ui;

import javax.microedition.lcdui.*;
import java.io.IOException;
import java.util.Vector;
import app.MainMIDlet;
import tools.TextBlitter;

public class room_main extends AbstractCanvas {
	private TextBlitter textBlitter;
    private Vector menuItems;
    private Vector nameItems;
    private Vector settingsItems;
	
	private int width = getWidth();
	private int height = getHeight();
    
    private int frame = 0;
    private int dia = ((width / 2) + (height / 2)) / 2;
    private int mainSelIndex = 0;
    private int nameSelIndex = 0;
    private int settSelIndex = 0;
    private int BGColor = 0x000000;
    private int lastKeyCode;
    
    private int fixedPosX = (width / 2) - 78;
    private int fixedPosY = (height / 2) - 92;
    private int startPosX = 0;
    private int startPosY = 0;
	
	private int PressedKey = 0;
    
    private String instruction = "";
    private String playerName = "";
    private String TopBanner = "Is this name correct?";
    
    String LBText = "No";
    String RBText = "Yes";

    public room_main(MainMIDlet midlet) {
        super(midlet);
        textBlitter = new TextBlitter();
		try {
            textBlitter.loadFont("fnt_maintext", "default");
            textBlitter.loadFont("fnt_main", "default");
        } catch (IOException e) {}
		
		midlet.playMIDI("mus_menu01", -1);
		
		menuItems = new Vector();
        menuItems.addElement("Begin Game");
        menuItems.addElement("Settings");
        
        nameItems = new Vector();
        nameItems.addElement("A");
        nameItems.addElement("B");
        nameItems.addElement("C");
        nameItems.addElement("D");
        nameItems.addElement("E");
        nameItems.addElement("F");
        nameItems.addElement("G");
        nameItems.addElement("H");
        nameItems.addElement("I");
        nameItems.addElement("J");
        nameItems.addElement("K");
        nameItems.addElement("L");
        nameItems.addElement("M");
        nameItems.addElement("N");
        nameItems.addElement("O");
        nameItems.addElement("P");
        nameItems.addElement("Q");
        nameItems.addElement("R");
        nameItems.addElement("S");
        nameItems.addElement("T");
        nameItems.addElement("U");
        nameItems.addElement("V");
        nameItems.addElement("W");
        nameItems.addElement("X");
        nameItems.addElement("Y");
        nameItems.addElement("Z");
		
		settingsItems = new Vector();
        settingsItems.addElement("EXIT");
        settingsItems.addElement("CONTROL OPTIONS");
        settingsItems.addElement("CONTROL TEST");
        settingsItems.addElement("MUSIC:     [no work]");
        settingsItems.addElement("INTRO:     [no work]");
    }
	
	private void update() {
	}

    protected void paint(Graphics g) {
		update();
		
		g.setColor(BGColor);
        g.fillRect(0, 0, width + 20, height + 20);
        
        switch (frame) {
			case 0:
				midlet.cleanupResources();
				drawInstruction(g);
				break;
			case 1:
				midlet.cleanupResources();
				drawEnterName(g);
				break;
			case 2:
				midlet.cleanupResources();
				drawSelectName(g);
				break;
			case 3:
				midlet.cleanupResources();
				drawSettings(g);
				break;
			case 4:
				midlet.cleanupResources();
				drawLoadSave(g);
				break;
			case 5:
				midlet.cleanupResources();
				drawErrorScrn(g);
				break;
		}
		textBlitter.setFont("fnt_maintext", "red");
		textBlitter.drawString(g, "Key Pressed: " + PressedKey, 5, 5);
    }
	
	private void drawInstruction(Graphics g) {
		this.instruction = " --- Instruction ---\n\n";
		this.instruction = instruction + "[* or ENTER] - Confirm\n";
		this.instruction = instruction + "[0 or RB] - Cancel\n";
		this.instruction = instruction + "[# or LB] - Menu (In-game)\n";
		this.instruction = instruction + "[D-PAD] - Movement\n";
		this.instruction = instruction + "When HP is 0, you lose.";
		
		textBlitter.setFont("fnt_maintext", "gray");
		textBlitter.drawString(g, instruction, fixedPosX, fixedPosY);
		
		for (int i = 0; i < menuItems.size(); i++) {
			String item = (String) menuItems.elementAt(i);
			if (i == mainSelIndex) {
				textBlitter.setFont("fnt_maintext", "yellow");
			} else {
				textBlitter.setFont("fnt_maintext", "white");
			}
			textBlitter.drawString(g, item, fixedPosX, fixedPosY + 128 + (i * 16));
		}
	}
	
	private void drawEnterName(Graphics g) {
		String helpText = "[#] - Backspace";
		this.LBText = "Quit";
		String ENTERText = "ENTER";
		this.RBText = "Done";
		
		this.TopBanner = "Name the fallen human.";
		
		int helpTextWidth = textBlitter.stringWidth(helpText);
		int helpENTERWidth = textBlitter.stringWidth(ENTERText);
		int helpRBWidth = textBlitter.stringWidth(RBText);
		
		int TopBannerWidth = textBlitter.stringWidth(TopBanner);
		
		textBlitter.setFont("fnt_maintext", "white");
		textBlitter.drawString(g, TopBanner, (width / 2) - (TopBannerWidth / 2), 28);
		textBlitter.drawString(g, playerName, (width / 2) - 18, 48);
		
		textBlitter.drawString(g, "<color:gray>" + helpText, (width / 2) - (helpTextWidth / 2), height - 40);
		
		textBlitter.drawString(g, "<color:white>" + LBText, 10, height - 20);
		textBlitter.drawString(g, "<color:white>" + ENTERText, (width / 2) - (helpENTERWidth / 2), height - 20);
		textBlitter.drawString(g, "<color:white>" + RBText, width - 10 - helpRBWidth, height - 20);
		
		if (playerName.equals("GASTER")) {
			this.TopBanner = "";
			this.playerName = "";
			midlet.switchCanvas(new room_main(midlet));
		}

		// Все символы
		this.startPosY = fixedPosY + 70;
		this.startPosX = fixedPosX;
		
		for (int i = 0; i < nameItems.size(); i++) {
			String item = (String) nameItems.elementAt(i);
			int row = i / 7;      // Номер строки
			int col = i % 7;      // Номер столбца

			if (i == nameSelIndex) {
				textBlitter.setFont("fnt_maintext", "yellow");
			} else {
				textBlitter.setFont("fnt_maintext", "white");
			}

			int x = startPosX + (col * 24);  // Смещение по X в зависимости от столбца
			int y = startPosY + (row * 16); // Смещение по Y в зависимости от строки

			textBlitter.drawString(g, "<shake:on>" + item + "<shake:off>", x, y);
		}
	}
	
	private void drawSelectName(Graphics g) {
		textBlitter.setFont("fnt_maintext", "white");
            
		int helpRBWidth = textBlitter.stringWidth(RBText);
		int TopBannerWidth = textBlitter.stringWidth("Is this name correct?");
		
		this.TopBanner = "Is this name correct?";
		if (playerName.equals("AAAAAA")) {
			this.LBText = "No";
			this.RBText = "Yes";
			this.TopBanner = "Not very creative...?";
		} else if (playerName.equals("FLOWEY")) {
			this.TopBanner = "I already CHOSE\nthat name.";
			this.LBText = "Go back";
			this.RBText = "";
		} else if (playerName.equals("SANS")) {
			this.LBText = "Go back";
			this.RBText = "";
			this.TopBanner = "nope.";
		} else if (playerName.equals("TORIEL")) {
			this.LBText = "Go back";
			this.RBText = "";
			this.TopBanner = "I think you should\nthink of your own\nname, my child.";
		} else if (playerName.equals("PAPYRU")) {
			this.LBText = "No";
			this.RBText = "Yes";
			this.TopBanner = "I'LL ALLOW IT!!!!";
		} else if (playerName.equals("ASGORE")) {
			this.LBText = "Go back";
			this.RBText = "";
			this.TopBanner = "You cannot.";
		} else if (playerName.equals("ASRIEL")) {
			this.LBText = "Go back";
			this.RBText = "";
			this.TopBanner = "...";
		} else if (playerName.equals("UNDYNE")) {
			this.LBText = "Go back";
			this.RBText = "";
			this.TopBanner = "Get your OWN name!";
		} else if (playerName.equals("ALPHYS")) {
			this.LBText = "Go back";
			this.RBText = "";
			this.TopBanner = "D-don't do that.";
		} else if (playerName.equals("ALPHY")) {
			this.LBText = "No";
			this.RBText = "Yes";
			this.TopBanner = "Uh... OK?";
		} else if (playerName.equals("CATTY")) {
			this.LBText = "No";
			this.RBText = "Yes";
			this.TopBanner = "Bratty! Bratty!\nThat's MY name!";
		} else if (playerName.equals("BRATTY")) {
			this.LBText = "No";
			this.RBText = "Yes";
			this.TopBanner = "Like, OK I guess.";
		} else if (playerName.equals("METTA")) {
			this.LBText = "Go back";
			this.RBText = "";
			this.TopBanner = "Name it's <color:red>NOT <color:white>done...";
		} else if (playerName.equals("MURDER")) {
			this.LBText = "Go back";
			this.RBText = "";
			this.TopBanner = "Name it's <color:red>NOT <color:white>done...";
		} else if (playerName.equals("MERCY")) {
			this.LBText = "Go back";
			this.RBText = "";
			this.TopBanner = "Name it's <color:red>NOT <color:white>done...";
		} else if (playerName.equals("GERSON")) {
			this.LBText = "Go back";
			this.RBText = "";
			this.TopBanner = "Name it's <color:red>NOT <color:white>done...";
		} else if (playerName.equals("BPANTS")) {
			this.LBText = "Go back";
			this.RBText = "";
			this.TopBanner = "Name it's <color:red>NOT <color:white>done...";
		} else if (playerName.equals("TEMMIE")) {
			this.LBText = "Go back";
			this.RBText = "";
			this.TopBanner = "Name it's <color:red>NOT <color:white>done...";
		} else if (playerName.equals("WOSHA")) {
			this.LBText = "Go back";
			this.RBText = "";
			this.TopBanner = "Name it's <color:red>NOT <color:white>done...";
		} else if (playerName.equals("JERRY")) {
			this.LBText = "Go back";
			this.RBText = "";
			this.TopBanner = "Name it's <color:red>NOT <color:white>done...";
		} else if (playerName.equals("BLOOKY")) {
			this.LBText = "Go back";
			this.RBText = "";
			this.TopBanner = "Name it's <color:red>NOT <color:white>done...";
		} else if (playerName.equals("SHYREN")) {
			this.LBText = "Go back";
			this.RBText = "";
			this.TopBanner = "Name it's <color:red>NOT <color:white>done...";
		} else if (playerName.equals("AARON")) {
			this.LBText = "Go back";
			this.RBText = "";
			this.TopBanner = "Name it's <color:red>NOT <color:white>done...";
		} else if (playerName.equals("CHARA")) {
			this.LBText = "Go back";
			this.RBText = "";
			this.TopBanner = "Name it's <color:red>NOT <color:white>done...";
		} else if (playerName.equals("FRISK")) {
			this.LBText = "Go back";
			this.RBText = "";
			this.TopBanner = "Name it's <color:red>NOT <color:white>done...";
		} else if (playerName.equals("IGNITE")) {
			this.LBText = "Go back";
			this.RBText = "";
			this.TopBanner = "Wow, you <color:yellow>FINALLY<color:white>\nchose my name...\nAnd unfortunately I'll\nhave to <color:red>refuse<color:white>!";
		} else {
			this.LBText = "No";
			this.RBText = "Yes";
		}
		
		textBlitter.drawString(g, "<color:white>" + TopBanner, (width / 2) - (TopBannerWidth / 2), 28);
		textBlitter.drawString(g, "<color:white>" + LBText, 10, height - 20);
		textBlitter.drawString(g, "<color:white>" + RBText, width - 10 - helpRBWidth, height - 20);
		
		int nameWidth = textBlitter.stringWidth(playerName);
		textBlitter.setFont("fnt_main", "white");
		textBlitter.drawString(g, "<shake:on>" + playerName + "<shake:off>", (width / 2) - nameWidth, (height / 2));
	}
	
	private void drawSettings(Graphics g) {
		textBlitter.setFont("fnt_main", "white");
		textBlitter.drawString(g, "SETTINGS", (width / 2) - (textBlitter.stringWidth("SETTINGS") / 2), 10);
		
		for (int i = 0; i < settingsItems.size(); i++) {
			String item = (String) settingsItems.elementAt(i);
			if (i == settSelIndex) {
				textBlitter.setFont("fnt_maintext", "yellow");
			} else {
				textBlitter.setFont("fnt_maintext", "white");
			}
			textBlitter.drawString(g, item, 40, 50 + (i * 16));
		}
	}
	
	private void drawLoadSave(Graphics g) {}
	
	private void drawErrorScrn(Graphics g) {}

    protected void keyPressed(int keyCode) {
		this.PressedKey = keyCode;
		
		switch (keyCode) {
			// Up Button (-1)
			case -1:
				if (frame == 0) {
					if (mainSelIndex > 0) {
						this.mainSelIndex = mainSelIndex - 1;
					}
				} else if (frame == 1) {
					if (nameSelIndex > 0) {
                        this.nameSelIndex = nameSelIndex - 7;
                    }
				} else if (frame == 3) {
					if (settSelIndex > 0) {
                        this.settSelIndex = settSelIndex - 1;
                    }
				}
				break;
				
			// Down Button (-2)
			case -2:
				if (frame == 0) {
					if (mainSelIndex < menuItems.size() - 1) {
                        this.mainSelIndex = mainSelIndex + 1;
                    }
				} else if (frame == 1) {
					if (nameSelIndex < nameItems.size() - 1) {
                        this.nameSelIndex = nameSelIndex + 7;
                    }
				} else if (frame == 3) {
					if (settSelIndex < settingsItems.size() - 1) {
                        this.settSelIndex = settSelIndex + 1;
                    }
				}
				break;
				
			// Left Button (-3)
			case -3:
				if (frame == 1) {
					if (nameSelIndex > 0) {
                        this.nameSelIndex = nameSelIndex - 1;
                    }
				}
				break;
				
			// Right Button (-4)
			case -4:
				if (frame == 1) {
					if (nameSelIndex < nameItems.size() - 1) {
                        this.nameSelIndex = nameSelIndex + 1;
                    }
				}
				break;
				
			// Z Button (42, -5, 122, 10)
			case 42:
			case -5:
			case 122:
			case 10:
				if (frame == 0) {
					handleSelection(frame);
				} else if (frame == 1) {
					String selectedItem = (String) nameItems.elementAt(nameSelIndex);
					if (playerName.length() < 6) {
                        this.playerName = playerName + selectedItem;
                    }
				} else if (frame == 3) {
					handleSelection(frame);
				}
				break;
				
			// X Button (48, -7, 120)
			case 48:
			case -7:
			case 120:
				if (frame == 1) {
					if (keyCode == -7) {
						if (playerName.length() > 0) {
							this.frame = 2;
						}
					}
				} else if (frame == 2) {
					if (keyCode == -7) {
						if (RBText == "Yes") {
							midlet.switchCanvas(new room_overworld(midlet));
						}
					}
				}
				break;
				
			// C Button (35, -6, 99)
			case 35:
			case -6:
			case 99:
				if (frame == 1) {
					if (keyCode == -6) {
						this.frame = 0;
						this.nameSelIndex = 0;
						this.playerName = "";
					}
				} else if (frame == 2) {
					if (keyCode == -6) {
						this.frame = 1;
					}
				}
				break;
				
			// ESC Button (55)
			case 55:
				break;
				
			// F4 Button (57)
			case 57:
				break;
				
			// Backspace Button (-8, 35)
			case -8:
			case 56:
				if (frame == 1) {
					if (playerName.length() > 0) {
                        this.playerName = playerName.substring(0, playerName.length() - 1);
                    }
				}
				break;
		}
		if (nameSelIndex < 0) {
			this.nameSelIndex = 0;
		}
		if (nameSelIndex > nameItems.size() - 1) {
			this.nameSelIndex = nameItems.size() - 1;
		}
    }
	
	private void handleSelection(int frame) {
        if (frame == 0) {
            String selectedItem = (String) menuItems.elementAt(mainSelIndex);
            if (selectedItem.equals("Begin Game")) {
                this.frame = 1;
            } if (selectedItem.equals("Settings")) {
                this.frame = 3;
            }  if (selectedItem.equals("Controls")) {
                midlet.switchCanvas(new room_controls(midlet));
            } 
        } else if (frame == 1) {
            String selectedItem = (String) nameItems.elementAt(nameSelIndex);
            int IndexLenghtName = textBlitter.stringWidth(playerName);
            
            if (IndexLenghtName < 6) {
                this.playerName = playerName + selectedItem;
            }
        } else if (frame == 3) {
            String selectedItem = (String) settingsItems.elementAt(settSelIndex);
            if (selectedItem.equals("EXIT")) {
                this.frame = 0; 
			}
        }
        
    }

    public void destroy() {
		midlet.cleanupResources();
		midlet.stopAllMIDI();
		System.out.println("");
    }
}