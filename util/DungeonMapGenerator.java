package util;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Random;

import orig.DungeonMap;
import orig.Element;
import orig.Planet;
import util.General.Direction;
import util.General.GridPoint;

public final class DungeonMapGenerator implements Serializable {
	
	public enum TileType {
		WALL,
		FLOOR,
		DOOR,
		UPSTAIRS,
		DOWNSTAIRS,
		PLAYERSPAWN,
		MONSTERSPAWN,
		TREASURE,
		TELEPORT,
		OTHER;
		
		public final int flag;
		
		TileType() {
			this.flag = 1 << this.ordinal();
		}
	}
	
	public class TileData {
		private Element wall;
		private Element floor;
		// tiletype maps to the image name in ImageUtil
		private HashMap<TileType, String> map;
		
		void setWall(Element e) {
			this.wall = e;
		}
		
		public Element getWall() {
			return this.wall;
		}
		
		void setFloor(Element e) {
			this.floor = e;
		}
		
		public Element getFloor() {
			return this.floor;
		}
		
		void setMap(HashMap<TileType, String> hm) {
			this.map = hm;
		}
		
		public HashMap<TileType, String> getMap() {
			return this.map;
		}
	}
	
	Random r = new Random();
	Element floorBase;
	Element wallBase;
	
	public DungeonMapGenerator(Planet p) {
		ImageUtil.loadImages();
		if (p.getElementCount() > 1) {
			floorBase = p.getElement(0);
			wallBase = p.getElement(1);
		} else {
			floorBase = new Element();
			wallBase = floorBase;
		}
	}
	
	public String generateBlankSquareMap(String mapName, int width, int height) {
		return this.generateBlankSquareMap(mapName, width, height, 15, 20, 3, width/3, 2, 30);
	}
	
	/**
	 * This generates a map with doors and stairs in specific locations to match nearby maps
	 * @param mapName The name of this dungeon
	 * @param width Its width
	 * @param height Its height
	 * @param doorLocations A GridPoint[], indicating locations of doors to connect to.
	 * @param stairs A GridPoint[], indicating locations of stairs to connect to
	 * 				negative coordinates indicate "LOWER" while positive indicate "HIGHER"
	 * @return The file path of this dungeon
	 */
	public String generateConnectedSquareMap(String mapName, int width, int height, GridPoint[] doorLocations, HashSet<Direction> forbidden, GridPoint[] stairs) {
		// May be the case that doors do not connect to the map at all, skipping an area?
		// Might be hard to generate a map that satisfies all the constraints.  eh, it'll be fine.
		boolean[][] map = this.generateSquareRooms(width, height, 15, 20, 4, width/4, 3, 30);
		int[][] details = new int[map.length][map[0].length];
		// simple solution! yay for code reuse!
		// for the stair gridpoints, plop a 3x3 block of floors around it
		this.generateStairs(details, map, forbidden);
		for (GridPoint s: stairs) {
			boolean higher = true;
			if (s.getX() < 0) {
				s.flipSigns();
				higher = false;
			}
			Direction d = (higher ? Direction.HIGHER : Direction.LOWER);
			addStairs(details, map, s, d);
			if (!higher)
				s.flipSigns();
		}
		// now connect the map up
		DungeonMapGenerator.minConnect(map);
		// for the doors, we need to go through, find the directions they represent and create add to
		// the forbidden hashset
		for (GridPoint d: doorLocations) {
			Direction dir = Direction.getDirection(d);
			forbidden.add(dir);
		}
		this.generateDoors(details, map, forbidden);
		// for the doors, it'll be fun, we'll add them, then re-run the connection algo
		for (GridPoint d: doorLocations) {
			Direction dir = Direction.getDirection(d);
			int RC = d.getY() * dir.getX() + d.getX() * dir.getY();
			if (RC < 0) {
				RC = -RC;
			}
			//System.err.printf("Trying to add door Dir: %s, RC: %s\n", dir, RC);
			this.addDoor(details, map, dir, RC);
			DungeonMapGenerator.minConnect(map);
		}
		// TODO: do some stairs stuff
		//System.err.printf("This is what the map %s looks like:\n", mapName);
		DungeonMapGenerator.convertToDetail(details, map);
		TileData data = this.getTileData();
		DungeonMap dm = new DungeonMap(details, data);
		return MapUtil.writeMap(mapName, dm);
	}
	
