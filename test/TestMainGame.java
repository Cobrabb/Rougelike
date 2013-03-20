package test;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.state.StateBasedGame;

import util.MapWriter;

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
	   MapWriter.setFolderDirectory("maps\\"); //C:\\Users\\Alex\\Documents\\GitHub\\Rougelike\\maps\\
	   MapWriter.setTileSource("test.labelmap");
	   MapWriter.setResourceFile("rougelike.tsx");
	   MapWriter.setTileWidth(16);
	   MapWriter.setTileHeight(16);
        try {
            AppGameContainer app = new AppGameContainer(new TestMainGame());
            app.setDisplayMode(800, 600, false);
            app.start();
        } catch (SlickException e) {
            e.printStackTrace();
        }
    }
}
