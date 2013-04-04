package util;

import orig.Element;
import orig.Planet;

public class TestDMG {
	
	public static void main(String[] args) {
		Element elem1 = new Element("stone_floor", 1.0);
		Element elem2 = new Element("stone_wall_updown", 2.0);
		Element elem3 = new Element("stone_wall_leftright", 2.0);
		Planet p = new Planet(new Element[] {elem1, elem2, elem3});
		DungeonMapGenerator map = new DungeonMapGenerator(p);
		
		
		boolean[][] m = null;//map.generateBlankSquareMap("", 20, 20);
		
		for (int i = 0; i < m.length; ++i) {
			for (int j = 0; j < m[i].length; ++j) {
				System.out.printf("%d", (m[i][j] ? 1 : 0));
			}
			System.out.println();
		}
	}
	
}
