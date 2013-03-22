package game;


import java.util.ArrayList;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.Sound;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

import orig.Creature;
import orig.Item;
import orig.Race;

public class MainGameState extends BasicGameState{
	int stateID = -1;
	Image floor = null;
	Image wall1 = null;
	Image wall2 = null;
	Image wall3 = null;
	Image wall4 = null;
	Image wall5 = null;
	Image wall6 = null;
	Image player = null;
	Image enemy = null;
	Image b_equip = null;
	Image b_unequip = null;
	Image b_drop = null;
	Image b_examine = null;
	Race r = new Race();
	Creature c = new Creature(r);
	ArrayList<Item> items;
	OnScreenChar o1;
	
	//variables related to the size of screen and tiles
	public static final int sizeX = 1024-1;
	public static final int sizeY = 672-1;
	final int tileSize = 32;
	final int upperX = sizeX-tileSize; //last place a tile should be rendered
	final int upperY = sizeY-tileSize; //last place a tile should be rendered
	
	//player stuff
	int pX = sizeX-6*tileSize;
	int pY = sizeY-6*tileSize;
	Creature player1;
	
	//menu variables, can be tweaked to change the menus
	final int textAllowed = 32; //How much vertical space is allowed for text
	final int header = 64; //How much vertical space is reserved to make things look nice
	final int footer = 64; //same except for bottom
	final int itemFit = ((sizeY-header-footer)/textAllowed)-2; //trust me on this
	final int itemMax = 250; //max length for an item name
	
	
	int inputDelta = 100;
	boolean menutime = false;
	boolean inventorytime = false;
	boolean equippedtime = false;
	int outer = 0;
	int onscreen=0;
	boolean scrolldown;
	
    MainGameState( int stateID ) 
    {
       this.stateID = stateID;
       player1 = new Creature(new Race());
    }
 
    @Override
    public int getID() {
        return stateID;
    }
	
	public void init(GameContainer container, StateBasedGame Sbg) throws SlickException {
		floor = new Image("data/tiles/stone_floor.png");
		wall1 = new Image("data/tiles/stone_wall_updown.png");
		wall2 = new Image("data/tiles/stone_wall_leftright.png");
		wall3 = new Image("data/tiles/stone_wall_leftdown.png");
		wall4 = new Image("data/tiles/stone_wall_leftup.png");
		wall5 = new Image("data/tiles/stone_wall_rightdown.png");
		wall6 = new Image("data/tiles/stone_wall_rightup.png");
		player = new Image("data/tiles/player.png");
		enemy = new Image("data/tiles/enemy.png");
		
		//buttons
		b_equip = new Image("data/tiles/button_equip.png");
		b_unequip = new Image("data/tiles/button_unequip.png");
		b_drop = new Image("data/tiles/button_drop.png");
		b_examine = new Image("data/tiles/button_examine.png");
		
		o1 = new OnScreenChar(enemy, tileSize*8-1, tileSize*8-1, tileSize, c);
		Item i = new Item();
		Item j = new Item();
		Item k = new Item();
		j.name = "ROFL";
		k.name = "LMAO";
		player1.pickup(i);
		player1.pickup(j);
		player1.pickup(k);
	}
 
