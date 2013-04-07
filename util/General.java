package util;

import java.io.Serializable;

import util.General.Direction;
import util.General.GridPoint;

public final class General {
	public enum Direction {
		LEFT,
		DOWNLEFT,
		DOWN,
		DOWNRIGHT,
		RIGHT,
		UPRIGHT,
		UP,
		UPLEFT,
		LOWER,
		HIGHER;
		
		private int[] dx = {-1, -1,  0,  1, 1, 1, 0, -1, 0, 0};
		private int[] dy = { 0, -1, -1, -1, 0, 1, 1,  1, 1, -1};
		
		int x;
		int y;
		
		Direction() {
			this.x = dx[this.ordinal()];
			this.y = dy[this.ordinal()];
		}
		
		public int getX() {
			return this.x;
		}
		
		public int getY() {
			return this.y;
		}
		
		public Direction opposite() {
			switch (this) {
			case LEFT:
				return RIGHT;
			case RIGHT:
				return LEFT;
			case UP:
				return DOWN;
			case DOWN:
				return UP;
			case UPLEFT:
				return DOWNRIGHT;
			case DOWNRIGHT:
				return UPLEFT;
			case UPRIGHT:
				return DOWNLEFT;
			case DOWNLEFT:
				return UPRIGHT;
			case LOWER:
				return HIGHER;
			case HIGHER:
				return LOWER;
			}
			return null;
		}
		
		/**
		 * This method roughly determines cardinal direction
		 * @param x
		 * @param y
		 * @return The Direction of the map we are on
		 */
		public static Direction getDirection(int x, int y) {
			// LEFT: y varies, x is small ~0
			if (x == 0)
				return LEFT;
			// UP: x varies, y is small ~0
			else if (y == 0)
				return UP;
			// RIGHT: y varies, x is high
			else if (x > y)
				return RIGHT;
			// DOWN: x varies, y is high
			else if (y > x)
				return DOWN;
			// can't tell. but most will fall into "right" or "down"
			return null;
		}

		public static Direction getDirection(GridPoint d) {
			Direction ret = null;
			boolean flipped = false;
			if (d.getX() < 0) {
				d.flipSigns();
				flipped = true;
			}
			ret = Direction.getDirection(d.getX(), d.getY());
			if (flipped)
				d.flipSigns();
			return ret;
		}
	}
	
	public static class GridPoint implements Serializable {
		/**
		 * 
		 */
		private static final long serialVersionUID = -3898952174675649150L;
		
		int x;
		int y;
		
		public GridPoint(int xx, int yy) {
			x = xx;
			y = yy;
		}
		
		// might be a bit weak, but good enough for our uses
		public int hashCode() {
			return x*69739 + y;
		}
		
		public boolean equals(Object o) {
			if (!(o instanceof GridPoint)) {
				return false;
			}
			GridPoint gp = (GridPoint)o;
			return (gp.x == x && gp.y == y);
		}

		public int getX() {
			return this.x;
		}
		
		public int getY() {
			return this.y;
		}
		
		public void flipSigns() {
			this.x = -this.x;
			this.y = -this.y;
		}
		
		public void mirror(int xMax, int yMax) {
			if (this.x == 0 || this.x == xMax - 1) {
				this.x = (xMax - 1 - this.x);
			}
			if (this.y == 0 || this.y == yMax - 1) {
				this.y = (yMax - 1 - this.y);
			}
		}
		
		public String toString() {
			return String.format("(%d, %d)", x, y);
		}
	}
}
