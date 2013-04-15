package game;


import org.lwjgl.input.Mouse;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.Sound;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

public class MenuState extends BasicGameState{
	int stateID = -1;
	//Sound fx = null;
	Input inputMouse = new Input(650);
	private String mouse = "No input yet!";
    MenuState( int stateID ) 
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
    	gp.drawString("Welcome to a Rougelike", 250, 150);
    	//gp.drawString("inputMouse x " + inputMouse.getMouseX() + " y " + inputMouse.getMouseY(), 100, 420);
    	//gp.drawString(mouse, 100, 400);
    	gp.drawString("Start", 250, 200);
    	gp.drawString("Create Character", 250, 250);
    }
 
    public void update(GameContainer gc, StateBasedGame sbg, int delta) throws SlickException {
    	Input input = gc.getInput();
    	//int posX = Mouse.getX();
		//int posY = Mouse.getY(); 
    	int mouseX = input.getMouseX();
    	int mouseY = input.getMouseY();
    	boolean insideStartGame = false;
    	boolean clickcc = false;
    	
    	if( ( mouseX >= 250 && mouseX <= 250 + 50) &&
    		    ( mouseY >= 200 && mouseY <= 200 + 50) ){
    		    insideStartGame = true;
    		  //  fx.play();
    	}
    	else if( ( mouseX >= 250 && mouseX <= 250 + 75) &&
    		    ( mouseY >= 230 && mouseY <= 230 + 50) ){
    		    clickcc = true;
    		  //  fx.play();
    	}
    	else{
    		
    	}
    	if (insideStartGame&& input.isMouseButtonDown(Input.MOUSE_LEFT_BUTTON) ){
    		sbg.enterState(MainGame.MAINGAMESTATE);
    	}
    	if (clickcc&& input.isMouseButtonDown(Input.MOUSE_LEFT_BUTTON) ){
    		sbg.enterState(MainGame.CHARACTERCREATESTATE);
    	}
    	 
    }
}
