package orig;

import org.newdawn.slick.Image;

import util.General.Direction;
import util.ImageUtil;

public class Door extends Square {
	
	private static final long serialVersionUID = -1704259244264164756L;
	private String imgName;
	private Direction d;
	
	public Door(boolean pass, Element cons) {
		super(pass, cons);
	}
	
	public void render(int row, int col, int px, int py) {
		if (super.noImage()) {
			if (imgName == null)
				super.setImage(ImageUtil.getImage("door"));
			else
				super.setImage(ImageUtil.getImage(imgName));
		}
		super.render(row, col, px, py);
	}
	
	public void setDirection(Direction d) {
		this.d = d;
	}
	
	public Direction getDirection() {
		return this.d;
	}
	
}