	/**
	 * make a map array that hold double array of booleans which represents where we will place tiles
	 * initially false = wall, true = tiles
	 * 
	 * Returns the mapPath where the map has been generated to.
	 */
	public String generateBlankSquareMap(
			String mapName, int width, int height, int minRoom, int maxRoom, int minDim, int maxDim, int dimRange,
			int retryCount)	{
		boolean map[][] = generateSquareRooms(width, height, minRoom, maxRoom, minDim, maxDim, dimRange, retryCount);
		
		DungeonMapGenerator.minConnect(map);
		int[][] mapDetails = this.generateMapDetails(map);
		//DungeonMap dm = new DungeonMap(map, this.wallBase, this.floorBase);
		TileData data = this.getTileData();
		DungeonMap dm = new DungeonMap(mapDetails, data);
		return MapUtil.writeMap(mapName, dm);
	}
	
	private boolean[][] generateSquareRooms(int width, int height,
			int minRoom, int maxRoom, int minDim, int maxDim, int dimRange,
			int retryCount) {
		boolean map[][] = new boolean[width][height];
		int numRoom = r.nextInt(maxRoom - minRoom + 1) + minRoom;
		
		//choose random row choose random column
		int xRand; // location of top left corner of room
		int yRand;
		int roomWidth;
		int roomHeight;
		int failure;
		for(int i = 0; i < numRoom; i++)
		{
			roomWidth = r.nextInt(maxDim - minDim + 1) + minDim;
			roomHeight = roomWidth + (r.nextInt(dimRange*2 + 1) - dimRange); // [-dimRange, dimRange]
			xRand = r.nextInt(width - roomWidth - 1);
			yRand = r.nextInt(height - roomHeight - 1);
			failure = 0;
			boolean okay = true;
			while (okay && failure < retryCount) {
		OUTER:	for (int xCheck = xRand; xCheck < xRand + roomWidth+2; ++xCheck) {
					for (int yCheck = yRand; yCheck < yRand + roomHeight+2; ++yCheck) {
						if (map[xCheck][yCheck]) {
							++failure;
							okay = false;
							break OUTER;
						}
					}
				}
				if (okay) {
					for (int xCheck = xRand+1; xCheck < xRand + roomWidth+1; ++xCheck) {
						for (int yCheck = yRand+1; yCheck < yRand + roomHeight+1; ++yCheck) {
							map[xCheck][yCheck] = true;
						}
					}
					okay = false;
				} else {
					okay = true;
				}
			}
		}
		return map;
	}

	private TileData getTileData() {
		TileData td = new TileData();
		HashMap<TileType, String> map = new HashMap<TileType, String>();
		td.setMap(map);
		
		td.setFloor(this.floorBase);
		td.setWall(this.wallBase);
		map.put(TileType.DOOR, "door");
		map.put(TileType.UPSTAIRS, "upstairs");
		map.put(TileType.DOWNSTAIRS, "downstairs");
		
		return td;
	}

	/**
	 * This method generates the additional information for a map
	 * such as doors, stairs, spawn points, treasures, etc
	 * @param map
	 * @return
	 */
	// TODO: finish this method
	private int[][] generateMapDetails(boolean[][] map) {
		int[][] ret = new int[map.length][map[0].length];
		this.generateDoors(ret, map, new HashSet<Direction>());
		this.generateStairs(ret, map, new HashSet<Direction>());
		this.generateSpawnPoints(ret, map);
		DungeonMapGenerator.convertUpstairsToPlayerSpawn(ret, map);
		DungeonMapGenerator.convertToDetail(ret, map);
		return ret;
	}
	
