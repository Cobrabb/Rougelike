package game;

import org.newdawn.slick.Image;

public class OnScreenChar {
	Image looks;
	int xPos;
	int yPos;
	int tileSize;
	
	public OnScreenChar(Image used, int X, int Y, int tileSize){
		looks = used;
		xPos = X;
		yPos = Y;
		looks.draw(xPos, yPos);
		this.tileSize = tileSize;
	}
	
	public void draw(){
		looks.draw(xPos, yPos);
	}
	
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
}
