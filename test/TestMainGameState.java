package test;


import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.Sound;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

import orig.DungeonMap;
import orig.Element;
import orig.Planet;
public class TestMainGameState extends BasicGameState{
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
	int pX = 223;
	int pY = 223;
	int inputDelta = 100;
	Planet planet = null;
	
    TestMainGameState( int stateID ) 
    {
       this.stateID = stateID;
    }
 
    @Override
    public int getID() {
        return stateID;
    }
	
	public void init(GameContainer container, StateBasedGame Sbg) throws SlickException {
		Element elem1 = new Element("stone_floor", 1.0);
		Element elem2 = new Element("stone_wall_updown", 2.0);
		Element elem3 = new Element("stone_wall_leftright", 2.0);
		planet = new Planet(new Element[] {elem1, elem2, elem3});
		String path = planet.generateMap("planetX");
		planet.setCurrentDungeon(path);
		
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
	}
 
	public void render(GameContainer container, StateBasedGame sbg, Graphics g) throws SlickException {
		DungeonMap dm = planet.getCurrentDungeon();
		dm.render(container, sbg, g);
		e1.draw();
		player.draw(pX, pY);
    }
	
	public void menu(GameContainer gc, Graphics gp){
		gp.drawString("Inventory",0,0);
		gp.drawString("Return", 0,50);
		boolean b = false;
		Input input;
		int mouseX=0;
		int mouseY=0;
		while(!b){
			input = gc.getInput();
			mouseX = input.getMouseX();
			mouseY = input.getMouseY();
			boolean bo = input.isMouseButtonDown(Input.MOUSE_LEFT_BUTTON);
			if(mouseX>=0&&mouseX<=150&&mouseY>=0&&mouseY<50&&bo){
				invent(gc, gp);
			}
			else if(mouseX>=0&&mouseY<=150&&mouseY>=50&&mouseY<100&&bo){
				b=true;
			}
		}
	}
	
	public void invent(GameContainer gc, Graphics gp){
		
	}
	
    public void update(GameContainer gc, StateBasedGame sbg, int delta) throws SlickException {
    	Input input = gc.getInput();
    	boolean kp = false;
    	inputDelta-=delta;
    	if(inputDelta<0){
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
    }
    
    
    
}
