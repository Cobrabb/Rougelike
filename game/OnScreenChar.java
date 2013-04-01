package game;

import java.util.ArrayList;

import org.newdawn.slick.Image;

import orig.Creature;
import orig.Item;

public class OnScreenChar {
	Image looks;
	int xPos;
	int yPos;
	public Creature baseCreature;
	final int tileSize = 32;
	
	public OnScreenChar(Image used, int X, int Y, Creature c){
		looks = used;
		xPos = X;
		yPos = Y;
		looks.draw(xPos, yPos);
		baseCreature = c;
	}
	
	public void draw(){
		looks.draw(xPos*tileSize, yPos*tileSize);
	}
	
	//useless
	public void step(int x, int y, int speed){
		if(xPos>x){
			move(0-speed, 0);
		}
		else if(xPos<x){
			move(speed, 0);
		}
		if(yPos>y){
			move(0, 0-speed);
		}
		else if(yPos<y){
			move(0, speed);
		}
	}
	
	public void move(int left, int up){
		this.xPos += left;
		this.yPos += up;
	}
	
	public void pickup(Item i){
		baseCreature.pickup(i);
	}
	
	public ArrayList<Item> getInventory(){
		return baseCreature.getInventory();
	}
	
	public String getEquipped(int i){
		return baseCreature.getEquipped(i);
	}
	
	public int getNumArms(){
		return baseCreature.getNumArms();
	}
	
	public void equip(Item i){
		baseCreature.equip(i);
	}
	
	public void drop(Item i){
		baseCreature.drop(i);
	}
	
	//attack, move, etc, will call the creature's attach stuff, I'll probably also do the same abstraction for inventory at some point...
}
