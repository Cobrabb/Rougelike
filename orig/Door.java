package orig;

import org.newdawn.slick.Image;

import util.ImageUtil;

public class Door extends Square {
	
	public Door(boolean pass, Element cons) {
		super(pass, cons);
	}

	public void setImage(Image image) {
		super.setImage(img);
	}
	
	public void render(int row, int col, int px, int py) {
		if (super.noImage()) {
			super.setImage(ImageUtil.getImage("door"));
		}
		super.render(row, col, px, py);
	}
	
}
