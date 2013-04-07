package orig;

import game.OnScreenChar;

import java.io.Serializable;

import org.newdawn.slick.Color;
import org.newdawn.slick.Image;

import util.ImageUtil;

public class Square implements Serializable {

	private static final long serialVersionUID = 6761577085545257185L;
	protected boolean passable; //true if this square is floor type, false if it is wall type
	protected Element consists; //the element that this square is made of
	//MapObject[] contains - It may be useful to store what is "here" in the square class, but I'm thinking not. If it is, we can fill this in later

	protected transient Image img;
	protected OnScreenChar c; //at most one creature may be on a square
	protected boolean seen;
	protected boolean visible;
	
	public Square(boolean pass, Element cons) {
		this.passable = pass;
		this.consists = cons;
		seen = false;
	}
	
	public boolean isPassable() {
		return (this.passable && (c == null));
	}
	
	public void render(int x, int y, int px, int py) {
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
			img.draw(x*ImageUtil.getTileWidth(), y*ImageUtil.getTileHeight(), transparency);
		}
		if (visible && c != null) {
			c.draw(px,  py);
		}
	}
	
	public void setVisible() {
		this.seen = true;
		this.visible = true;
	}
	
	public void setNonvisible() {
		this.visible = false;
	}


	public void setOnScreenChar(OnScreenChar cre) {
		this.c = cre;
	}

	public OnScreenChar getOnScreenChar() {
		return this.c;
	}

	protected void setImage(Image img2) {
		this.img = img2;
	}

	public boolean noImage() {
		return this.img == null;
	}
}
