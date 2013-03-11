package orig;

import java.util.ArrayList;

import orig.Item.iType;
import orig.Race.rStats;
import orig.Race.rVal;


//import java.lang.Math;

public class Creature {
	public enum cStats {
		STR_CLIMB, STR_PHYS_ATTACK, STR_MAX_CAPACITY, SPEED_MOVE, SPEED_ATTACK, DETECT_SIGHT,	DETECT_SOUND,
		STEALTH_SIGHT, STEALTH_SOUND, TECH_WEAPON, TECH_ARMOR, STAM_HEALTH, STAM_ENERGY, TOTAL;
		
		public String toString() {
			return this.name();
		}
	}
	/*
	public enum rVal {
		LEVEL,XP,MAX,CURRENT,TOTAL;
		
		public String toString() {
			return this.name();
		}
	}
*/
	private String name; 
	private Race race;

	private int[][] stat;
	//private int[] statXP;
	
	private ArrayList<Item> inventory;
	private Item[] equipped;
	private int staminaMaxInventory;
	private int maxLvl = 100;
	private int raceGain;
	private int initCap = 10; 
	
	
	// behind the scenes
	final double race_creat_ratio = .2; //race stats * .2 + creat stats = effective
	private double mul = 10;
	private double rate = 1.15;
	final int slots = 3;
	private int lvlGain = 1;
	
	public Creature() {
		this.race = new Race();
		this.name = race.getName();
		this.inventory = new ArrayList<Item>();
		this.stat = new int[cStats.TOTAL.ordinal()][rVal.TOTAL.ordinal()];
		this.equipped = new Item[slots+this.race.getNumArms()];
		//set all of stat to 0 and set each level to n
		for(int i=0; i<cStats.TOTAL.ordinal(); i++) {
			for(int j=0; j<rVal.TOTAL.ordinal(); j++) {
				this.stat[i][j] = 0;
			}
		}
		randomInit();
	}

	public Creature(int n) {
		this.race = new Race();
		this.name = race.getName();
		this.inventory = new ArrayList<Item>();
		this.stat = new int[cStats.TOTAL.ordinal()][rVal.TOTAL.ordinal()];
		this.equipped = new Item[slots+this.race.getNumArms()];
		//set all of stat to 0 and set each level to n
		for(int i=0; i<cStats.TOTAL.ordinal(); i++) {
			for(int j=0; j<rVal.TOTAL.ordinal(); j++) {
				this.stat[i][j] = 0;
			}
			this.stat[i][rVal.LEVEL.ordinal()] = n;
		}
	}
	
	public Creature(Element e[], Language l) {
		this.race = new Race(e,l);
		this.name = race.getName();
		this.inventory = new ArrayList<Item>();
		this.stat = new int[cStats.TOTAL.ordinal()][rVal.TOTAL.ordinal()];
		this.equipped = new Item[slots+this.race.getNumArms()];
		//set all of stat to 0 and set each level to n
		for(int i=0; i<cStats.TOTAL.ordinal(); i++) {
			for(int j=0; j<rVal.TOTAL.ordinal(); j++) {
				this.stat[i][j] = 0;
			}
		}
		randomInit();

	}
	
	public Creature(Element e[], Language l, int n) {
		this.race = new Race(e,l);
		this.name = race.getName();
		this.inventory = new ArrayList<Item>();
		this.stat = new int[cStats.TOTAL.ordinal()][rVal.TOTAL.ordinal()];
		this.equipped = new Item[slots+this.race.getNumArms()];
		//set all of stat to 0 and set each level to n
		for(int i=0; i<cStats.TOTAL.ordinal(); i++) {
			for(int j=0; j<rVal.TOTAL.ordinal(); j++) {
				this.stat[i][j] = 0;
			}
			this.stat[i][rVal.LEVEL.ordinal()] = n;
		}
	}
	public Race getRace() {
		return this.race;
	}
	public int get(cStats s, rVal v) {
		return stat[s.ordinal()][v.ordinal()];//[n];
	}
	
	public void set(cStats s,rVal v, int n) {
		stat[s.ordinal()][v.ordinal()] = n;
	}
	
	public int getEffective(cStats s, rVal v) {
		//returns effective value for stat (useful for current/max)
		int eff = (int) (race_creat_ratio*race.get(decode(s),v) + stat[s.ordinal()][v.ordinal()]);
		//System.out.println(eff);
		return eff;
	}
	
	
	public void randomInit() {
		for(int i = 0; i < cStats.TOTAL.ordinal(); i++) {
			stat[i][rVal.LEVEL.ordinal()] = cStats.values()[i].ordinal();
			stat[i][rVal.XP.ordinal()] = (int) Math.pow(stat[i][rVal.LEVEL.ordinal()],3);
			stat[i][rVal.MAX.ordinal()] = (int) Math.pow(stat[i][rVal.LEVEL.ordinal()],2.3);
			stat[i][rVal.CURRENT.ordinal()] = (int) stat[i][rVal.MAX.ordinal()];
		}
	}
	
