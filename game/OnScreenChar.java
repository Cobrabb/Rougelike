package game;

import org.newdawn.slick.Image;

import orig.Creature;

public class OnScreenChar {
	Image looks;
	int xPos;
	int yPos;
	int tileSize;
	Creature baseCreature;
	
	public OnScreenChar(Image used, int X, int Y, int tileSize, Creature c){
		looks = used;
		xPos = X;
		yPos = Y;
		looks.draw(xPos, yPos);
		this.tileSize = tileSize;
		baseCreature = c;
	}
	
	public void draw(){
		looks.draw(xPos, yPos);
	}
	
	//useless
	public void step(int x, int y){
		if(xPos>x){
			xPos-=tileSize;
		}
		else if(xPos<x){
			xPos+=tileSize;
		}
		if(yPos>y){
			yPos-=tileSize;
		}
		else if(yPos<y){
			yPos+=tileSize;
		}
	}
	
	//attack, move, etc, will call the creature's attach stuff, I'll probably also do the same abstraction for inventory at some point...
}
