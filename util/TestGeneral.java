package util;

import util.General.Direction;

public class TestGeneral {
	public static void main(String[] args) {
		Direction[] dirs = {Direction.LEFT, Direction.DOWN, Direction.DOWNLEFT, Direction.DOWNRIGHT, Direction.UP, Direction.LOWER, Direction.RIGHT};
		for (Direction d: dirs) {
			System.err.printf("The opposite of %s is %s\n", d, d.opposite());
		}
	}
}
