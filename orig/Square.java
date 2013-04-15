package orig;

import game.OnScreenChar;

import java.io.Serializable;
import java.util.Stack;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;

import util.ImageUtil;

public class Square implements Serializable {

	private static final long serialVersionUID = 6761577085545257185L;
	protected boolean passable; //true if this square is floor type, false if it is wall type
	protected Element consists; //the element that this square is made of
	//MapObject[] contains - It may be useful to store what is "here" in the square class, but I'm thinking not. If it is, we can fill this in later
	protected transient Image img;
	protected String imgName;
	protected OnScreenChar c; //at most one creature may be on a square
	protected boolean seen;
	protected boolean visible;
	protected boolean seeThrough;
	protected Stack<Item> itemStack;
	protected Color tempFade;
	
	public Square(boolean pass, Element cons) {
		this.passable = pass;
		this.consists = cons;
		this.imgName = consists.getName();
		this.seen = false;
		this.seeThrough = pass;
		itemStack = new Stack<Item>();
	}
	
	public void dropItem(Item i) {
		itemStack.push(i);
	}
	
	public boolean containsItem() {
		return !itemStack.isEmpty();
	}
	
	public Item pickUpItem() {
		if (containsItem())
			return itemStack.pop();
		return null;
	}
	
	public boolean isPassable() {
		return (this.passable && (c == null));
	}
	
	public void render(int x, int y, int px, int py, Graphics g) {
		// TODO Auto-generated method stub
		if (noImage()) {
			if (imgName != null) {
				img = ImageUtil.getImage(imgName);
			} else {
				img = ImageUtil.getImage(consists.getName());
			}
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
			} else if (tempFade != null) {
				transparency = tempFade;
				if (!passable)
					transparency = transparency.darker(0.50f);
			}
			img.draw(x*ImageUtil.getTileWidth(), y*ImageUtil.getTileHeight(), transparency);
		}
		if(!itemStack.empty() && visible){
			itemStack.peek().draw(x, y);
		}
		if (visible && c != null) {
			if (tempFade == null)
				c.draw(px,  py, g, Color.white);
			else
				c.draw(px, py, g, tempFade);
		}
	}
	
	public String getImageName() {
		return this.imgName;
	}
	
	public void setImageName(String name) {
		this.imgName = name;
	}
	
	public void setSeeThrough() {
		this.seeThrough = true;
	}
	
	public void setOpaque() {
		this.seeThrough = false;
	}
	
	public void setVisible() {
		this.seen = true;
		this.visible = true;
		this.tempFade = null;
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

	public boolean isSeeThrough() {
		if (c != null)
			return false;
		return this.seeThrough;
	}

	public void setFade(Color color) {
		this.tempFade = color;
	}
}
