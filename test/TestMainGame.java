package test;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

import util.ImageUtil;
import util.MapUtil;

public class TestMainGame extends StateBasedGame{
	public static final int MENUSTATE = 0;
	public static final int MAINGAMESTATE = 1;

	public TestMainGame(){
		super("Space Rougelike");
	}
   
    public void initStatesList(GameContainer gc){
    	this.addState(new TestMenuState(MENUSTATE));
    	this.addState(new TestMainGameState(MAINGAMESTATE));
    }
	public static void main(String[] args) {
		MapUtil.setFolderDirectory("maps");
		MapUtil.setTileSource("test.labelmap");
		MapUtil.setResourceFile("rougelike.tsx");
		ImageUtil.setRelativeFolder("maps");
		ImageUtil.setTPF("tileset_parse.tpf");
		ImageUtil.setTilesetImageFile("ccrgeek_ground_tileset.png");
		MapUtil.setTileWidth(16);
		MapUtil.setTileHeight(16);
		try {
	        AppGameContainer app = new AppGameContainer(new TestMainGame());
	        app.setDisplayMode(800, 608, false);
	        app.start();
	    } catch (SlickException e) {
	        e.printStackTrace();
	    }
    }
}
