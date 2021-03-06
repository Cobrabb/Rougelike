package game;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.state.StateBasedGame;

import util.ImageUtil;
import util.MapUtil;

public class MainGame extends StateBasedGame{
	public static final int MENUSTATE = 0;
	public static final int MAINGAMESTATE = 1;
	public static final int CHARACTERCREATESTATE = 2;
	public static final int WINGAMESTATE = 3;
	
	public static final int sizeX = 1324;
	public static final int sizeY = 672;

	public MainGame(){
		super("Space Rougelike");
	}
   
    public void initStatesList(GameContainer gc){
    	this.addState(new MenuState(MENUSTATE));
    	this.addState(new MainGameState(MAINGAMESTATE));
    	this.addState(new CreateCharacterState(CHARACTERCREATESTATE));
    	this.addState(new WinGameState(WINGAMESTATE));
    }
   public static void main(String[] args) {
	   MapUtil.setFolderDirectory("maps");
		ImageUtil.setRelativeFolder("maps");
		ImageUtil.setTPF("tileset_parse.tpf");
        try {
            AppGameContainer app = new AppGameContainer(new MainGame());
            app.setDisplayMode(sizeX, sizeY, false);
            app.start();
        } catch (SlickException e) {
            e.printStackTrace();
        }
    }
}
