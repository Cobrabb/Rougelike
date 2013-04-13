package orig;

import org.newdawn.slick.Graphics;

import util.ImageUtil;

public class Teleporter extends Square {

	private static final long serialVersionUID = 8210150193412212325L;
	private int[] toLoc;
	
	public Teleporter(boolean pass, Element cons) {
		super(pass, cons);
	}
	
	public void setTeleport(int relX, int relY) {
		this.toLoc = new int[] {relX, relY};
	}
	
	public int[] teleport() {
		// maybe should make a copy so it can't be modified
		return this.toLoc;
	}
	
	public void render(int row, int col, int px, int py, Graphics g) {
		if (img == null) {
			img = ImageUtil.getImage("teleporter");
		}
		super.render(row,  col, px, py, g);
		if (visible) {
			img.draw(row*ImageUtil.getTileWidth(), col*ImageUtil.getTileHeight());
		}
	}
}
