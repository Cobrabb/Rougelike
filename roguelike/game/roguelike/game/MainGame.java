package roguelike.game;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.state.StateBasedGame;

public class MainGame extends StateBasedGame{
	public static final int MENUSTATE = 0;
	public static final int MAINGAMESTATE = 1;

	public MainGame(){
		super("Space Rougelike");
	}
   
    public void initStatesList(GameContainer gc){
    	this.addState(new MenuState(MENUSTATE));
    	this.addState(new MainGameState(MAINGAMESTATE));
    }
   public static void main(String[] args) {
        try {
            AppGameContainer app = new AppGameContainer(new MainGame());
            app.setDisplayMode(800, 600, false);
            app.start();
        } catch (SlickException e) {
            e.printStackTrace();
        }
    }
}
