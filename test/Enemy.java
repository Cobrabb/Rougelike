package test;

import org.newdawn.slick.Image;
import org.newdawn.slick.Sound;

import orig.DungeonMap;
public class Enemy {
	Image looks;
	int xPos;
	int yPos;
	Sound roar;
	boolean roared;
	DungeonMap dm;
	public Enemy(Image used, int X, int Y, Sound roar, DungeonMap dmm) {
		looks = used;
		xPos = X;
		yPos = Y;
		looks.draw(xPos, yPos);
		this.roar = roar;
		roared = false;
		this.dm = dmm;
	}
	
	public void draw(){
		looks.draw(xPos, yPos);
	}
	
	public void step(int x, int y){
		int newX = xPos;
		int newY = yPos;
		if(newX>x){
			newX-=16;
		}
		else if(newX<x){
			newX+=16;
		}
		if(newY>y){
			newY-=16;
		}
		else if(newY<y){
			newY+=16;
		}
		// validate newX, newY
		if (dm.isPassable(newX/32, newY/32)) {
			xPos = newX;
			yPos = newY;
		}
		if(xPos == x && yPos == y && !roared){
			roar.play();
			roared = true;
		}
	}
	
}

