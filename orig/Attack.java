package orig;

import java.util.ArrayList;

public class Attack {
	public enum AttackPattern {
		POINT, LINE, CIRCLE, PLUS, WEDGE, OTHER
	}
	
	private int x, y;
	private ArrayList<int[]> pattern;
	private Item weapon;
	private ArrayList<Effect> effects;
	private Creature attacker;
	
	public Attack(int x, int y, AttackPattern ap, int attackSize, Creature attacker) {
		this.x = x;
		this.y = y;
		decodePattern(ap, attackSize);
		this.attacker = attacker;
	}
	
	public void decodePattern(AttackPattern ap, int attackRadius) {
		switch(ap) {
		case POINT:
			this.pattern = new ArrayList<int[]>(1);
			this.pattern.set(0, new int[2]);
			this.pattern.get(0)[0] = this.x;
			this.pattern.get(0)[1] = this.y;
			break;
		case LINE:
			this.pattern = new ArrayList<int[]>(attackRadius);
			//determine orientation of attack based on attack position and originating position
			
			break;
		case CIRCLE:
			//this is the is the number in the square (bigger than circle)
			this.pattern = new ArrayList<int[]>(attackRadius*attackRadius*4);
			//figure out pattern
			break;
		case PLUS:
			this.pattern = new ArrayList<int[]>(4*attackRadius-1);
			this.pattern.get(0)[0] = this.x;
			this.pattern.get(0)[1] = this.y;
			//begin creating the pattern o,pi/2,pi,3pi/2
			for(int i=1; i<2*attackRadius; i+=4) {
				this.pattern.get(i)[0] = this.x+i;
				this.pattern.get(i)[1] = this.y;
				this.pattern.get(i+1)[0] = this.x;
				this.pattern.get(i+1)[1] = this.y+i;
				this.pattern.get(i+2)[0] = this.x-i;
				this.pattern.get(i+2)[1] = this.y;
				this.pattern.get(i+3)[0] = this.x;
				this.pattern.get(i+3)[0] = this.y-i;
				
			}
			
			break;
		case WEDGE:
			this.pattern = new ArrayList<int[]>(attackRadius*attackRadius/2);//triangle pattern
			
			break;
		}
	}
	
	public ArrayList<int[]> getPattern() {
		return this.pattern;
	}
	
	public void addPatternPoint(int[] point) {
		this.pattern.add(new int[2]);
		this.pattern.get(this.pattern.size()-1)[0] = point[0];
		this.pattern.get(this.pattern.size()-1)[1] = point[1];
	}
	
	public void removePatternPoint(int[] point) {
		this.pattern.remove(point);
	}
}
