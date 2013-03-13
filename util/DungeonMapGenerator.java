package util;

import java.io.IOException;
import java.util.Random;

import orig.Element;
import orig.Planet;




public final class DungeonMapGenerator {
	
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
	
	public String weakRandomMapGen(String mapPath, int width, int height) {
		String[] layers = {"wall", "floor", "doors"};
		String[][][] tiles = new String[layers.length][width][height];
		Random rand = new Random();
		// poor implementation
		for (int x = 0; x < width; ++x) {
			for (int y = 0; y < height; ++y) {
				tiles[0][x][y] = "";
				tiles[1][x][y] = floorBase.getName();
				tiles[2][x][y] = "";
				if (rand.nextInt(6) == 0) {
					tiles[0][x][y] = wallBase.getName();
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