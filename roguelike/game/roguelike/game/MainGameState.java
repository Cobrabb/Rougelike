package roguelike.game;


import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.Sound;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;
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
	int pX = 223;
	int pY = 223;
	int inputDelta = 100;
	
    MainGameState( int stateID ) 
    {
       this.stateID = stateID;
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
	}
 
	public void render(GameContainer container, StateBasedGame sbg, Graphics g) throws SlickException {
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
	    			pY=567;
	    		}
	    		if(pY<15){
	    			pY=15;
	    		}
	    		e1.step(pX, pY);
	    	}
    	}
    }
}
