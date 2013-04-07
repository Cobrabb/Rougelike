package game;

import org.newdawn.slick.Image;

import orig.Creature;
import orig.DungeonMap;

public class Player extends OnScreenChar{

	public Player(String used, int X, int Y, Creature c) {
		super(used, X, Y, c);
	}
	
	public boolean canMove(int left, int up, DungeonMap dm){
		return dm.isPassable(xPos+left, yPos+up);
	}

}
