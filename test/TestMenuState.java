package test;


import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.Sound;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

public class TestMenuState extends BasicGameState{
	int stateID = -1;
	Sound fx = null;
	 
    TestMenuState( int stateID ) 
    {
       this.stateID = stateID;
    }
 
    @Override
    public int getID() {
        return stateID;
    }
 
    public void init(GameContainer gc, StateBasedGame sbg) throws SlickException {
    	fx = new Sound("data/ding.wav");
    }
 
    public void render(GameContainer gc, StateBasedGame sbg, Graphics gp) throws SlickException {
    	gp.drawString("Welcome to a Rougelike", 250, 150);
    	gp.drawString("Start", 250, 200);
    }
 
    public void update(GameContainer gc, StateBasedGame sbg, int delta) throws SlickException {
    	Input input = gc.getInput();
    	 
    	int mouseX = input.getMouseX();
    	int mouseY = input.getMouseY();
    	boolean insideStartGame = false;
    	if( ( mouseX >= 250 && mouseX <= 250 + 50) &&
    		    ( mouseY >= 200 && mouseY <= 200 + 50) ){
    		    insideStartGame = true;
    		    fx.play();
    	}
    	else{
    		
    	}
    	if (insideStartGame&& input.isMouseButtonDown(Input.MOUSE_LEFT_BUTTON) ){
    		sbg.enterState(TestMainGame.MAINGAMESTATE);
    	}
    }
}
