package game;


import org.lwjgl.input.Mouse;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.Sound;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

public class WinGameState extends BasicGameState{
	int stateID = -1;
	//Sound fx = null;
	Input inputMouse = new Input(650);
	int counter = 0;
    WinGameState( int stateID ) 
    {
       this.stateID = stateID;
    }
 
    @Override
    public int getID() {
        return stateID;
    }
 
    public void init(GameContainer gc, StateBasedGame sbg) throws SlickException {
    	//fx = new Sound("data/ding.wav");
    }
 
    public void render(GameContainer gc, StateBasedGame sbg, Graphics gp) throws SlickException {
    	gp.drawString("YOU WIN!", 250, 150);
    }
 
    public void update(GameContainer gc, StateBasedGame sbg, int delta) throws SlickException {
    	counter+=delta;
    	if(counter>=1500){
    		sbg.enterState(MainGame.MENUSTATE);
    	}
    	 
    }
}
