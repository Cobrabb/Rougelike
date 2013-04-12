package orig;

import java.io.Serializable;

import orig.Creature.bStats;
import orig.Creature.cStats;
import orig.Creature.sVal;

public class Effect implements Serializable {

	private cStats cstat = null; //which cStat to affect
	private bStats bstat = null; // which bStat to affect
	private boolean attack = true; //whether harms others or help self
	private sVal sval = null; // which sVal to affect
	private double value = 0.0; // amount to modify by initially;
	private double repeatRate = 0.0; // amount to modify value by each step (i.e. it decreases in effectiveness each step)
	private boolean base = false; //whether to affect base stats or creature stats
	private boolean permanent = false; //whether to modify permanent stats or be temporary
	private int steps = 1; //number of steps until it wears off
	private boolean elemental = false; //whether to consider elements or not
	private Element element = null; //element it consists of (for damage/healing);
	private boolean move = false; // whether or not to move position
	private int rel[] = {0,0};  // new coordinates of creature (relative to current)
	
	public Effect(Effect e) {
		this.cstat = e.getCStat();
		this.bstat = e.getBStat();
		this.attack = e.isAttack();
		this.sval = e.getSVal();
		this.value = e.getValue();
		this.repeatRate = e.getRepeateRate();
		this.base = e.isBase();
		this.permanent = !e.isTemp();
		this.steps = e.getSteps();
		this.elemental = e.isElemental();
		this.element = e.getElement();
		this.move = e.isMove();
		this.rel = new int[e.getRel().length];
		for(int i=0; i<e.getRel().length; i++) {
			rel[i] = e.getRel()[i];
		}
	}
	public Effect() {
		this.base = (Math.random() < .15); //chooses whether it deals with base or creature values (weighted 85% toward Creature)
		if(base) {
			bStats b[] = bStats.values();
			this.bstat = b[(int) (bStats.TOTAL.ordinal()*Math.random())];
		}
		else {
			cStats c[] = cStats.values();
			this.cstat = c[(int) (cStats.TOTAL.ordinal()*Math.random())];
		}
		sVal s[] = sVal.values();
		this.sval = s[(int) (sVal.TOTAL.ordinal()*Math.random())];
		this.attack = (Math.random() <.7);  // 70% chance of being an attack  effect
		this.value = 1/Math.random();
		this.repeatRate = Math.random()/Math.random(); // can be higher or lower
		this.steps = (int) (1/Math.random());
		this.permanent = (Math.random() < .15); //chooses permanent or temp (weighted 85% to temp
		this.elemental = (Math.random() < .65); //chooses elemental or not (weighted 65% to elemental
		if(this.elemental){
			this.element = UET.getUET().getElementList().get((int) ((UET.TOTAL*+1)*Math.random())); 
		}
		this.move = (Math.random() < .1); // chooses whether or not to move the affected
		if(this.move){
			this.rel = new int[2];
			this.rel[0] = (int) (Math.random()/Math.random());
			this.rel[1] = (int) (Math.random()/Math.random());
		}
	}
	
	public Effect(cStats cstat, sVal sval, double value) {
		this.cstat = cstat;
		this.bstat = null;
		this.sval = sval;
		this.value = value;
		this.base = false;
		this.permanent = false;
	}
	
	public Effect(bStats bstat, sVal sval, double value) {
		this.cstat = null;
		this.bstat = bstat;
		this.sval = sval;
		this.value = value;
		this.base = true;
		this.permanent = false;
	}
	
	public Effect(cStats cstat, sVal sval, double value, boolean permanent) {
		this.cstat = cstat;
		this.bstat = null;
		this.sval = sval;
		this.base = false;
		this.value = value;
		this.permanent = permanent;
	}
	
	public Effect(bStats bstat, sVal sval, double value, boolean permanent) {
		this.cstat = null;
		this.bstat = bstat;
		this.sval = sval;
		this.base = true;
		this.value = value;
		this.permanent = permanent;
	}
	
	public Effect(cStats cstat, sVal sval, double value, int steps, boolean permanent) {
		this.cstat = cstat;
		this.bstat = null;
		this.sval = sval;
		this.base = false;
		this.value = value;
		this.steps = steps;
		this.permanent = permanent;
	}
	
	public Effect(bStats bstat, sVal sval, double value, int steps, boolean permanent) {
		this.cstat = null;
		this.bstat = bstat;
		this.sval = sval;
		this.base = true;
		this.value = value;
		this.steps = steps;
		this.permanent = permanent;
	}
	
	public Effect(cStats cstat, sVal sval, double value, int steps) {
		this.cstat = cstat;
		this.bstat = null;
		this.sval = sval;
		this.base = false;
		this.value = value;
		this.steps = steps;
	}
	
	public Effect(bStats bstat, sVal sval, double value, int steps) {
		this.cstat = null;
		this.bstat = bstat;
		this.sval = sval;
		this.base = true;
		this.value = value;
		this.steps = steps;
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
	
	public boolean isAttack() {
		return this.attack;
	}
	
	public int getSteps() {
		return this.steps;
	}
	
	public void setSteps(int steps) {
		this.steps = steps;
	}
	
	public boolean isBase() {
		return this.base;
	}
	
	public boolean isElemental() {
		return this.elemental;
	}
	
	public boolean isTemp() {
		return !this.permanent;
	}
	
	public Element getElement() {
		return this.element;
	}
	
	public boolean isMove() {
		return this.move;
	}
	
	public int[] getRel() {
		return this.rel;
	}
	
	protected double getRepeateRate() {
		return this.repeatRate;
	}
	
	public void update() {
		this.steps--;
		this.value *= this.repeatRate;
	}
}