	public void render(GameContainer container, StateBasedGame sbg, Graphics g) throws SlickException {
		if(!menutime){
			wall5.draw(0,0);
			for(int i=tileSize-1; i<upperY; i+=tileSize){
				wall1.draw(0, i);
			}
			wall6.draw(0, upperY);
			for(int i=tileSize-1; i<upperX; i+=tileSize){
				wall2.draw(i, upperY);
			}
			wall4.draw(upperX, upperY);
			for(int i=tileSize-1; i<upperY; i+=tileSize){
				wall1.draw(upperX, i);
			}
			wall3.draw(upperX, 0);
			for(int i=tileSize-1; i<upperX; i+=tileSize){
				wall2.draw(i, 0);
			}
			for(int i=tileSize-1; i<upperX; i+=tileSize){
				for(int j=tileSize-1; j<upperY; j+=tileSize){
					floor.draw(i, j);
				}
			}
			o1.draw();
			player.draw(pX, pY);
		}
		else{
			if(inventorytime){
				if(outer>0){
					g.drawString("Scroll Up", 0, header);
				}
				onscreen=0;
				scrolldown = false;
				for(int i=outer;i<player1.getInventory().size(); i++){
					g.drawString(player1.getInventory().get(i).getName(),0,header+((i+1)*textAllowed));
					b_equip.draw(itemMax, header+((i+1)*textAllowed));
					b_examine.draw(itemMax+tileSize,header+((i+1)*textAllowed));
					b_drop.draw(itemMax+(tileSize*2),header+((i+1)*textAllowed));
					onscreen++;
					if(i>outer+itemFit){
						scrolldown=true;
						g.drawString("Scroll Down", 0, footer-textAllowed);
						break;
					}
				}
				g.drawString("Back",700,header);
			}
			else if(equippedtime){
				g.drawString("Equipped", 0, header);
				g.drawString("Head: "+player1.getEquipped(0), 0, header+textAllowed);
				b_unequip.draw(itemMax, header+(1*textAllowed));
				b_examine.draw(itemMax+tileSize,header+(1*textAllowed));
				b_drop.draw(itemMax+(tileSize*2),header+(1*textAllowed));
				g.drawString("Body: "+player1.getEquipped(1), 0, header+(2*textAllowed));
				b_unequip.draw(itemMax, header+(2*textAllowed));
				b_examine.draw(itemMax+tileSize,header+(2*textAllowed));
				b_drop.draw(itemMax+(tileSize*2),header+(2*textAllowed));
				g.drawString("Legs: "+player1.getEquipped(2), 0, header+(3*textAllowed));
				b_unequip.draw(itemMax, header+(3*textAllowed));
				b_examine.draw(itemMax+tileSize,header+(3*textAllowed));
				b_drop.draw(itemMax+(tileSize*2),header+(3*textAllowed));
				for(int i=3;i<player1.getNumArms()+3;i++){
					g.drawString("Hands: "+player1.getEquipped(i), 0, header+(i+1)*textAllowed);
					b_unequip.draw(itemMax, header+((i+1)*textAllowed));
					b_examine.draw(itemMax+tileSize,header+((i+1)*textAllowed));
					b_drop.draw(itemMax+(tileSize*2),header+((i+1)*textAllowed));
				}
				g.drawString("Back", 0, sizeY-footer-textAllowed);
			}
			else{
				g.drawString("Inventory",0,header);
				g.drawString("Equipped", 0, header+(1*textAllowed));
				g.drawString("Return", 0,header+(2*textAllowed));
			}
		}
    }
	
