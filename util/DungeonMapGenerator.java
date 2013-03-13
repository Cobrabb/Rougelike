package util;

import java.io.IOException;
import java.util.Random;

import orig.Element;
import orig.Planet;




public final class DungeonMapGenerator {
	Random r = new Random();
	Element floorBase;
	Element wallBase;
	
	public DungeonMapGenerator(Planet p) {
		if (p.getElementCount() > 1) {
			floorBase = p.getElement(0);
			wallBase = p.getElement(1);
		} else {
			floorBase = new Element();
			wallBase = floorBase;
		}
	}
	
	/**
	 *make a map array that hold double array of booleans which represents where we will place tiles
	 *initally false = wall, true = tiles
	 * 
	 */
	
	public void makeAllUnvisited()// sets the whole map as unvisited
	{
		
	}
	public boolean[][] generateMap(String mapPath, int width, int height)
	{
		boolean map[][] = new boolean[width][height];
		int numRoom = r.nextInt(6) + 15;
		
		//choose random row choose random column
		int xRand; // location of top left corner of room
		int yRand;
		int roomWidth;
		int roomHeight;
		int failure;
		for(int i = 0; i < numRoom; i++)
		{
			roomWidth = r.nextInt(width/3-2)+3;
			//roomHeight = r.nextInt(height/2-1)+2;
			roomHeight = roomWidth + (r.nextInt(5) - 2);
			xRand = r.nextInt(width - roomWidth - 1);
			yRand = r.nextInt(height - roomHeight - 1);
			failure = 0;
			boolean okay = true;
			while (okay && failure < 10) {
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
	
	
	public String weakRandomMapGen(String mapPath, int width, int height) {
		String[] layers = {"floor", "wall", "doors"};
		String[][][] tiles = new String[layers.length][width][height];
		Random rand = new Random();
		// poor implementation
		for (int x = 0; x < width; ++x) {
			for (int y = 0; y < height; ++y) {
				tiles[0][x][y] =  floorBase.getName();
				tiles[1][x][y] = "";
				tiles[2][x][y] = "";
				if (rand.nextInt(6) == 0) {
					tiles[1][x][y] = wallBase.getName();
				}
			}
		}
		String path = null;
		try {
			path = MapWriter.arrayToXML(mapPath, layers, tiles);
		} catch (Exception e) {
			e.printStackTrace();
			System.err.printf("Error generating map at location %s of width %d, height %d\n", mapPath, width, height);
		}
		return path;
	}
}