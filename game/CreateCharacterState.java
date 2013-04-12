package game;


import java.awt.Font;
import java.util.ArrayList;

import org.lwjgl.input.Mouse;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.Sound;
import org.newdawn.slick.UnicodeFont;
import org.newdawn.slick.font.effects.ColorEffect;
import org.newdawn.slick.gui.TextField;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.Image;

import orig.Creature;
import orig.Element;
import orig.Race;
import orig.UET;
import util.ImageUtil;



public class CreateCharacterState extends BasicGameState{
	int stateID = -1;
	Image screen;
	int state = 0;
	//Sound fx = null;
	Input inputMouse = new Input(650);
	private String mouse = "No input yet!";
	TextField name;
	UnicodeFont font2;
	CreateCharacterState( int stateID ) 
    {
       this.stateID = stateID;
    }
 
    @Override
    public int getID() {
        return stateID;
    }
 
    public void init(GameContainer gc, StateBasedGame sbg) throws SlickException {
    	//fx = new Sound("data/ding.wav");
    	
    	font2 = getNewFont("Arial" , 16);
    	
    	
    	
    	font2.loadGlyphs();
    }
 
    @Override
	public void enter(GameContainer gc, StateBasedGame sbg) throws SlickException
	{
		
		name = new TextField(gc, font2, 350, 300, 180, 25);
		
		
		
		
	}
    
    public void render(GameContainer gc, StateBasedGame sbg, Graphics gp) throws SlickException {
    	
    	
    	if(name != null) name.render(gc, gp);
    	gp.drawString("inputMouse x " + inputMouse.getMouseX() + " y " + inputMouse.getMouseY(), 100, 420);
    	gp.drawString(mouse, 100, 400);
    	
    	if(state == 0)
    	{
    		gp.drawString("Welcome to Character Creation", 300, 250);
    		gp.drawString("Name: ", 300, 300);
    		gp.drawString("Next", 300, 350);
    		
    	}
    	
    	if(state == 1)
    	{
    		name.setLocation(2000, 2000);
    		gp.drawString("Choose Your Race", 300, 350);
    		
    		gp.drawString("HUMAN", 300, 400);
    		
    	}
    	
    	if(state == 2)
    	{
    		
    	}
    }
 
    public void update(GameContainer gc, StateBasedGame sbg, int delta) throws SlickException {
    	Input input = gc.getInput();
    	int posX = Mouse.getX();
		int posY = Mouse.getY(); 
    	int mouseX = input.getMouseX();
    	int mouseY = input.getMouseY();
    	font2.loadGlyphs();
    	
    	boolean next = false;
    	if(state == 0)
    	{
    		if( ( mouseX >= 300 && mouseX <= 330) &&
        		    ( mouseY >= 325 && mouseY <= 345 + 50) ){
        		    next = true;
    		{
    			
    			if (next&& input.isMouseButtonDown(Input.MOUSE_LEFT_BUTTON) ){
				
    					state++;
    	    		
    	    	}
    		}
    	}
    }
    	boolean race = false;
    	if(state == 1)
    	{
    		UET e = UET.getUET();
    		if( ( mouseX >= 300 && mouseX <= 330) &&
        		    ( mouseY >= 375 && mouseY <= 3955 + 50) ){
        		    race = true;
    		{
    			//System.out.println(Math.pow((Math.random()*200),.5));
    			if (race&& input.isMouseButtonDown(Input.MOUSE_LEFT_BUTTON) ){
    					//TODO:ALEX																										//casing							//fluid						//organs
    					Race human = new Race("human", 0, new Element(/* "Produces",5,7,8*/) , new Element(/*"Consumes",12,10,11*/), e.getElementList().get(e.MEAT), e.getElementList().get(e.WATER), e.getElementList().get(e.MEAT), 2, 2, new ArrayList<Race>());
    					Creature c = new Creature(human, name.getText());
    					
    					MainGameState.loadedCreature = c;
    					sbg.enterState(MainGame.MAINGAMESTATE);
    					
    	    		
    	    	}
    		}
    	}
    	}
    	
    	
    	
    	/*boolean insideStartGame = false;
    	if( ( mouseX >= 250 && mouseX <= 250 + 50) &&
    		    ( mouseY >= 200 && mouseY <= 200 + 50) ){
    		    insideStartGame = true;
    		  //  fx.play();
    	}
    	else{
    		
    	}
    	if (( mouseX >= 250 && mouseX <= 250 + 50) &&
    		( mouseY >= 200 && mouseY <= 200 + 50) && 
    		  input.isMouseButtonDown(Input.MOUSE_LEFT_BUTTON) ){
    		
    		state++;
    	}
    	*/
    	
    }
    
    
    public UnicodeFont getNewFont(String fontName , int fontSize)
    {
        font2 = new UnicodeFont(new Font(fontName , Font.PLAIN , fontSize));
//        font2.addGlyphs("@");
        font2.getEffects().add(new ColorEffect(java.awt.Color.white));
        return (font2);
    }
}
