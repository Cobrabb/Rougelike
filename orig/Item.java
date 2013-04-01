package orig;

import java.util.ArrayList;

import orig.Attack.AttackDirection;
import orig.Attack.AttackPattern;
import orig.Creature.cStats;
import orig.Creature.sVal;

public class Item {
	
	public enum iType {
		HEAD, ARMOR, BOOTS, HAND, TOTAL;
		
		public String toString() {
			return this.name();
		}
	}
	
	public enum AttackType {
		BUFF, PHYS, RANGED, NONE
	}
	
	//private UniversalElements UE = null;

	private Element[] consists = null; //need to know proportions
	private Element[] repairs = null; //need to know proportions
	//private Element[] improve;
	private int level = 1; // item level
	private int hands = 1; //number of hands required to wield
	private int techRequired = 1;
	private double phys_tech_ratio = 0; //0 is pure physical, 1 is pure tech (projectile)
	private int baseDmg = 0; //base value for attacking/defending
	private iType type = null;
	private String name = "";
	private double weight = 0.0;
	private double health = 0.0;
	private boolean attack = false; //whether it is an attack or defense item
	private ArrayList<Effect> effects = null; //does item pass on any additional effects
	private AttackPattern pattern = AttackPattern.POINT; // how does this item attack, defaults to a point
	private AttackType atype = AttackType.PHYS; // defaults to physical attack
	private int attackSize = 2; // defaults to attacking 2 spacing (except for point attacks)
//	private double wearDmg = 0.0;
	
	public Item() {
		this.name = new Language().generate();
		iType types[] = iType.values();
		this.type = types[(int) Math.random()*iType.TOTAL.ordinal()];
		//generate random elements for consists and repairs
		this.consists = new Element[(int) Math.max((1.0/Math.random()),1)]; //create a new element array with random number  of slots, weighted toward fewer slots (having at least 1)
		for(int i=0; i<this.consists.length; i++) {
			this.consists[i] = UET.getUET().getElementList().get((int) (Math.min((UET.TOTAL+1)*Math.random(),UET.TOTAL)));
		}
		this.repairs = new Element[(int) Math.max((1.0/Math.random()),1)]; //create a new element array with random number  of slots, weighted toward fewer slots (having at least 1)
		for(int i=0; i<this.repairs.length; i++) {
			this.repairs[i] = UET.getUET().getElementList().get((int) ((UET.TOTAL+1)*Math.random()));
		}
		this.level = (int) (10*Math.random());
		if(this.type == iType.HAND) {
			this.attack = (Math.random() < .85); // 85% chance of being weapon (as opposed to shield)
			this.hands = (int) Math.max((this.level/Math.random()),1); //generate random number of hands (at least 1 hand though)
			this.baseDmg = (int) (this.level/Math.random())*this.hands; //generate random damage (assuming more hands means a more powerful weapon)
		}
		else {
			this.attack = false;
			this.hands = 0;
			this.baseDmg = (int) (this.level/Math.random()); //generate random damage
		}
		this.techRequired = (int) (100*Math.random()); //generate tech level required
		this.phys_tech_ratio = Math.random(); //generate random ratio of physical and tech
		this.weight = 1/Math.random();
		this.health = 1/Math.random();
		this.effects = new ArrayList<Effect>(0);
		for(int i=0;Math.random() < .7; i++) {
			this.effects.add(new Effect());
		}
		AttackPattern ap[] = AttackPattern.values();
		this.pattern = ap[(int) (AttackPattern.OTHER.ordinal()*Math.random())];
		AttackType at[] = AttackType.values();
		this.atype = at[(int) (AttackType.NONE.ordinal()*Math.random())];
		this.attackSize = (int) (1/Math.random());
	}
	
	public Item(Element[] consists, Element[] repairs, int hands, int techRequired, double phys_tech_ratio, int baseDmg, iType type, double weight) {
		this.consists = consists;
		this.repairs = repairs;
		this.hands = hands;
		this.techRequired = techRequired;
		this.phys_tech_ratio = phys_tech_ratio;
		this.baseDmg = baseDmg;
		this.type = type;
		this.weight = weight;
	}
	

/*
	public boolean repair(Element[] elements) {
		boolean bool = false;
		for(Element e: elements) {
			
		}
		return bool;
	}
*/	
	public iType getType() {
		return this.type;
	}
	
	public String getName(){
		return this.name;
	}
	
	public int getHands() {
		return this.hands;
	}
	
	public  double getWeight() {
		return this.weight;
	}
	
	public double getHealth() {
		return this.health;
	}
	
	public Element[] getConsists() {
		return this.consists;
	}
	
	public AttackPattern getAttackPattern() {
		return this.pattern;
	}
	
	public int getAttackSize() {
		return this.attackSize;
	}
	
	public boolean canAttack() {//returns whether it is weapon or defensive item
		return this.attack;
	}
	
	public boolean canEquip(int techLevel) {
		return (techLevel >= this.techRequired);
	}
	
	public ArrayList<Effect> getEffects() {
		return this.effects;
	}
	
	public double getPhysTech() {
		return this.phys_tech_ratio;
	}
	
////////////////////////////////Stuff for attacking////////////////////////////////////	
	public Attack attack(int x, int y, AttackDirection ad, Creature c) {
		int phys = c.getEffective(cStats.STR_PHYS_ATTACK, sVal.CURRENT);
		int tech = c.getEffective(cStats.TECH_WEAPON, sVal.CURRENT);
		double damage = this.baseDmg*((1-phys_tech_ratio)*phys + phys_tech_ratio*tech);
		Attack a = new Attack(x,y,damage,this,c,ad);
		return a;
	}
	
	public AttackType getAttackType() {
		return this.atype;
	}
	
	public void takeAttack(Attack a) {
		double dmg = 0, count = 0, atStr = a.getAttackStrength();
		for(Element ae : a.getWeapon().getConsists()) {
			for(Element me : this.consists) {
				dmg += atStr*UET.getUET().getDmg(ae, me);
				count++;
			}
		}
		dmg /= count;
		this.health -= dmg;
		if(this.health < 0) this.health = 0;
		
		if(a.getWeapon().getAttackType() == AttackType.PHYS){
			ArrayList<Element> elements = new ArrayList<Element>(0);
			for(Element e : this.consists) {
				elements.add(e);
			}
			AttackResults ar = new AttackResults(this.effects, this, dmg);
			a.getWeapon().takeAttackResults(ar); 
		}
	}
	
	public void takeAttackResults(AttackResults ar) {
		if(ar != null) {//takes reflected damaged
			double dmg = 0, count = 0, atStr = ar.getAttackStrength();
			for(Element ae : ar.getConsists()) {
				for(Element me : this.consists) {
					dmg += atStr*UET.getUET().getDmg(ae, me);
					count++;
				}
			}
			dmg /= count;
			this.health -= dmg;
			if(this.health < 0) this.health = 0;
			
		}
		else {//need to figure out how much firing damage it should take
			//damage should be proportional to damage dealt, but inversely to skill (know how to take care of weapon)
		}
	}
	

}
