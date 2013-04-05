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
	protected Image img;
	public Creature c; //at most one creature may be on a square
	public boolean seen;
	public boolean visible;
	
	public Square(boolean pass, Element cons) {
		this.passable = pass;
		this.consists = cons;
		seen = false;
	}
	
	public boolean isPassable() {
		return (this.passable && (c == null));
	}
	
	public void render(int row, int col, int px, int py) {
		// TODO Auto-generated method stub
		if (noImage()) {
			img = ImageUtil.getImage(consists.getName());
		}
		Color transparency = null;
		if (seen) {
			if (passable) {
				transparency = Color.white;
			} else {
				transparency = Color.darkGray;
			}
			if (!visible) { // not visible, then darken more
				transparency = transparency.darker(0.50f);
			}
			img.draw(row*ImageUtil.getTileWidth(), col*ImageUtil.getTileHeight(), transparency);
		}
	}
	
	public void setVisible() {
		this.seen = true;
		this.visible = true;
	}
	
	public void setNonvisible() {
		this.visible = false;
	}

	public void setCreature(Creature cre) {
		this.c = cre;
	}

	public Creature getCreature() {
		return this.c;
	}

	protected void setImage(Image img2) {
		this.img = img2;
	}

	public boolean noImage() {
		return this.img == null;
	}
	
	
	/*public MapObject getTop() {
		return null; // indicates no items on this Square.
	}*/
	
}
