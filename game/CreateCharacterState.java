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
import orig.Creature.cStats;
import orig.Creature.sVal;
import orig.Element;
import orig.Item;
import orig.Item.AttackType;
import orig.Item.iType;
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
	UET e = null;
	transient Image ihuman;
	transient Image iargok;
	transient Image iwonka;
	String savedText;
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
    	 e = UET.getUET();
    	font2 = getNewFont("Arial" , 16);
    	
    	ihuman = ImageUtil.getImage("human");
    	iargok = ImageUtil.getImage("argok");
    	iwonka = ImageUtil.getImage("oompa");
    	font2.loadGlyphs();
    }
 
    @Override
	public void enter(GameContainer gc, StateBasedGame sbg) throws SlickException
	{
		
		name = new TextField(gc, font2, 350, 300, 180, 25);
		
		
		
		
	}
    
    public void render(GameContainer gc, StateBasedGame sbg, Graphics gp) throws SlickException {
    	
    	
    	if(name != null) name.render(gc, gp);
    	//gp.drawString("inputMouse x " + inputMouse.getMouseX() + " y " + inputMouse.getMouseY(), 100, 420);
    	//gp.drawString(mouse, 100, 400);
    	
    	
    	
    	if(state == 0)
    	{
    		gp.drawString("Welcome to Character Creation", 300, 250);
    		gp.drawString("Name: ", 300, 300);
    		gp.drawString("Next", 300, 350);
    		
    	}
    	
    	if(state == 1)
    	{
    		name.setLocation(2000, 2000);
    		
    		ihuman.draw(80, 130);
    		iargok.draw(80,200);
    		iwonka.draw(80,270);
    		
    		gp.drawString("Choose Your Race", 300, 115);
    		
    		gp.drawString("HUMAN", 130, 130);
    		gp.drawString("The Human race is a young adventurous race with 2 arms and 2 legs\nand eager to explore the universe.", 200, 130);
    		
    		gp.drawString("ARGOK", 130, 200);
    		gp.drawString("The Argok race is an old primative race who devolved into a floating collection\n of stone-like creatures.They have 4 arms and 2 legs.", 200, 200);
    		
    		gp.drawString("WONKA", 130, 270);
    		gp.drawString("The Wonka race is a superior advanced race and is the only race to ever reach \nthe final evolution stage of fudge.Their entire body " +
    					  "is made of fudge and have\n4 arms and 2 legs", 200, 270);
    		
    		
    		
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
    	if (!name.getText().equals(savedText)) {
    		font2.loadGlyphs();
    		savedText = name.getText();
    	}
    	
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
    	
    	if(state == 1)
    	{
    		
    		if( ( mouseX >= 130 && mouseX <= 200) &&
        		    ( mouseY >= 130 && mouseY <= 130 + 20) ){
        		   
    		{
    			
    			if (input.isMouseButtonDown(Input.MOUSE_LEFT_BUTTON) ){
    																																	//casing							//fluid						//organs
    					Race human = new Race("human", 0, new Element(/* "Produces",5,7,8*/) , new Element(/*"Consumes",12,10,11*/), e.getElementList().get(e.MEAT), e.getElementList().get(e.WATER), e.getElementList().get(e.MEAT), 2, 2, new ArrayList<Race>());
    					human.gain(cStats.DETECT_SIGHT, sVal.XP, 11);
    					human.gain(cStats.SPEED_ATTACK, sVal.XP, 11);
    					human.gain(cStats.TECH_WEAPON, sVal.XP, 11);

    					Element array[] = new Element[1];
    					array[0] = e.getElementList().get(e.METAL);
    					Creature c = new Creature(human, name.getText());
    					c.pickAndEquip(new Item("Metal Sword",array,null, 1, 0, .5, 15, iType.HAND, 1));
    					c.pickAndEquip(new Item("Metal Tunic",array,null, 50, 0, .5, 15, iType.ARMOR, 1,100));
    					c.pickAndEquip(new Item("Metal Boots",array,null, 1, 0, .5, 15, iType.BOOTS, 1,100));
    					c.pickAndEquip(new Item("Metal Helm",array,null, 1, 0, .5, 15, iType.HEAD, 1,100));
    					MainGameState.loadedCreature = c;
    					enterMainGame(sbg);
    					
    	    		
    	    	}
    		}
    	}
    		
    		
    		if( ( mouseX >= 130 && mouseX <= 180) &&
        		    ( mouseY >= 180 && mouseY <= 190+50) ){// +50 because game does  not start at 0,0 have to offset it by 50
        		    
    		{
    			
    			if (input.isMouseButtonDown(Input.MOUSE_LEFT_BUTTON) ){
    																																				//casing							//fluid						//organs
    					Race argok = new Race("argok", 0, new Element(/* "Produces",5,7,8*/) , new Element(/*"Consumes",12,10,11*/), e.getElementList().get(e.STONE), e.getElementList().get(e.DIRT), e.getElementList().get(e.WOOD), 4, 2, new ArrayList<Race>());
    					argok.gain(cStats.STAM_HEALTH, sVal.XP, 11);
    					argok.gain(cStats.STR_PHYS_ATTACK, sVal.XP, 11);
    					
    					
    					
    					Creature c = new Creature(argok, name.getText());
    					Element array[] = new Element[1];
    					array[0] = e.getElementList().get(e.STONE);
    					c.pickAndEquip(new Item("Blarg",array,null, 1, 0, .5, 15, iType.HAND, 1));
    					c.pickAndEquip(new Item("Stone Tunic",array,null, 50, 0, .5, 15, iType.ARMOR, 1,100));
    					c.pickAndEquip(new Item("Stone Boots",array,null, 1, 0, .5, 15, iType.BOOTS, 1,100));
    					c.pickAndEquip(new Item("Stone Helm",array,null, 1, 0, .5, 15, iType.HEAD, 1,100));
    					
    					
    					
    					MainGameState.loadedCreature = c;
    					enterMainGame(sbg);
    					
    	    		
    	    	}
    		}
    	}
    		
    		
    		

    		if( ( mouseX >= 130 && mouseX <= 180) && //WONKAS!
        		    ( mouseY >= 270 && mouseY <= 270+50) ){// +50 because game does  not start at 0,0 have to offset it by 50
        		    
    		{
    			
    			if (input.isMouseButtonDown(Input.MOUSE_LEFT_BUTTON) ){
    																																				//casing							//fluid						//organs
    					Race wonka = new Race("oompa", 0, new Element(/* "Produces",5,7,8*/) , new Element(/*"Consumes",12,10,11*/), e.getElementList().get(e.FUDGE), e.getElementList().get(e.WATER), e.getElementList().get(e.FUDGE), 4, 2, new ArrayList<Race>());
    					wonka.gain(cStats.TECH_WEAPON, sVal.XP, 11);
    					wonka.gain(cStats.TECH_ARMOR, sVal.XP, 11);
    					wonka.gain(cStats.STEALTH_SIGHT, sVal.XP, 11);
    					wonka.gain(cStats.DETECT_SIGHT, sVal.XP, 11);
    					wonka.gain(cStats.SPEED_MOVE, sVal.XP, 11);
    					
    					
    					
    					Creature c = new Creature(wonka, name.getText());
    					Element array[] = new Element[1];
    					array[0] = e.getElementList().get(e.FUDGE);
    					c.pickAndEquip(new Item("Candy cane",array,null, 1, 0, .5, 15, iType.HAND, 1));
    					c.pickAndEquip(new Item("Fudge Tunic",array,null, 1, 0, .5, 15, iType.ARMOR, 1,100));
    					c.pickAndEquip(new Item("Fudge Boots",array,null, 1, 0, .5, 15, iType.BOOTS, 1,100));
    					c.pickAndEquip(new Item("Fudge Helm",array,null, 1, 0, .5, 15, iType.HEAD, 1,100));
    					
    					MainGameState.loadedCreature = c;
    					enterMainGame(sbg);
    					
    	    		
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
    
    private void enterMainGame(StateBasedGame sbg) {
    	state = 0;
		sbg.enterState(MainGame.MAINGAMESTATE);
    }
    
    
    public UnicodeFont getNewFont(String fontName , int fontSize)
    {
        font2 = new UnicodeFont(new Font(fontName , Font.PLAIN , fontSize));
//        font2.addGlyphs("@");
        font2.getEffects().add(new ColorEffect(java.awt.Color.white));
        return (font2);
    }
}
