package game;

import org.newdawn.slick.Image;
import org.newdawn.slick.Sound;
public class Enemy {
	Image looks;
	int xPos;
	int yPos;
	Sound roar;
	boolean roared;
	public Enemy(Image used, int X, int Y, Sound roar){
		looks = used;
		xPos = X;
		yPos = Y;
		looks.draw(xPos, yPos);
		this.roar = roar;
		roared = false;
	}
	
	public void draw(){
		looks.draw(xPos, yPos);
	}
	
	public void step(int x, int y){
		if(xPos>x){
			xPos-=16;
		}
		else if(xPos<x){
			xPos+=16;
		}
		if(yPos>y){
			yPos-=16;
		}
		else if(yPos<y){
			yPos+=16;
		}
		if(xPos==x&&yPos==y){
			// added this so we don't keep hearing the roar
			if (!roared) {
				roar.play();
				roared = true;
			}
		} else {
			roared = false;
		}
	}
	
}
