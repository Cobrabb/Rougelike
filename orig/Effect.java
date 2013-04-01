package orig;

import orig.Creature.cStats;
import orig.Creature.bStats;
import orig.Creature.sVal;

public class Effect {

	private cStats cstat; //which cStat to affect
	private bStats bstat; // which bStat to affect
	private sVal sval; // which sVal to affect
	private double value; // amount to modify by initially;
	private double repeatRate; // amount to modify value by each step (i.e. it decreases in effectiveness each step)
	private boolean base; //whether to affect base stats or creature stats
	private boolean permanent; //whether to modify permanent stats or be temporary
	private int steps; //number of steps until it wears off
	private boolean elemental; //whether to consider elements or not
	private Element[] elements; //elements it consists of (for damage/healing);
	private boolean move; // whether or not to move position
	private int relX, relY;  // new coordinates of creature (relative to current)
	
	public Effect(cStats cstat, int value) {
		this.cstat = cstat;
		this.bstat = null;
		this.value = value;
		this.base = false;
		this.permanent = false;
	}
	public Effect(bStats bstat, int value) {
		this.cstat = null;
		this.bstat = bstat;
		this.value = value;
		this.base = true;
	}
	
	public cStats getCStat() {
		return this.cstat;
	}
	
	public bStats getBStat() {
		return this.bstat;
	}
	
	public double getValue() {
		return this.value;
	}
	
	public sVal getSVal() {
		return this.sval;
	}
	
	public int getSteps() {
		return this.steps;
	}
	
	public boolean getBase() {
		return this.base;
	}
	
	public void update() {
		if(this.permanent) {
			
		}
		else {
			this.steps--;
		}
	}
}