	//Gain/lose health,energy,stats,XP etc
	public void gain(cStats s, rVal v, int n) {
		this.stat[s.ordinal()][v.ordinal()] += n;
		if(v == rVal.XP) { 
			rStats r = decode(s);
			this.race.gain(r, v, n/raceGain); //Race gains XP in corresponding stat, divided by number of cstats that map to it
			System.out.println(n+ " / " + raceGain + " = " + n/raceGain);
		}
		checkXP(s);
	}
	
	//If XP >= mul*(lvl)^rate, then lvlUP
	private void checkXP(cStats s) {
		//while XP >= max for level, level up
		while( (this.stat[s.ordinal()][rVal.XP.ordinal()]) >= ((int) mul*Math.pow(this.stat[s.ordinal()][rVal.LEVEL.ordinal()],rate)) ){
			//System.out.println(stat[s.ordinal()][rVal.XP.ordinal()] + ", " + (int) (mul*Math.pow(this.stat[s.ordinal()][rVal.LEVEL.ordinal()],rate)));
			lvlUp(s, mul, rate);
		}
	}
	private void lvlUp(cStats s, double mul, double rate) {
		//Incrementlevel and decrement XP by XP cap for level;
		this.stat[s.ordinal()][rVal.XP.ordinal()] -= ((int) mul*Math.pow(this.stat[s.ordinal()][rVal.LEVEL.ordinal()],rate));
		this.stat[s.ordinal()][rVal.LEVEL.ordinal()]++;
		this.stat[s.ordinal()][rVal.MAX.ordinal()] += lvlGain;
		// current = max (unless temp effect has it above max already)
		if(this.stat[s.ordinal()][rVal.CURRENT.ordinal()] < this.stat[s.ordinal()][rVal.MAX.ordinal()]) {
			this.stat[s.ordinal()][rVal.CURRENT.ordinal()] = this.stat[s.ordinal()][rVal.MAX.ordinal()];
		}
//		this.race.gain(decode(s),rVal.XP,raceGain);
	}
	
	public rStats decode(cStats s) {
		String temp[], str[] = s.toString().split("_");
		rStats names[] = rStats.values();
		cStats cnames[] = cStats.values();
		this.raceGain = 0;
		int rNum = -1;
		for(int i=0; i<rStats.TOTAL.ordinal(); i++) {
			//if(str[0].compareTo(names[i].toString()) == 0) {
			if((str[0].substring(0, 3)).compareTo((names[i].name().substring(0, 3))) == 0) {
				rNum = i;
				for(int j=0; j<cStats.TOTAL.ordinal(); j++) {
					temp = cnames[j].toString().split("_");
					//compares the first 3 characters between 
					if(str[0].substring(0, 3).compareTo(temp[0].substring(0, 3)) == 0) {
						this.raceGain++;
					}
				}
			}
		}
		//System.out.println(str[0]);
		if( (rNum > rStats.TOTAL.ordinal()) || (rNum < 0)) {
			return null;
		}
		return names[rNum];
	}
	
	public int getRaceGain() {
		return this.raceGain;
	}
	/*
	public void setRaceGain(int n) {
		this.raceGain = n;
	}
	*/
	
	public void attack(Square s) {
		for(int i=0; i<this.race.getNumArms(); i++) {
			//slots holds the number of non-arm slots in equipped
			// for each arm-slot, attack s
			this.equipped[slots+i].attack(s,getEffective(cStats.STR_PHYS_ATTACK,rVal.CURRENT),getEffective(cStats.TECH_WEAPON,rVal.CURRENT));
			//this.stat[cStats.STR_PHYS_ATTACK.ordinal()][rVal.CURRENT.ordinal()],this.stat[cStats.TECH_WEAPON.ordinal()][rVal.CURRENT.ordinal()]);
		}
	}
	
	public void takeDamage(Element[] consists, int damage) {
		//get resistances from univeral reaction table
	}
	
	public boolean equip(Item i, int hand) {
		if(i.getType() == iType.HAND) {
			if(hand >= 0 && hand <= race.getNumArms()) {
				equipped[slots+hand] = i;
				return true;
			}
			return false;
		}
		equipped[i.getType().ordinal()] = i;
		return true;
	}

}
