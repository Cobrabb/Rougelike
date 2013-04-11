package orig;

import java.util.ArrayList;

public class AttackResults {
	private ArrayList<Effect> effects = null;
	private double attackStrength = 0.0;
//	private double defenseStrengths[] = null;
	private Item item = null;
	private ArrayList<Element> consists = null;

	public AttackResults(ArrayList<Effect> effects, Item i , double attackStrength, ArrayList<Element> consists) {
		this.effects = new ArrayList<Effect>(effects.size());
		for(int j=0; j<effects.size(); j++) {
			this.effects.add(effects.get(j));
		}
		this.item = i;
		this.attackStrength = attackStrength;
		this.consists = new ArrayList<Element>(consists.size());
		for(int j=0; j<consists.size(); j++) {
			this.consists.add(consists.get(j));
		}
	}
	
	public AttackResults(ArrayList<Effect> effects, Item i, double attackStrength) {
		this.effects = effects;
		this.attackStrength = attackStrength;
		if(i != null) {
			this.item = i;
			this.consists = new ArrayList<Element>(0);
			for(int j=0; j<i.getConsists().length; j++) {
				this.consists.add(i.getConsists()[j]);
			}
		}
	}
	
/*
	public AttackResults(ArrayList<Effect> effects,Item i, double attackStrength) {
		this.effects = effects;
		this.attackStrength = attackStrength;
		this.item = i;
	}
*/	
	
	public ArrayList<Effect> getEffects() {
		return this.effects;
	}
	
	public double getAttackStrength() {
		return this.attackStrength;
	}
	
	public Item getItems() {
		return this.item;
	}
	
	public ArrayList<Element> getConsists() {
		return this.consists;
	}
	
	public void merge(AttackResults ar) {
		if(this.effects == null) {
			this.effects = ar.getEffects();
		}
		else {
			for(int i=0; i<ar.getEffects().size(); i++) {
				this.effects.add(ar.getEffects().get(i));
			}
		}
	}
}
