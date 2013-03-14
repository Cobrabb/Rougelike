package game;


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
	Enemy e1;
	
	//player stuff
	int pX = 223;
	int pY = 223;
	Creature player1;
	
	//menu variables, can be tweaked to change the menus
	int textAllowed = 25; //How much vertical space is allowed for text
	int header = 50; //How much vertical space is reserved to make things look nice
	int footer = 50; //same except for bottom
	int itemFit = ((600-header-footer)/textAllowed)-2; //trust me on this
	
	
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
		Sound roar = new Sound("data/roar.wav");
		e1 = new Enemy(enemy, 95, 95, roar);
		Item i = new Item();
		player1.pickup(i);
		player1.pickup(i);
		player1.pickup(i);
	}
 
	public void render(GameContainer container, StateBasedGame sbg, Graphics g) throws SlickException {
		if(!menutime){
			wall5.draw(0,0);
			for(int i=15; i<575; i+=16){
				wall1.draw(0, i);
			}
			wall6.draw(0, 575);
			for(int i=15; i<783; i+=16){
				wall2.draw(i, 575);
			}
			wall4.draw(783, 575);
			for(int i=15; i<575; i+=16){
				wall1.draw(783, i);
			}
			wall3.draw(783, 0);
			for(int i=15; i<783; i+=16){
				wall2.draw(i, 0);
			}
			for(int i=15; i<768; i+=16){
				for(int j=15; j<560; j+=16){
					floor.draw(i, j);
				}
			}
			e1.draw();
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
				g.drawString("Body: "+player1.getEquipped(1), 0, header+(2*textAllowed));
				g.drawString("Legs: "+player1.getEquipped(2), 0, header+(3*textAllowed));
				for(int i=3;i<player1.getNumArms()+3;i++){
					g.drawString("Hands: "+player1.getEquipped(i), 0, header+(i+1)*textAllowed);
				}
				g.drawString("Back", 0, 600-footer-textAllowed);
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
		    		pX-=16;
		    		pY+=16;
		    	}
		    	else if(input.isKeyDown(Input.KEY_NUMPAD2)||input.isKeyDown(Input.KEY_DOWN)){
		    		kp = true;
		    		pY+=16;
		    	}
		    	else if(input.isKeyDown(Input.KEY_NUMPAD3)){
		    		kp = true;
		    		pX +=16;
		    		pY +=16;
		    	}
		    	else if(input.isKeyDown(Input.KEY_NUMPAD6)||input.isKeyDown(Input.KEY_RIGHT)){
		    		kp = true;
		    		pX +=16;
		    	}
		    	else if(input.isKeyDown(Input.KEY_NUMPAD9)){
		    		kp = true;
		    		pX +=16;
		    		pY -=16;
		    	}
		    	else if(input.isKeyDown(Input.KEY_NUMPAD8)||input.isKeyDown(Input.KEY_UP)){
		    		kp = true;
		    		pY -=16;
		    	}
		    	else if(input.isKeyDown(Input.KEY_NUMPAD7)){
		    		kp = true;
		    		pX -=16;
		    		pY -=16;
		    	}
		    	else if(input.isKeyDown(Input.KEY_NUMPAD4)||input.isKeyDown(Input.KEY_LEFT)){
		    		kp = true;
		    		pX -=16;
		    	}
		    	else if(input.isKeyDown(Input.KEY_ESCAPE)){
		    		menutime = true;
		    	}
		    	if(kp){
		    		inputDelta=100;
		    		if(pX>767){
		    			pX=767;
		    		}
		    		if(pX<15){
		    			pX=15;
		    		}
		    		if(pY>559){
		    			pY=559;
		    		}
		    		if(pY<15){
		    			pY=15;
		    		}
		    		e1.step(pX, pY);
		    	}
	    	}
	    	else{
	    		if(inventorytime){
	    			int mouseX = input.getMouseX();
		        	int mouseY = input.getMouseY();
		        	if(mouseX<150&&mouseY<header+textAllowed&&input.isMouseButtonDown(Input.MOUSE_LEFT_BUTTON)&&outer>0){
		        		outer=outer-itemFit;
		        	}
		        	else if(mouseX<150&&mouseY>(header+textAllowed)&&mouseY<(header+onscreen*textAllowed)&&input.isMouseButtonDown(Input.MOUSE_LEFT_BUTTON)){
		        		boolean equipcheck = player1.equip(player1.getInventory().get((mouseY-header-itemFit)/itemFit+outer));
		        		if(equipcheck){
		        			player1.getInventory().remove((mouseY-header-itemFit)/itemFit+outer);
		        		}
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
	    			if(mouseX<150&&mouseY>=600-footer-textAllowed&&input.isMouseButtonDown(Input.MOUSE_LEFT_BUTTON)){
	    				equippedtime=false;
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
