package orig;

import java.io.Serializable;

import org.newdawn.slick.Color;
import org.newdawn.slick.Image;

import util.ImageUtil;

public class Square implements Serializable {

	private static final long serialVersionUID = 6761577085545257185L;
	private boolean passable; //true if this square is floor type, false if it is wall type
	private Element consists; //the element that this square is made of
	//MapObject[] contains - It may be useful to store what is "here" in the square class, but I'm thinking not. If it is, we can fill this in later
	private Image elementTile;
	public Creature c; //at most one creature may be on a square
	public boolean seen;
	
	public Square(boolean pass, Element cons) {
		this.passable = pass;
		this.consists = cons;
		seen = false;
	}
	
	public boolean isPassable() {
		return this.passable;
	}
	
	public void render(int row, int col) {
		// TODO Auto-generated method stub
		if (elementTile == null) {
			elementTile = ImageUtil.getImage(consists.getName());
		}
		if(seen){
			elementTile.draw(row*ImageUtil.getTileWidth(), col*ImageUtil.getTileHeight(), (passable ? Color.white : Color.darkGray));
		}
	}
	
	
	/*public MapObject getTop() {
		return null; // indicates no items on this Square.
	}*/
	
}
