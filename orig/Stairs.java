package orig;

import org.newdawn.slick.Image;

import util.General.Direction;
import util.ImageUtil;

public class Stairs extends Square {
	
	private static final long serialVersionUID = -1263166261797377155L;
	private String imgName;
	private Direction d;
	
	public Stairs(boolean pass, Element cons) {
		super(pass, cons);
	}
	
	public void render(int row, int col, int px, int py) {
		if (super.noImage()) {
			if (imgName == null) {
				if (d != null) {
					if (d == Direction.HIGHER)
						super.setImage(ImageUtil.getImage("upstairs"));
					else
						super.setImage(ImageUtil.getImage("downstairs"));
				} else {
					super.setImage(ImageUtil.getImage("door"));
				}
			} else
				super.setImage(ImageUtil.getImage(imgName));
		}
		super.render(row, col, px, py);
	}
	
	public void setImageName(String name) {
		this.imgName = name;
	}
	
	public String getImageName() {
		return this.imgName;
	}
	
	public void setDirection(Direction d) {
		this.d = d;
	}
	
	public Direction getDirection() {
		return this.d;
	}
	
}
