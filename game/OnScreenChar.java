package game;

import java.util.ArrayList;

import org.newdawn.slick.Image;

import orig.Creature;
import orig.DungeonMap;
import orig.Item;
import orig.Creature.bStats;
import orig.Creature.sVal;

public class OnScreenChar {
	Image looks;
	int xPos;
	int yPos;
	int speed;
	double ratioMovement;
	double stackedMovement;
	public Creature baseCreature;
	final int tileSize = 32;
	
	public OnScreenChar(Image used, int X, int Y, Creature c){
		looks = used;
		xPos = X;
		yPos = Y;
		baseCreature = c;
		speed = 1; //baseCreature.get(bStats.SPEED, sVal.LEVEL)
		looks.draw(xPos, yPos);
		ratioMovement = speed;
		stackedMovement = 0;
	}
	
	public void draw(int mapX, int mapY){
		looks.draw((xPos-mapX)*tileSize, (yPos-mapY)*tileSize);
	}
	
	public void initializeRatio(int playerSpeed){
		ratioMovement = ((double)speed)/((double)playerSpeed);
	}
	
	//useless
	public void step(int x, int y, DungeonMap dm){
		if(xPos>x){
			move(-1, 0, dm);
		}
		else if(xPos<x){
			move(1, 0, dm);
		}
		if(yPos>y){
			move(0, -1, dm);
		}
		else if(yPos<y){
			move(0, 1, dm);
		}
	}
	
	public void move(int left, int up, DungeonMap dm){
		if(left>1||left<-1||up>1||up<-1) return;
		dm.removeCreature(xPos, yPos);
		stackedMovement = stackedMovement+ratioMovement;
		int movement = (int)stackedMovement;
		if(movement>0){
			stackedMovement = stackedMovement-movement;
			if(dm.isPassable(xPos+left*movement, yPos+up*movement)){
				this.xPos += left*movement;
				this.yPos += up*movement;
			}
			else{
				for(int i=left*movement; i!=0; i-=left){
					if(dm.isPassable(xPos+i,yPos)){
						this.xPos+=i;
						break;
					}
				}
				for(int i=up*movement; i!=0; i-=up){
					if(dm.isPassable(xPos, yPos+i)){
						this.yPos+=i;
						break;
					}
				}
			}
		}
		dm.putCreature(xPos, yPos, baseCreature);
	}
	
	public boolean canMove(int left, int up, DungeonMap dm){
		boolean b = dm.isPassable(xPos+left, yPos+up);
		if(b){
			move(left, up, dm);
		}
		return b;
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
