package orig;

import game.OnScreenChar;

import java.io.Serializable;
import java.util.HashMap;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.util.pathfinding.PathFindingContext;
import org.newdawn.slick.util.pathfinding.TileBasedMap;

import util.DungeonMapGenerator.TileData;
import util.DungeonMapGenerator.TileType;

public class DungeonMap implements TileBasedMap, Serializable { 
	
	private static final long serialVersionUID = -6764222487499645397L;

	//getting around
	int mapID;
	
	//the map's properties
	Square[][] squares;
	int level; //the height, above sea level. 0 is usually the surface
	boolean outside; //true if the ceiling is the sky, false otherwise
	Planet planet;
	
	//these only exist so that a quick description can be given of the room, without iterating through the squares array to find the most commmon elements.
	Element wallsbase;
	Element floorsbase;
	
	// this image map is for rendering special details, like doors, stairs, etc?
	HashMap<TileType, Image> extras;
	
	public DungeonMap(){ //completely random constructor, probably not necessary.
	}
	
	public DungeonMap(Planet p, boolean outer) {
		//for now, there is no random generation
		this.planet = p;
		floorsbase = p.getLand();
		outside = outer;
		if(!outer){
			wallsbase = floorsbase; 
		}
		else{
			
		}
	}
	
	public DungeonMap(String mapPath, Planet p, boolean outer) {
		this(p, outer);
	}
	
	public void reveal(int radius, int x, int y){
		for(Square[] sq: squares) {
			for(Square s: sq) {
				s.setNonvisible();
			}
		}
		for(int i=(x-radius); i<x+radius; i++ ){
			for(int j=(y-radius); j<y+radius; j++){
				if(validateCoordinates(i,j)){
					squares[i][j].setVisible();
				}
			}
		}
	}
	
	public DungeonMap(int[][] mapDetails, TileData data) {
		this.wallsbase = data.getWall();
		this.floorsbase = data.getFloor();
		this.squares = new Square[mapDetails.length][mapDetails[0].length];
		for (int row = 0; row < mapDetails.length; ++row) {
			for (int col = 0; col < mapDetails.length; ++col) {
				// several checks, to decide which types of squares to initialize
				if ((mapDetails[row][col] & TileType.WALL.flag) != 0) {
					// this square is simply a wall.
					Square sq = new Square(false, this.wallsbase);
					this.squares[row][col] = sq;
					continue;
				} else if ((mapDetails[row][col] & TileType.DOOR.flag) != 0) {
					// this is a door
					Door d = new Door(false, this.floorsbase);
					//d.setImage(data.getMap().get(TileType.DOOR));
					this.squares[row][col] = d;
					continue;
				}
				// not a wall, so it is a floor. but it could be something special
				// for now, just a floor
				Square sq = new Square(true, this.floorsbase);
				this.squares[row][col] = sq;
			}
		}
		
	}
	
	public DungeonMap(boolean[][] wallFloor, Element wbase, Element fbase) {
		this.wallsbase = wbase;
		this.floorsbase = fbase;
		this.squares = new Square[wallFloor.length][wallFloor[0].length];
		for (int row = 0; row < wallFloor.length; ++row) {
			for (int col = 0; col < wallFloor[row].length; ++col) {
				// might want to look into changing this way of initializing squares
				boolean passable = wallFloor[row][col];
				Element cons = (wallFloor[row][col] ? fbase : wbase);
				Square sq = new Square(passable, cons);
				this.squares[row][col] = sq;
			}
		}
	}
	
	/**
	 * This method attempts to place a creature in a particular square on the grid
	 * @param x X coordinate of location to put creature
	 * @param y Y coordinate of location to put creature
	 * @param c reference to the Creature
	 * @return whether the operation was successful or not
	 */
	public boolean putCreature(int x, int y, OnScreenChar c) {
		if (validateCoordinates(x, y)) {
			if (squares[x][y].getCreature() == null) {
				squares[x][y].setCreature(c);
				return true;
			}
		}
		return false;
	}
	
	/**
	 * This method attempts to remove a creature from a particular square
	 * @param x X coordinate on the grid
	 * @param y Y coordinate on the grid
	 * @return successful - meaning whether the x,y coordinates were valid
	 */
	public boolean removeCreature(int x, int y) {
		if (validateCoordinates(x, y)) {
			squares[x][y].setCreature(null);
			return true;
		}
		return false;
	}
	
	public boolean isPassable(int xCoor, int yCoor) {
		// validate coordinates
		if (!validateCoordinates(xCoor, yCoor))
			return false; // tell caller they can't move off the grid, maybe throw exception
		if (squares[xCoor][yCoor].getCreature() != null){
			return false; // cannot walk there if another creature is already there
		}
		return squares[xCoor][yCoor].isPassable();
	}

	private boolean validateCoordinates(int xCoor, int yCoor) {
		return (xCoor >= 0 && xCoor < squares.length && yCoor >= 0 && yCoor < squares[0].length);
	}

	public void render(GameContainer container, StateBasedGame sbg, Graphics g, int xPos, int yPos, int screenX, int screenY) {
		int xUpper = (xPos+screenX > squares.length) ? squares.length : xPos+screenX;
		int yUpper = (yPos+screenY > squares[0].length) ? squares[0].length : yPos+screenY;
		int xLower = (xPos < 0) ? 0 : xPos;
		int yLower = (yPos < 0) ? 0 : yPos;
		for (int row = xLower; row < xUpper; ++row) {
			for (int col = yLower; col < yUpper; ++col) {
				squares[row][col].render(row-xPos, col-yPos, xPos, yPos);
			}
		}
	}

	@Override
	public boolean blocked(PathFindingContext arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public float getCost(PathFindingContext arg0, int arg1, int arg2) {
		return 0;
	}

	@Override
	public int getHeightInTiles() {
		return squares[0].length;
	}

	public int getWidthInTiles() {
		return squares.length;
	}

	@Override
	public void pathFinderVisited(int arg0, int arg1) {
	}
	
//prototype Attack function (should just pass to creature at each of the points in attack pattern) 
	public void attack(Attack a) {
		//handle attacks and Attack results
	}
	
}
