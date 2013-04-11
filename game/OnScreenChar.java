package game;

import java.io.Serializable;
import java.util.ArrayList;

import org.newdawn.slick.Image;

import orig.Creature;
import orig.DungeonMap;
import orig.Item;
import util.ImageUtil;

public class OnScreenChar implements Serializable {
	transient Image looks;
	String imgName;
	int xPos;
	int yPos;
	int speed;
	double ratioMovement;
	double stackedMovement;
	public Creature baseCreature;
	final int tileSize = 32;
	boolean isPlayer;
	
	public OnScreenChar(int X, int Y, Creature c) {
		this(c.getRName(), X, Y, c);
	}
	
	public OnScreenChar(String imgName, int X, int Y, Creature c){
		this.imgName = imgName;
		xPos = X;
		yPos = Y;
		baseCreature = c;
		speed = 1; //baseCreature.get(bStats.SPEED, sVal.LEVEL)
		draw(xPos, yPos);
		ratioMovement = speed;
		stackedMovement = 0;
		isPlayer = false;
	}
	
	public void draw(int mapX, int mapY){
		if (looks == null)
			looks = ImageUtil.getImage(this.imgName);
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
		dm.removeOnScreenChar(xPos, yPos);
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
		dm.putOnScreenChar(xPos, yPos, this, true);
	}
	
	/**
	 * ONLY DUNGEONMAP SHOULD USE THIS
	 * This is used when the player steps on a door/stairs, to re-adjust their position
	 * @param newX
	 * @param newY
	 */
	public void setPosition(int newX, int newY) {
		this.xPos = newX;
		this.yPos = newY;
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

	public boolean isPlayer() {
		return isPlayer;
	}
	
	public void setAsPlayer() {
		isPlayer = true;
	}

	public int getX() {
		return this.xPos;
	}
	
	public int getY() {
		return this.yPos;
	}
	
	//attack, move, etc, will call the creature's attach stuff, I'll probably also do the same abstraction for inventory at some point...
}