	/**
	 * Warning: This should only really be used by the blank map generator
	 * Or, don't call it when you need things to be connected.
	 * @param ret
	 * @param map
	 */
	private static void convertUpstairsToPlayerSpawn(int[][] ret, boolean[][] map) {
		for (int x = 0; x < ret.length; ++x) {
			for (int y = 0; y < ret[x].length; ++y) {
				if ( (ret[x][y] & TileType.UPSTAIRS.flag) != 0 ) {
					ret[x][y] -= TileType.UPSTAIRS.flag;
					ret[x][y] |= TileType.PLAYERSPAWN.flag;
				}
			}
		}
		
	}

	private static void convertToDetail(int[][] ret, boolean[][] map) {
		for (int r = 0; r < map.length; ++r) {
			for (int c = 0; c < map[r].length; ++c) {
				if (map[r][c]) {
					ret[r][c] |= TileType.FLOOR.flag;
				} else {
					ret[r][c] |= TileType.WALL.flag;
				}
			}
		}
		/*for (int y = 0; y < map[0].length; ++y) {
			for (int x = 0; x < map.length; ++x) {
				System.err.printf("%d", (map[x][y] ? 0 : 1));
			}
			System.err.println();
		}*/
	}

	/**
	 * This method will generate 
	 * @param details
	 * @param map
	 */
	private void generateSpawnPoints(int[][] details, boolean[][] map) {
		// TODO Auto-generated method stub
		
	}

	private void generateStairs(int[][] details, boolean[][] map, HashSet<Direction> forbidden) {
		HashSet<Direction> possible = new HashSet<Direction>();
		if (!forbidden.contains(Direction.HIGHER))
			possible.add(Direction.HIGHER);
		if (!forbidden.contains(Direction.LOWER))
			possible.add(Direction.LOWER);
		
		int[] limits = {0, 0, 100};
		// int[] limits = {90, 95, 100};
		int stairTypes = 0;
		int check = r.nextInt(100);
		for (int i = 0; i < limits.length; ++i) {
			if (check < limits[i]) {
				stairTypes = i;
				break;
			}
		}
		
		stairTypes = Math.min(stairTypes, possible.size());
		if (stairTypes == 0)
			return; // nothing to generate.
		

		int[] count = new int[stairTypes];
		for (int i = 0; i < stairTypes; ++i)
			count[i] = r.nextInt(2) + 1; // 1-2
		Direction[] dirs = new Direction[possible.size()];
		int idx = 0;
		Iterator<Direction> it = possible.iterator();
		while (it.hasNext()) {
			dirs[idx++] = it.next();
		}
		if (r.nextBoolean() && dirs.length == 2) {
			Direction t = dirs[0];
			dirs[0] = dirs[1];
			dirs[1] = t;
		}
		for (int i = 0; i < count.length; ++i) {
			for (int j = 0; j < count[i]; ++j) {
				addStairs(details, map, null, dirs[i]);
			}
		}
	}
	
	/**
	 * This method will add a staircase to the given map
	 * If loc != null, it will add the staircase at loc
	 * The staircase will be within a 3x3 block of floor, at least
	 * @param details The map details
	 * @param map The map wall/floor plans
	 * @param loc A possible requested location
	 * @param upDown The direction the stair goes: HIGHER or LOWER
	 */
	private void addStairs(int[][] details, boolean[][] map, GridPoint loc, Direction upDown) {
		TileType type = null;
		switch (upDown) {
		case HIGHER:
			type = TileType.UPSTAIRS;
			break;
		case LOWER:
			type = TileType.DOWNSTAIRS;
			break;
		default:
			throw new RuntimeException("Bad direction " + upDown + " passed to addStairs method.");
		}
		// if loc is not null, our job is easy, otherwise we need to find a good place
		if (loc == null) {
			// since loc was null, we need to determine a good place to put the staircase
			// algo: randomly choose x, y on grid, then iterate right, down until we find a
			// place with at least 3 nearby squares are floor tiles
			// choose a place that is at least 2 squares from the wall
			int randX = r.nextInt(map.length-4)+2;
			int randY = r.nextInt(map[0].length-4)+2;
			boolean done = false;
			while (!done) {
				// check location
				int count = 0; // count adjacent free spaces
				int badCount = 0; // count nearby stairs
				for (int x = randX-1; x <= randX+1; ++x) {
					for (int y = randY-1; y <= randY+1; ++y) {
						if (map[x][y])
							++count;
						if ((details[x][y] & (TileType.UPSTAIRS.flag + TileType.DOWNSTAIRS.flag)) != 0)  {
							++badCount;
						}
					}
				}
				if (map[randX][randY])
					--count;
				if (count >= 3 && badCount == 0) { // good location
					done = true;
				} else {
					// not good
					++randX;
					if (randX == map.length-2) {
						randX = 2;
						++randY;
						if (randY == map[0].length-2) {
							randY = 2;
						}
					}
				}
			}
			loc = new GridPoint(randX, randY);
		}
		int xCen = loc.getX();
		int yCen = loc.getY();
		details[xCen][yCen] |= type.flag;
		// at least make sure it is passable
		for (int x = xCen-1; x <= xCen+1; ++x) {
			for (int y = yCen-1; y <= yCen+1; ++y) {
				//details[x][y] |= TileType.FLOOR.flag;
				map[x][y] = true;
			}
		}
	}
	
