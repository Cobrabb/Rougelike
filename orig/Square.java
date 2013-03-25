package orig;

import java.io.Serializable;

import org.newdawn.slick.Color;
import org.newdawn.slick.Image;

import util.ImageUtil;

public class Square implements Serializable {

	private static final long serialVersionUID = 6761577085545257185L;
	boolean passable; //true if this square is floor type, false if it is wall type
	Element consists; //the element that this square is made of
	//MapObject[] contains - It may be useful to store what is "here" in the square class, but I'm thinking not. If it is, we can fill this in later
	Image elementTile;
	public Creature c; //at most one creature may be on a square
	public void render(int row, int col) {
		// TODO Auto-generated method stub
		if (elementTile == null) {
			elementTile = ImageUtil.getImage(consists.getName());
		}
		elementTile.draw(row*ImageUtil.getTileWidth(), col*ImageUtil.getTileHeight(), (passable ? Color.white : Color.darkGray));
	}
	
	
	/*public MapObject getTop() {
		return null; // indicates no items on this Square.
	}*/
	
}
