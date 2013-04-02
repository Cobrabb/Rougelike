package util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Random;

import orig.DungeonMap;
import orig.Element;
import orig.Planet;

public final class DungeonMapGenerator {
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
		DungeonMap dm = new DungeonMap(map, this.wallBase, this.floorBase);
		return MapUtil.writeMap(mapName, dm);
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