    public void update(GameContainer gc, StateBasedGame sbg, int delta) throws SlickException {
    	Input input = gc.getInput();
    	boolean kp = false;
    	inputDelta-=delta;
    	if(inputDelta<0){
	    	if(!menutime){
		    	if(input.isKeyDown(Input.KEY_NUMPAD1)){
		    		kp = true;
		    		pX-=tileSize;
		    		pY+=tileSize;
		    	}
		    	else if(input.isKeyDown(Input.KEY_NUMPAD2)||input.isKeyDown(Input.KEY_DOWN)){
		    		kp = true;
		    		pY+=tileSize;
		    	}
		    	else if(input.isKeyDown(Input.KEY_NUMPAD3)){
		    		kp = true;
		    		pX +=tileSize;
		    		pY +=tileSize;
		    	}
		    	else if(input.isKeyDown(Input.KEY_NUMPAD6)||input.isKeyDown(Input.KEY_RIGHT)){
		    		kp = true;
		    		pX +=tileSize;
		    	}
		    	else if(input.isKeyDown(Input.KEY_NUMPAD9)){
		    		kp = true;
		    		pX +=tileSize;
		    		pY -=tileSize;
		    	}
		    	else if(input.isKeyDown(Input.KEY_NUMPAD8)||input.isKeyDown(Input.KEY_UP)){
		    		kp = true;
		    		pY -=tileSize;
		    	}
		    	else if(input.isKeyDown(Input.KEY_NUMPAD7)){
		    		kp = true;
		    		pX -=tileSize;
		    		pY -=tileSize;
		    	}
		    	else if(input.isKeyDown(Input.KEY_NUMPAD4)||input.isKeyDown(Input.KEY_LEFT)){
		    		kp = true;
		    		pX -=tileSize;
		    	}
		    	else if(input.isKeyDown(Input.KEY_ESCAPE)){
		    		menutime = true;
		    	}
		    	if(kp){
		    		inputDelta=100;
		    		if(pX>upperX-tileSize){
		    			pX=upperX-tileSize;
		    		}
		    		if(pX<tileSize-1){
		    			pX=tileSize-1;
		    		}
		    		if(pY>upperY-tileSize){
		    			pY=upperY-tileSize;
		    		}
		    		if(pY<tileSize-1){
		    			pY=tileSize-1;
		    		}
		    		o1.step(pX, pY);
		    	}
	    	}
	    	else{
	    		if(inventorytime){
	    			int mouseX = input.getMouseX();
		        	int mouseY = input.getMouseY();
		        	if(mouseX<150&&mouseY<header+textAllowed&&input.isMouseButtonDown(Input.MOUSE_LEFT_BUTTON)&&outer>0){
		        		outer=outer-itemFit;
		        	}
		        	else if(mouseX>itemMax&&mouseX<=itemMax+tileSize&&mouseY>(header+textAllowed)&&mouseY<(header+(onscreen+1)*textAllowed)&&input.isMouseButtonDown(Input.MOUSE_LEFT_BUTTON)){
		        		player1.equip(player1.getInventory().get(((mouseY-(header+textAllowed))/textAllowed)+outer));
		        	}
		        	else if(mouseX>itemMax+tileSize&&mouseX<=itemMax+2*tileSize&&mouseY>(header+textAllowed)&&mouseY<(header+(onscreen+1)*textAllowed)&&input.isMouseButtonDown(Input.MOUSE_LEFT_BUTTON)){
		        		//examine
		        	}
		        	else if(mouseX>itemMax+2*tileSize&&mouseX<=itemMax+3*tileSize&&mouseY>(header+textAllowed)&&mouseY<(header+(onscreen+1)*textAllowed)&&input.isMouseButtonDown(Input.MOUSE_LEFT_BUTTON)){
		        		player1.drop(player1.getInventory().get(((mouseY-(header+textAllowed))/textAllowed)+outer));
		        	}
		        	else if(mouseX<150&&mouseY>=footer-textAllowed&&input.isMouseButtonDown(Input.MOUSE_LEFT_BUTTON)&&scrolldown){
		        		outer=outer+itemFit;
		        	}
		        	else if(mouseX>700&&mouseY<header+textAllowed&&input.isMouseButtonDown(Input.MOUSE_LEFT_BUTTON)){
		        		inventorytime=false;
		        	}
	    		}
	    		else if(equippedtime){
		    		int mouseX = input.getMouseX();
		        	int mouseY = input.getMouseY();
	    			if(mouseX<150&&mouseY>sizeY-footer-textAllowed&&input.isMouseButtonDown(Input.MOUSE_LEFT_BUTTON)){
	    				equippedtime=false;
	    			}
	    			else if(mouseX>itemMax&&mouseX<=itemMax+tileSize&&mouseY>(header+textAllowed)&&mouseY<(header+(onscreen+1)*textAllowed)&&input.isMouseButtonDown(Input.MOUSE_LEFT_BUTTON)){
		        		player1.unequip(player1.getInventory().get(((mouseY-(header+textAllowed))/textAllowed)+outer));
		        	}
		        	else if(mouseX>itemMax+tileSize&&mouseX<=itemMax+2*tileSize&&mouseY>(header+textAllowed)&&mouseY<(header+(onscreen+1)*textAllowed)&&input.isMouseButtonDown(Input.MOUSE_LEFT_BUTTON)){
		        		//examine
		        	}
		        	else if(mouseX>itemMax+2*tileSize&&mouseX<=itemMax+3*tileSize&&mouseY>(header+textAllowed)&&mouseY<(header+(onscreen+1)*textAllowed)&&input.isMouseButtonDown(Input.MOUSE_LEFT_BUTTON)){
		        		player1.drop(player1.getInventory().get(((mouseY-(header+textAllowed))/textAllowed)+outer));
		        	}
	    		}
	    		else{
		    		int mouseX = input.getMouseX();
		        	int mouseY = input.getMouseY();
		        	if(mouseX<150&&mouseY<header+textAllowed&&input.isMouseButtonDown(Input.MOUSE_LEFT_BUTTON)){
		        		inventorytime = true;
		        	}
		        	else if(mouseX<150&&mouseY<header+textAllowed*2&&input.isMouseButtonDown(Input.MOUSE_LEFT_BUTTON)){
		        		equippedtime = true;
		        	}
		        	else if(mouseX<150&&mouseY<header+(3*textAllowed)&&input.isMouseButtonDown(Input.MOUSE_LEFT_BUTTON)){
		        		menutime = false;
		        	}
	    		}
	    		inputDelta=100;
	    	}
    	}
    }
    
    
    
}
