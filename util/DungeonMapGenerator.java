package util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Random;

import org.newdawn.slick.Image;

import orig.DungeonMap;
import orig.Element;
import orig.Planet;
import util.General.Direction;

public final class DungeonMapGenerator {
	
	public enum TileType {
		WALL,
		FLOOR,
		DOOR,
		STAIRS,
		SPAWNPOINT,
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
		private HashMap<TileType, Image> map;
		
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
		
		void setMap(HashMap<TileType, Image> hm) {
			this.map = hm;
		}
		
		public HashMap<TileType, Image> getMap() {
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
	 * make a map array that hold double array of booleans which represents where we will place tiles
	 * initially false = wall, true = tiles
	 * 
	 * Returns the mapPath where the map has been generated to.
	 */
	public String generateBlankSquareMap(
			String mapName, int width, int height, int minRoom, int maxRoom, int minDim, int maxDim, int dimRange,
			int retryCount)	{
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
		DungeonMapGenerator.minConnect(map);
		int[][] mapDetails = this.generateMapDetails(map);
		//DungeonMap dm = new DungeonMap(map, this.wallBase, this.floorBase);
		TileData data = this.getTileData();
		DungeonMap dm = new DungeonMap(mapDetails, data);
		return MapUtil.writeMap(mapName, dm);
	}
	
	private TileData getTileData() {
		TileData td = new TileData();
		HashMap<TileType, Image> map = new HashMap<>();
		td.setMap(map);
		
		td.setFloor(this.floorBase);
		td.setWall(this.wallBase);
		map.put(TileType.DOOR, ImageUtil.getImage("door"));
		
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
		this.generateDoors(ret, map);
		this.generateStairs(ret, map);
		this.generateSpawnPoints(ret, map);
		for (int r = 0; r < map.length; ++r) {
			for (int c = 0; c < map[r].length; ++c) {
				if (map[r][c]) {
					ret[r][c] |= TileType.FLOOR.flag;
				} else {
					ret[r][c] |= TileType.WALL.flag;
				}
			}
		}
		return ret;
	}

	private void generateSpawnPoints(int[][] details, boolean[][] map) {
		// TODO Auto-generated method stub
		
	}

	private void generateStairs(int[][] details, boolean[][] map) {
		// TODO Auto-generated method stub
		
	}
	
	/**
	 * This method adds to the int[][] details locations of doors
	 * It arbitrarily decides where to add doors
	 * @param details The unfinished map details
	 * @param map The boolean map of walls, floor
	 */
	private void generateDoors(int[][] details, boolean[][] map) {
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
		Direction[] dirs = {Direction.LEFT, Direction.RIGHT, Direction.UP, Direction.DOWN};
		for (int i = 0; i < dirs.length; ++i) {
			int swapIdx = r.nextInt(dirs.length-i) + i;
			Direction temp = dirs[swapIdx];
			dirs[swapIdx] = dirs[i];
			dirs[i] = temp;
		}
		
		// 2.a) How many doors
		// the first 'numDirections' elements of dirs are the directions that the map will extend.
		int doorCount = r.nextInt(numDirections+1) + numDirections;
		
		// 2.b) Which directions
		for (int i = 0; i < doorCount; ++i) {
			if (i < numDirections) {
				addDoor(details, map, dirs[i]);
			} else {
				int randomDir = r.nextInt(numDirections);
				addDoor(details, map, dirs[randomDir]);
			}
		}
	}
	
	/**
	 * Find the closest point to the wall in that direction, add the door to details,
	 * and add floor tiles to the map.
	 * @param details
	 * @param map
	 * @param d
	 */
	private void addDoor(int[][] details, boolean[][] map, Direction d) {
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
		
		// after we have the list, randomly choose one of the rows
		// TODO: this can "add" a door at a location where
		// a door already exists.
		int idx = r.nextInt(rowcol.size());
		idx = rowcol.get(idx);
		// now that we have the selected row, add a door there, and floor.
		int row = idx*reindex[0];
		int col = idx*reindex[1];
		details[row][col] |= TileType.DOOR.flag;
		for (int inner = innerStart - innerChange; inner < innerMax+1 && inner >= 0; inner += innerChange) {
			row = idx*reindex[0] + inner*reindex[1];
			col = idx*reindex[1] + inner*reindex[0];
			if (map[row][col]) // stop once we have reached the rest of the map
				break;
			map[row][col] = true;
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