package orig;

import game.OnScreenChar;

import java.io.Serializable;
import java.util.HashMap;
import java.util.HashSet;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.util.pathfinding.PathFindingContext;
import org.newdawn.slick.util.pathfinding.TileBasedMap;

import util.DungeonMapGenerator.TileData;
import util.DungeonMapGenerator.TileType;
import util.General.Direction;
import util.General.GridPoint;

public class DungeonMap implements TileBasedMap, Serializable { 
	
	private static final long serialVersionUID = -6764222487499645397L;

	//getting around
	int mapID;
	
	//the map's properties
	Square[][] squares;
	int level; //the height, above sea level. 0 is usually the surface
	boolean outside; //true if the ceiling is the sky, false otherwise
	transient Planet planet;
	
	//these only exist so that a quick description can be given of the room, without iterating through the squares array to find the most commmon elements.
	Element wallsbase;
	Element floorsbase;
	
	// this image map is for rendering special details, like doors, stairs, etc?
	// maybe not even needed
	HashMap<TileType, String> extras;
	
	HashSet<GridPoint> doorList;

	private HashSet<GridPoint> stairList;
	
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
		for(int i=(x-radius); i<= x+radius; i++ ){
			for(int j=(y-radius); j<= y+radius; j++){
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
		this.doorList = new HashSet<GridPoint>();
		this.stairList = new HashSet<GridPoint>();
		
		for (int x = 0; x < mapDetails.length; ++x) {
			for (int y = 0; y < mapDetails.length; ++y) {
				// several checks, to decide which types of squares to initialize
				if ((mapDetails[x][y] & TileType.DOOR.flag) != 0) {
					// this is a door
					Door d = new Door(true, this.floorsbase);
					d.setImageName(data.getMap().get(TileType.DOOR));
					// detect the direction.
					d.setDirection(Direction.getDirection(x, y));
					this.doorList.add(new GridPoint(x, y));
					this.squares[x][y] = d;
				} else if ((mapDetails[x][y] & (TileType.UPSTAIRS.flag + TileType.DOWNSTAIRS.flag)) != 0) {
					// there is a stair case here.
					Stairs s = new Stairs(true, this.floorsbase);
					if ((mapDetails[x][y] & TileType.UPSTAIRS.flag) != 0) {
						s.setDirection(Direction.HIGHER);
						s.setImageName(data.getMap().get(TileType.UPSTAIRS));
					} else {
						s.setDirection(Direction.LOWER);
						s.setImageName(data.getMap().get(TileType.DOWNSTAIRS));
					}
					this.stairList.add(new GridPoint(x, y));
					this.squares[x][y] = s;
				} else if ((mapDetails[x][y] & TileType.FLOOR.flag) != 0) {
					// this square is simply a floor.
					// this ordering also means that if a detail location has both FLOOR and WALL flags, it will be a floor
					Square sq = new Square(true, this.floorsbase);
					this.squares[x][y] = sq;
				} else  {
					// this is not anything special, so it must be a wall
					Square sq = new Square(false, this.wallsbase);
					this.squares[x][y] = sq;
				}
			}
		}
		
	}
	
	public DungeonMap(boolean[][] wallFloor, Element wbase, Element fbase) {
		this.wallsbase = wbase;
		this.floorsbase = fbase;
		this.squares = new Square[wallFloor.length][wallFloor[0].length];
		for (int x = 0; x < wallFloor.length; ++x) {
			for (int y = 0; y < wallFloor[x].length; ++y) {
				// might want to look into changing this way of initializing squares
				boolean passable = wallFloor[x][y];
				Element cons = (wallFloor[x][y] ? fbase : wbase);
				Square sq = new Square(passable, cons);
				this.squares[x][y] = sq;
			}
		}
	}
	
	/**
	 * This method attempts to place a creature in a particular square on the grid
	 * @param x X coordinate of location to put creature
	 * @param y Y coordinate of location to put creature
	 * @param c reference to the Creature
	 * @param walking Whether or not we walked onto this square, or were simply placed there.
	 * @return whether the operation was successful or not
	 */
	public boolean putOnScreenChar(int x, int y, OnScreenChar c, boolean walking) {
		if (validateCoordinates(x, y)) {
			GridPoint gp = new GridPoint(x, y);
			if (doorList.contains(gp) && c.isPlayer() && walking) {
				// need to update maps!
				Door door = this.getDoor(gp);
				gp.mirror(squares.length, squares[0].length);
				planet.moveMap(door.getDirection(), c, gp);
				return true;
			} else if (stairList.contains(gp) && c.isPlayer() && walking) {
				Stairs stairs = this.getStairs(gp);
				planet.moveMap(stairs.getDirection(), c, gp);
				return true;
			}
			if (squares[x][y].getOnScreenChar() == null) {
				squares[x][y].setOnScreenChar(c);
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
	public boolean removeOnScreenChar(int x, int y) {
		if (validateCoordinates(x, y)) {
			squares[x][y].setOnScreenChar(null);
			return true;
		}
		return false;
	}
	
	public boolean isPassable(int xCoor, int yCoor) {
		// validate coordinates
		if (!validateCoordinates(xCoor, yCoor))
			return false; // tell caller they can't move off the grid, maybe throw exception
		if (squares[xCoor][yCoor].getOnScreenChar() != null){
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
		for (int x = xLower; x < xUpper; ++x) {
			for (int y = yLower; y < yUpper; ++y) {
				squares[x][y].render(x-xPos, y-yPos, xPos, yPos);
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
	
//prototype Attack function (should just pass to creature/Item at each of the points in attack pattern) 
//remove if it is already implemented - Michael
	public void attack(Attack a) {
		//handle attacks and Attack results
	}

	public HashSet<GridPoint> getDoorList() {
		return this.doorList;
	}

	public Door getDoor(GridPoint nextPt) {
		Square s = this.squares[nextPt.getX()][nextPt.getY()];
		if (s instanceof Door)
			return (Door)s;
		return null;
	}

	public void setPlanet(Planet plan) {
		this.planet = plan;
	}

	public HashSet<GridPoint> getStairList() {
		return this.stairList;
	}

	public Stairs getStairs(GridPoint nextPt) {
		Square s = this.squares[nextPt.getX()][nextPt.getY()];
		if (s instanceof Stairs)
			return (Stairs)s;
		return null;
	}
}
