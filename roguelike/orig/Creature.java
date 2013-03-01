package orig;

import java.util.ArrayList;

import orig.Race.rStats;
//import java.lang.Math;
import orig.Race.rVal;

public class Creature {
	public enum cStats {
		STR_CLIMB, STR_PHYS_ATTACK, SPEED_MOVE, SPEED_ATTACK, DETECT_SIGHT,	DETECT_SOUND,
		STEALTH_SIGHT, STEALTH_SOUND, TECH_WEAPON, TECH_ARMOR, STAM_HEALTH, STAM_ENERGY, TOTAL
	}
	public enum cVal {
		LEVEL,XP,MAX,CURRENT,TOTAL
	}

	private String name; 
	private Race race;

	private int[][] stat;
	//private int[] statXP;
	
	private ArrayList<Item> inventory;
	private Item[] equipped;
	private int staminaMaxInventory;
	private int maxLvl = 100;
	private int raceGain;
	final int slots = 3;
	
	public Creature() {
		this.race = new Race();
		this.name = race.getName();
		this.inventory = new ArrayList<Item>();
		this.stat = new int[cStats.TOTAL.ordinal()][cVal.TOTAL.ordinal()];
		this.equipped = new Item[slots+this.race.getNumArms()];
		//this.statXP = new int[(cStats.TOTAL.ordinal()+1)];
		randomInit();
	}
	
	public Creature(Element e[], Language l) {
		this.race = new Race(e,l);
		this.name = race.getName();
		this.inventory = new ArrayList<Item>();
		this.stat = new int[cStats.TOTAL.ordinal()][cVal.TOTAL.ordinal()];
		this.equipped = new Item[slots+this.race.getNumArms()];
		randomInit();

	}
	
	public int get(cStats s, cVal v) {
		return stat[s.ordinal()][v.ordinal()];//[n];
	}
	
	public void set(cStats s,cVal v, int n) {
		stat[s.ordinal()][v.ordinal()] = n;
	}
	
	
	public void randomInit() {
		for(int i = 0; i < cStats.TOTAL.ordinal(); i++) {
			stat[i][cVal.LEVEL.ordinal()] = cStats.values()[i].ordinal();
			stat[i][cVal.XP.ordinal()] = (int) Math.pow(stat[i][cVal.LEVEL.ordinal()],3);
			stat[i][cVal.MAX.ordinal()] = (int) Math.pow(stat[i][cVal.LEVEL.ordinal()],2.3);
			stat[i][cVal.CURRENT.ordinal()] = (int) stat[i][cVal.MAX.ordinal()];
		}
	}
	
	//Gain/lose health,energy,stats,XP etc
	public void gain(cStats s, cVal v, int n) {
		this.stat[s.ordinal()][v.ordinal()] += n;
		checkXP(s);
	}
	
	//If XP >= mul*(lvl)^rate, then lvlUP
	private void checkXP(cStats s) {
		double mul = 10; 
		double rate = 1.15; 
		boolean test = (this.stat[s.ordinal()][rVal.XP.ordinal()]) >= ((int) mul*Math.pow(this.stat[s.ordinal()][rVal.LEVEL.ordinal()],rate));
		if(test) {
			lvlUp(s);
		}
	}
	private void lvlUp(cStats s) {
		this.stat[s.ordinal()][cVal.LEVEL.ordinal()]++;
		this.stat[s.ordinal()][cVal.XP.ordinal()] = 0;
//		this.race.gain(decode(s),rVal.XP,raceGain);
	}
	
	private rStats decode(cStats s) {
		String str = s.toString();
		System.out.println(str);
		raceGain = 2;
		return rStats.SPEED;
	}
	
	public void attack(Square s) {
		for(int i=0; i<this.race.getNumArms(); i++) {
			this.equipped[slots+i].attack(s,this.stat[cStats.STR_PHYS_ATTACK.ordinal()][cVal.CURRENT.ordinal()],this.stat[cStats.TECH_WEAPON.ordinal()][cVal.CURRENT.ordinal()]);
		}
	}
	
	public void takeDamage(Element[] consists, int damage) {
		//get resistances from univeral reaction table
	}

}
