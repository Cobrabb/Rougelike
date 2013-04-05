package util;

public final class General {
	public enum Direction {
		LEFT,
		DOWNLEFT,
		DOWN,
		DOWNRIGHT,
		RIGHT,
		UPRIGHT,
		UP,
		UPLEFT;
		
		private int[] dx = {-1, -1,  0,  1, 1, 1, 0, -1};
		private int[] dy = { 0, -1, -1, -1, 0, 1, 1,  1};
		
		int x;
		int y;
		
		Direction() {
			this.x = dx[this.ordinal()];
			this.y = dy[this.ordinal()];
		}
	}
}