	/**
	 * This method adds to the int[][] details locations of doors
	 * It arbitrarily decides where to add doors
	 * @param details The unfinished map details
	 * @param map The boolean map of walls, floor
	 * @param forbidden The directions which cannot have a door
	 */
	private void generateDoors(int[][] details, boolean[][] map, HashSet<Direction> forbidden) {
		// randomly decide:
		// 1) How many directions there will be doors
		// 2) How many doors in each direction
		
		// 1) How many directions:
		// 0: 40%, 1: 35%, 2: 15%, 3: 7%, 4: 3%
		int[] limits = {0, 0, 0, 0, 100};
		int check = r.nextInt(100);
		int numDirections = 0;
		for (int i = 0; i < limits.length; ++i) {
			if (check < limits[i]) {
				numDirections = i;
				break;
			}
		}
		// limit by forbidden
		Direction[] allDirs = {Direction.LEFT, Direction.RIGHT, Direction.UP, Direction.DOWN};
		ArrayList<Direction> temp = new ArrayList<Direction>();
		for (Direction d: allDirs) {
			if (!forbidden.contains(d)) {
				temp.add(d);
			}
		}
		Direction[] dirs = new Direction[temp.size()];
		for (int i = 0; i < dirs.length; ++i)
			dirs[i] = temp.get(i);
		numDirections = Math.min(numDirections, dirs.length);
		
		// randomly shuffle the directions
		for (int i = 0; i < dirs.length; ++i) {
			int swapIdx = r.nextInt(dirs.length-i) + i;
			Direction t = dirs[swapIdx];
			dirs[swapIdx] = dirs[i];
			dirs[i] = t;
		}
		
		// 2.a) How many doors
		// the first 'numDirections' elements of dirs are the directions that the map will extend.
		int doorCount = r.nextInt(numDirections+1) + numDirections;
		
		// 2.b) Which directions
		for (int i = 0; i < doorCount; ++i) {
			if (i < numDirections) {
				addDoor(details, map, dirs[i], -1);
			} else {
				int randomDir = r.nextInt(numDirections);
				addDoor(details, map, dirs[randomDir], -1);
			}
		}
	}
	
