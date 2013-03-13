package orig;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.tiled.TiledMap;
import org.newdawn.slick.util.pathfinding.PathFindingContext;
import org.newdawn.slick.util.pathfinding.TileBasedMap;

public class DungeonMap implements TileBasedMap { //extends TiledMap

	//getting around
	int mapID;
	
	private TiledMap map;
	
	//the map's properties
	Square[][] squares;
	int level; //the height, above sea level. 0 is usually the surface
	boolean outside; //true if the ceiling is the sky, false otherwise
	
	//these only exist so that a quick description can be given of the room, without iterating through the squares array to find the most commmon elements.
	Element wallsbase;
	Element floorsbase;
	
	public DungeonMap(){ //completely random constructor, probably not necessary.
	}
	
	public DungeonMap(Planet p, boolean outer) {
		//for now, there is no random generation
		floorsbase = p.getLand();
		outside = outer;
		if(!outer){
			wallsbase = floorsbase; 
		}
		else{
			
		}
	}
	
	public DungeonMap(String mapPath, Planet p, boolean outer, boolean generate) throws SlickException {
		this(p, outer);
		if (generate)
			mapPath = p.generateMap(mapPath);
		this.map = new TiledMap(mapPath, "maps");
	}
	
	public void render(GameContainer container, StateBasedGame sbg, Graphics g) {
		this.map.render(0, 0);
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
		return map.getHeight();
	}

	@Override
	public int getWidthInTiles() {
		return map.getWidth();
	}

	@Override
	public void pathFinderVisited(int arg0, int arg1) {
	}
	
}
