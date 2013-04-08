package util;

import org.newdawn.slick.Image;

public class TestImageUtil {
	public static void main(String[] args) {
		// note, this unfortunately can't be tested as is, you need to throw this line:
		// TestImageUtil.main(null)
		// into one of the game states for it to do it properly
		// probably should get around to setting up a testing enviro
		ImageUtil.setRelativeFolder("maps");
		ImageUtil.setTPF("tileset_parse.tpf");
		ImageUtil.loadImages();
		String[] images = {"gray stone", "ice", "silver brick", "light brown rock", "light cobblestone", "cobblestone", "sandstone", "dark brown rock"};
		int diff = 0;
		for (String s: images) {
			Image temp = ImageUtil.getImage(s);
			temp.draw(32*diff, 32*diff);
			temp.draw(32*diff, 32*diff + 32);
			temp.draw(32*diff + 32, 32*diff);
			++diff;
		}
	}
}