	/**
	 * Find the closest point to the wall in that direction, add the door to details,
	 * and add floor tiles to the map.
	 * @param details This is what will be modified and used to produce the actual map
	 * @param map	  This is partially old stuff, true indicates floor, false wall
	 * @param d		  This is the direction we will add the door to
	 * @param RC	  This is a variable which offers the option of, if -1, choosing a random row/col
	 * 				  in the selected direction that is closest to the wall
	 * 				  If not -1, then it will add a door in that selected row/col = RC
	 */
	private void addDoor(int[][] details, boolean[][] map, Direction d, int RC) {
		int minDist = Math.max(map.length, map[0].length);
		ArrayList<Integer> rowcol = new ArrayList<Integer>();
		int innerStart = 0;
		int outerMax = 0;
		int innerChange = 0;
		int innerMax = 0;
		int[] reindex = new int[2];
		// set up general variables.
		switch (d) {
		case UP:
		case DOWN:
			// setup specific to left, right
			switch(d) {
			case UP:
				innerStart = 1;
				innerChange = 1;
				break;
			case DOWN:
				innerStart = map[0].length-2;
				innerChange = -1;
			}
			// setup applies to both left and right
			outerMax = map.length-1;
			innerMax = map[0].length-1;
			reindex[0] = 1;
			reindex[1] = 0;
			break;
		case LEFT:
		case RIGHT:
			// setup specific to up, down
			switch (d) {
			case LEFT:
				innerStart = 1;
				innerChange = 1;
				break;
			case RIGHT:
				innerStart = map.length-2;
				innerChange = -1;
			}
			// setup applies to both up and down
			outerMax = map[0].length-1;
			innerMax = map.length-1;
			reindex[0] = 0;
			reindex[1] = 1;
			break;
		default:
			throw new RuntimeException("The addDoor method was called with an inappropriate direction: " + d.toString());
		}
		// search for the rows, columns closest to the selected direction.
		if (RC == -1) { // only do this search if we haven't passed in such a variable
			for (int outer = 1; outer < outerMax ; ++outer) {
				int tempDist = 0;
				for (int inner = innerStart; inner < innerMax && inner >= 1; inner += innerChange) {
					int row = outer*reindex[0] + inner*reindex[1];
					int col = inner*reindex[0] + outer*reindex[1];
					if (!map[row][col]) { // wall
						++tempDist;
					} else { // floor
						break;
					}
				}
				// if we've found a row/col that is closer to the wall, reset the minDist
				// and reset the stored row/cols which are that close to the wall.
				if (tempDist < minDist) {
					minDist = tempDist;
					rowcol.clear();
					rowcol.add(outer);
				}
				// if we are equally close, then add this row/col to the list
				else if (tempDist == minDist) {
					rowcol.add(outer);
				}
			}
		}
		int idx = RC;
		// after we have the list, randomly choose one of the rows
		// TODO: this can "add" a door at a location where
		// a door already exists.
		if (idx == -1) { // only do this option if nothing was passed in
			idx = r.nextInt(rowcol.size());
			idx = rowcol.get(idx);
		}
		// now that we have the selected row, add a door there, and floor.
		int row, col;
		for (int inner = innerStart - innerChange; inner < innerMax+1 && inner >= 0; inner += innerChange) {
			col = idx*reindex[0] + inner*reindex[1];
			row = idx*reindex[1] + inner*reindex[0];
			// put a door at the very edge of the map, it connects to other maps
			if (inner == (innerStart - innerChange)) {
				details[col][row] |= TileType.DOOR.flag;
			}
			if (map[col][row]) // stop once we have reached the rest of the map
				break;
			map[col][row] = true;
		}
	}

	private static void minConnect(boolean[][] map) {
		// does minimum spanning tree algo, treating connected map rooms as one location.
		
		int componentCount = 2;
		while (componentCount != 1) {
			
			componentCount = 0;
			int[][] visited = new int[map.length][map[0].length];
			Queue<Pair> queue = new LinkedList<Pair>();
			ArrayList<ArrayList<Pair>> components = new ArrayList<ArrayList<Pair>>();
			ArrayList<Pair> centers = new ArrayList<Pair>();
			
			components.add(new ArrayList<Pair>()); // so that it can be 1-indexed
			centers.add(new Pair(-1, -1)); // so that it can be 1-indexed
			int[] dx = {-1, 1, 0, 0};
			int[] dy = {0, 0, -1, 1};
			
			for (int row = 0; row < map.length; ++row) {
				for (int col = 0; col < map[row].length; ++col) {
					if (map[row][col] && visited[row][col] == 0) {
						++componentCount;
						queue.offer(new Pair(row, col));
						visited[row][col] = componentCount;
					} else {
						continue;
					}
					Pair curCenter = new Pair(row, col);
					ArrayList<Pair> curComp = new ArrayList<Pair>();
					centers.add(curCenter);
					curComp.add(curCenter);
					components.add(curComp);
					while (!queue.isEmpty()) {
						Pair cur = queue.poll();
						for (int i = 0; i < dx.length; ++i) {
							int nextX = cur.x + dx[i];
							int nextY = cur.y + dy[i];
							// validate move
							if (!(nextX >= 0 && nextX < map.length && nextY >= 0 && nextY < map[0].length))
								continue;
							// move is valid
							if (map[nextX][nextY] && visited[nextX][nextY] == 0) {
								visited[nextX][nextY] = componentCount;
								Pair nextLoc = new Pair(nextX, nextY);
								queue.offer(nextLoc);
								curCenter.x += nextX;
								curCenter.y += nextY;
								curComp.add(nextLoc);
							}
						}
					}
					curCenter.x /= curComp.size();
					curCenter.y /= curComp.size();
				}
			}
			if (componentCount == 1)
				continue;
			// found components.
			// connect components via approx. min span tree
			// use the centers as a single point of location for components (heuristic)
			// distance will be Manhattan distance.
			
			// it'll be done in a slightly strange way: we will only connect the two closest components
			// then, because we've added some path, we will recalculate?
			
			Pair com = new Pair(-1, -1);
			com.data = -1;
			for (int one = 1; one <= componentCount; ++one) {
				for (int two = one+1; two <= componentCount; ++two) {
					int dist = centers.get(one).distance(centers.get(two));
					if (com.data == -1 || dist < com.data) {
						com.data = dist;
						com.x = one;
						com.y = two;
					}
				}
			}
			
			// connect the components.
			// A* with backtracking
			Pair[][] grid = new Pair[map.length][map[0].length];
			int[][] prev = new int[map.length][map[0].length];
			for (int[] i: prev)
				Arrays.fill(i, -1);
			PriorityQueue<Pair> points = new PriorityQueue<Pair>();
			int moveCost = 4;
			// iterate over all points in components "com.x" (a number)
			for (int i = 0; i < components.get(com.x).size(); ++i) {
				Pair p = components.get(com.x).get(i);
				// assign the data value the heuristic = distance to the center of component "com.y"
				p.data = p.distance(centers.get(com.y));
				// add to grid
				grid[p.x][p.y]= p; 
				// add to priority queue
				points.add(p);
			}
			Pair ret = null;
			while (!points.isEmpty()) {
				Pair cur = points.poll();
				// check if we've reached the component com.y
				// if so, we are done
				if (visited[cur.x][cur.y] == com.y) {
					ret = cur;
					break;
				}
				// try going up,down,left,right
				for (int i = 0; i < dx.length; ++i) {
					int nextX = cur.x + dx[i];
					int nextY = cur.y + dy[i];
					// validate
					if (!(nextX >= 0 && nextX < grid.length && nextY >= 0 && nextY < grid.length)){
						continue;
					}
					// validated - assume nextX, nextY are good
					Pair next = new Pair(nextX, nextY);
					next.data = next.distance(centers.get(com.y)) + cur.data + moveCost;
					if (grid[nextX][nextY] == null || next.compareTo(grid[nextX][nextY]) < 0) {
						grid[nextX][nextY] = next;
						prev[nextX][nextY] = i;
						points.add(next);
					}
				}
			}
			
			// finally, we've found the path, now to draw it into map.
			while (ret != null) {
				map[ret.x][ret.y] = true;
				int oldX = ret.x;
				int oldY = ret.y;
				if (prev[oldX][oldY] == -1)
					break;
				ret.x -= dx[prev[oldX][oldY]];
				ret.y -= dy[prev[oldX][oldY]];
			}
		}
	}
	
	private static class Pair implements Comparable<Pair> {
		int x;
		int y;
		int data;
		
		public Pair(int xx, int yy) {
			x = xx;
			y = yy;
			data = 0;
		}

		public int distance(Pair p) {
			return (x - p.x)*(x - p.x) + (y - p.y)*(y - p.y);
		}

		@Override
		public int compareTo(Pair p) {
			return data - p.data;
		}
	}
}