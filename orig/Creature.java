package orig;

import java.util.ArrayList;

import orig.Item.iType;


//import java.lang.Math;

public class Creature {
	public enum cStats {
		STR_CLIMB, STR_PHYS_ATTACK, STR_MAX_CAPACITY, SPEED_MOVE, SPEED_ATTACK, DETECT_SIGHT,	DETECT_SOUND,
		STEALTH_SIGHT, STEALTH_SOUND, TECH_WEAPON, TECH_ARMOR, STAM_HEALTH, STAM_ENERGY, TOTAL;
		
		public String toString() {
			return this.name();
		}
	}
	
	public enum bStats {
		STRENGTH,SPEED,DETECTION,STEALTH,TECH,STAMINA,TOTAL;
		
		public String toString() {
			return this.name();
		}
	}
	
	public enum sVal {
		LEVEL,XP,MAX,CURRENT,TOTAL;
		
		public String toString() {
			return this.name();
		}
	}
	/*
	public enum Elem {
		CONSUMES, PRODUCES, CASING, FLUID, ORGANS, TOTAL;
		
		public String toString() {
			return this.name();
		}
	}
*/

	//from Race
	//private String name;
	private String name; 
	private String rName;
	private int diet;  //How it eats, not what it eats
	private Element consumes; //what it eats
	private Element produces; //what it poops
	private Element casing; //the flesh
	private Element fluid; //the blood
	private Element organs;
	private boolean genders; //true if the Race is not a hermaphodite
	private int numArms;
	private int numLegs; //potentially for stability/speed
	
	private int[][] cStat;
	private int[][] bStat;
	
	private ArrayList<Item> inventory;
	private Item[] equipped; // which items (non-hand)
	private ArrayList<Item> weilding; // which items (hands)
	private int staminaMaxCapacity;
	private int availHands;
	private double weight;
	
	
	// behind the scenes
	final double base_creat_ratio = .2; //race stats * .2 + creat stats = effective
	final double mul = 10;
	final double rate = 1.15;
	final int slots = 3;
	final int lvlGain = 1;
	private int maxLvl = 100;
	private int baseGain;
	private int initCap = 5*cStats.TOTAL.ordinal();
	
	/*public Creature() {
		//this.name = race.getName();
		this.inventory = new ArrayList<Item>();
		this.cStat = new int[cStats.TOTAL.ordinal()][sVal.TOTAL.ordinal()];
		this.bStat = new int[bStats.TOTAL.ordinal()][sVal.TOTAL.ordinal()];
		this.equipped = new Item[slots];
		this.weilding = new ArrayList<Item>(0);
		//set all of stat to 0 and set each level to n
		for(int i=0; i<cStats.TOTAL.ordinal(); i++) {
			for(int j=0; j<sVal.TOTAL.ordinal(); j++) {
				this.cStat[i][j] = 0;
			}
		}
		for(int i=0; i<bStats.TOTAL.ordinal(); i++) {
			for(int j=0; j<sVal.TOTAL.ordinal(); j++) {
				this.bStat[i][j] = 0;
			}
		}
		randomInitXP();
	}
	*/
	
	/*
	public Creature(Element e[], Language l) {
		//this.name = race.getName();
		this.inventory = new ArrayList<Item>();
		this.cStat = new int[cStats.TOTAL.ordinal()][sVal.TOTAL.ordinal()];
		this.bStat = new int[bStats.TOTAL.ordinal()][sVal.TOTAL.ordinal()];
		this.equipped = new Item[slots];
		//set all of stat to 0 and set each level to n
		for(int i=0; i<cStats.TOTAL.ordinal(); i++) {
			for(int j=0; j<sVal.TOTAL.ordinal(); j++) {
				this.cStat[i][j] = 0;
			}
		}
		for(int i=0; i<bStats.TOTAL.ordinal(); i++) {
			for(int j=0; j<sVal.TOTAL.ordinal(); j++) {
				this.bStat[i][j] = 0;
			}
		}
		randomInitPoints();
	}
	*/
	
	public Creature(String name, String rName, int diet, Element consumes, Element produces, Element casing, Element fluid, Element organs, boolean genders, int numArms, int numLegs, int[][] cStat, int[][] bStat, ArrayList<Item> inventory, Item[] equipped, ArrayList<Item> weilding, int staminaMaxCapacity, int availHands, double weight, int initCap) {
		this.name = name;
		this.rName = rName;
		this.diet = diet;
		this.consumes = consumes;
		this.produces = produces;
		this.casing = casing;
		this.fluid = fluid;
		this.organs = organs;
		this.genders = genders;
		this.numArms = numArms;
		this.numLegs = numLegs;
		this.cStat = new int[cStats.TOTAL.ordinal()][sVal.TOTAL.ordinal()];
		for(int i=0; i<cStats.TOTAL.ordinal(); i++) {
			for(int j=0; j<sVal.TOTAL.ordinal(); j++) {
				this.cStat[i][j] = cStat[i][j];
			}
		}
		this.bStat = new int[bStats.TOTAL.ordinal()][sVal.TOTAL.ordinal()];
		for(int i=0; i<bStats.TOTAL.ordinal(); i++) {
			for(int j=0; j<sVal.TOTAL.ordinal(); j++) {
				this.bStat[i][j] = bStat[i][j];
			}
		}
		if(inventory != null) this.inventory = inventory;
		else this.inventory = new ArrayList<Item>(0);
		if(equipped != null) this.equipped = equipped;
		else this.equipped = new Item[slots];
		if(weilding != null) this.weilding = weilding;
		else this.weilding = new ArrayList<Item>(0);
		this.staminaMaxCapacity = staminaMaxCapacity;
		if(availHands <= this.numArms && availHands >= 0) this.availHands = availHands;
		else this.availHands = this.numArms;
		this.weight = weight;
		this.initCap = initCap;
	}
	
	public Creature(Race r) {
		cStats[] c = cStats.values();
		bStats[] b = bStats.values();
		sVal[] v = sVal.values();
		
		this.name = r.getName();
		this.rName = r.L.generate();
		this.diet = r.getDiet();
		this.consumes = r.getConsumes();
		this.produces = r.getProduces();
		this.casing = r.getCasing();
		this.fluid = r.getFluid();
		this.organs = r.getOrgans();
		this.genders = r.getGenders();
		this.numArms = r.getNumArms();
		this.numLegs = r.getNumLegs();
		this.cStat = new int[cStats.TOTAL.ordinal()][sVal.TOTAL.ordinal()];
		for(int i=0; i<cStats.TOTAL.ordinal(); i++) {
			for(int j=0; j<sVal.TOTAL.ordinal(); j++) {
				this.cStat[i][j] = r.get(c[i],v[j]);
			}
		}
		this.bStat = new int[bStats.TOTAL.ordinal()][sVal.TOTAL.ordinal()];
		for(int i=0; i<bStats.TOTAL.ordinal(); i++) {
			for(int j=0; j<sVal.TOTAL.ordinal(); j++) {
				this.bStat[i][j] = r.get(b[i],v[j]);
			}
		}
		
		this.inventory = new ArrayList<Item>(0);
		this.equipped = new Item[slots];
		this.weilding = new ArrayList<Item>(0);
		this.staminaMaxCapacity = bStat[bStats.STAMINA.ordinal()][sVal.CURRENT.ordinal()];
		this.availHands = this.numArms;
		this.weight = 0;
		
	}
	
	public int get(cStats s, sVal v) {
		return cStat[s.ordinal()][v.ordinal()];//[n];
	}
	
	public void set(cStats s,sVal v, int n) {
		cStat[s.ordinal()][v.ordinal()] = n;
		if(v == sVal.LEVEL) {
			this.cStat[s.ordinal()][sVal.MAX.ordinal()] = lvlGain*this.cStat[s.ordinal()][sVal.LEVEL.ordinal()];
			// current = max (unless temp effect has it above max already)
			if(this.cStat[s.ordinal()][sVal.CURRENT.ordinal()] < this.cStat[s.ordinal()][sVal.MAX.ordinal()]) {
				this.cStat[s.ordinal()][sVal.CURRENT.ordinal()] = this.cStat[s.ordinal()][sVal.MAX.ordinal()];
			}
		}

	}

	public int get(bStats s, sVal v) {
		return bStat[s.ordinal()][v.ordinal()];//[n];
	}
	
	public void set(bStats s,sVal v, int n) {
		bStat[s.ordinal()][v.ordinal()] = n;
		if(v == sVal.LEVEL) {
			this.bStat[s.ordinal()][sVal.MAX.ordinal()] = lvlGain*this.bStat[s.ordinal()][sVal.LEVEL.ordinal()];
			// current = max (unless temp effect has it above max already)
			if(this.bStat[s.ordinal()][sVal.CURRENT.ordinal()] < this.bStat[s.ordinal()][sVal.MAX.ordinal()]) {
				this.bStat[s.ordinal()][sVal.CURRENT.ordinal()] = this.bStat[s.ordinal()][sVal.MAX.ordinal()];
			}
		}
	}	
	
	public String getName() {
		return this.name;
	}
	
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getRName() {
		return this.rName;
	}
	
	public void setRName(String rName) {
		this.rName = rName;
	}
	
	public int getNumArms() {
		return this.numArms;
	}
	
	public void setNumArms(int n) {
		this.numArms = n;
	}
	
	public int getNumLegs() {
		return this.numLegs;
	}
	
	public void setNumLegs(int n) {
		this.numLegs = n;
	}
	
	public int getEffective(cStats s, sVal v) {
		//returns effective value for stat (useful for current/max)
		int eff = (int) (base_creat_ratio*bStat[decode(s).ordinal()][v.ordinal()] + cStat[s.ordinal()][v.ordinal()]);
		if(v == sVal.CURRENT) {
			
			if(s == cStats.SPEED_MOVE && (weight > staminaMaxCapacity)) {
				eff = (int) Math.max(Math.min((1-(weight-this.staminaMaxCapacity)/(this.staminaMaxCapacity*2)), 1),0);
			}
		}
		return eff;
	}
	
	public int getDiet() {
		return this.diet;
	}
	
	public void setDiet(int diet) {
		this.diet = diet;
	}
	
	public Element getConsumes() {
		return this.consumes;
	}
	
	public void setConsumes(Element e) {
		this.consumes = e;
	}
	
	public Element getProduces() {
		return this.produces;
	}
	
	public void setProduces(Element e) {
		this.produces = e;
	}
	
	public Element getCasing() {
		return this.casing;
	}
	
	public void setCasing(Element e) {
		this.casing = e;
	}
	
	public Element getFluid() {
		return this.fluid;
	}
	
	public void setFluid(Element e) {
		this.fluid = e;
	}
	
	public Element getOrgans() {
		return this.organs;
	}
	
	public void setOrgans(Element e) {
		this.organs = e;
	}
	
	public boolean getGenders() {
		return this.genders;
	}
	
	public void setGenders(boolean b) {
		this.genders = b;
	}

	
	
//////////RANDOM/AUTO LEVELING//////////////////////////////////////////////
	public void randomInitXP() {
		int total = 0, xp, min = cStat[0][sVal.LEVEL.ordinal()], max = cStat[0][sVal.LEVEL.ordinal()], stat;
		cStats c[] = cStats.values();
		for(int i = 0; i < cStats.TOTAL.ordinal(); i++) {
			total += cStat[i][sVal.LEVEL.ordinal()]; 
			if(min > cStat[i][sVal.LEVEL.ordinal()]) {
				min = cStat[i][sVal.LEVEL.ordinal()];
			}
			if(max < cStat[i][sVal.LEVEL.ordinal()]) {
				max = cStat[i][sVal.LEVEL.ordinal()];
			}
		}
		while(total < initCap) {
			xp = (int) (mul*Math.pow((min+max)/2+1,rate)*Math.random());// can't level up by anymore than 1 level (lowest level)
			stat = (int) (cStats.TOTAL.ordinal()*Math.random());
			gain(c[stat],sVal.XP,xp);
			total = 0;
			for(int i = 0; i < cStats.TOTAL.ordinal(); i++) {
				min = cStat[i][sVal.LEVEL.ordinal()];
				total += cStat[i][sVal.LEVEL.ordinal()]; 
				if(min > cStat[i][sVal.LEVEL.ordinal()]) {
					min = cStat[i][sVal.LEVEL.ordinal()];
				}
				if(max < cStat[i][sVal.LEVEL.ordinal()]) {
					max = cStat[i][sVal.LEVEL.ordinal()];
				}
			}
		}
	}
	
	public void randomInitPoints() {
		int total = 0, stat;
		cStats c[] = cStats.values();
		for(int i = 0; i < cStats.TOTAL.ordinal(); i++) {
			total += cStat[i][sVal.LEVEL.ordinal()]; // = cStats.values()[i].ordinal();
		}
		for(int i = total; i <= initCap; i++) {
			stat = (int) (cStats.TOTAL.ordinal()*Math.random());
			gain(c[stat],sVal.LEVEL,1);
		}
	}

	public void autoLvlPointsBy(int levels) {
		int stat;
		cStats c[] = cStats.values();
		for(int i = 0; i <= levels; i++) {
			stat = (int) (cStats.TOTAL.ordinal()*Math.random());
			gain(c[stat],sVal.LEVEL,1);
		}
	}

	public void autoLvlPointsTo(int levels) {
		int total = 0, stat;
		cStats c[] = cStats.values();
		for(int i = 0; i < cStats.TOTAL.ordinal(); i++) {
			total += cStat[i][sVal.LEVEL.ordinal()]; 
		}
		for(int i = total; i <= levels; i++) {
			stat = (int) (cStats.TOTAL.ordinal()*Math.random());
			gain(c[stat],sVal.LEVEL,1);
		}
	}

	public void autoLvlXP(int xp) {
		// randomly allocating chunks of xp/100 
		int div = 100;
		int total = 0, stat, givenXP = 0;
		cStats c[] = cStats.values();
		while(total < xp) {
			givenXP = (int) ((10*Math.random())*div*(Math.random()));// can't level up by anymore than 1 level (lowest level)
			if(givenXP > (xp - total)) givenXP = (xp-total);
			
			stat = (int) (cStats.TOTAL.ordinal()*Math.random());
			if(givenXP < (xp - total)) {
				gain(c[stat],sVal.XP,givenXP);
				total += givenXP;
			}
			else {
				gain(c[stat],sVal.XP,xp-total);
				total = xp;
			}
		}
	}

	
//Gaining stats/leveling up///////////////////////////////////////////////
	//Gain/lose health,energy,stats,XP etc
	public int gain(cStats s, sVal v, int n) {
		if(v == sVal.XP) { 
			bStats b = decode(s);
			gain(b, v, n/baseGain); //Base stats gain XP in corresponding stat, divided by number of cstats that map to it
			this.cStat[s.ordinal()][v.ordinal()] += n;
		}
		else {
			this.cStat[s.ordinal()][v.ordinal()] += n;
			if(v == sVal.LEVEL) {
				if(cStat[s.ordinal()][v.ordinal()] > maxLvl) cStat[s.ordinal()][sVal.LEVEL.ordinal()] = maxLvl;
				
				this.cStat[s.ordinal()][sVal.MAX.ordinal()] = lvlGain*this.cStat[s.ordinal()][sVal.LEVEL.ordinal()];
				// current = max (unless temp effect has it above max already)
				if(this.cStat[s.ordinal()][sVal.CURRENT.ordinal()] < this.cStat[s.ordinal()][sVal.MAX.ordinal()]) {
					this.cStat[s.ordinal()][sVal.CURRENT.ordinal()] = this.cStat[s.ordinal()][sVal.MAX.ordinal()];
				}	
			}
		}
		return checkXP(s);
	}

	//Same as above but for base stats
	private void gain(bStats s, sVal v, int n) {
		if(v == sVal.XP) { 
			this.bStat[s.ordinal()][v.ordinal()] += n;
		}
		else {
			this.bStat[s.ordinal()][v.ordinal()] += n;
			if(v == sVal.LEVEL) {
				if(bStat[s.ordinal()][v.ordinal()] > maxLvl) bStat[s.ordinal()][sVal.LEVEL.ordinal()] = maxLvl;
				
				this.bStat[s.ordinal()][sVal.MAX.ordinal()] = lvlGain*this.bStat[s.ordinal()][sVal.LEVEL.ordinal()];
				// current = max (unless temp effect has it above max already)
				if(this.bStat[s.ordinal()][sVal.CURRENT.ordinal()] < this.bStat[s.ordinal()][sVal.MAX.ordinal()]) {
					this.bStat[s.ordinal()][sVal.CURRENT.ordinal()] = this.bStat[s.ordinal()][sVal.MAX.ordinal()];
				}	
			}

		}
		checkXP(s);
	}
	
	//If XP >= mul*(lvl)^rate, then lvlUP
	private int checkXP(cStats s) {
		int temp = 0;
		//while XP >= max for level, level up
		if(cStat[s.ordinal()][sVal.LEVEL.ordinal()] >= maxLvl) {
			cStat[s.ordinal()][sVal.LEVEL.ordinal()] = maxLvl;
			temp = cStat[s.ordinal()][sVal.XP.ordinal()];
			cStat[s.ordinal()][sVal.XP.ordinal()] = 0;
			return temp;
		}
		while( (this.cStat[s.ordinal()][sVal.XP.ordinal()]) >= ((int) mul*Math.pow(this.cStat[s.ordinal()][sVal.LEVEL.ordinal()],rate)) ){
			if(cStat[s.ordinal()][sVal.LEVEL.ordinal()] >= maxLvl) { //it is about to level up
				cStat[s.ordinal()][sVal.LEVEL.ordinal()] = maxLvl;
				temp = cStat[s.ordinal()][sVal.XP.ordinal()];
				cStat[s.ordinal()][sVal.XP.ordinal()] = 0;
				return temp;
			}
			else {
				lvlUp(s);
			}
		}
		return temp;
	}

	//If XP >= mul*(lvl)^rate, then lvlUP
	private void checkXP(bStats s) {
		//while XP >= max for level, level up
		while( this.bStat[s.ordinal()][sVal.XP.ordinal()] >= ((int) mul*Math.pow(this.bStat[s.ordinal()][sVal.LEVEL.ordinal()],rate)) ){
			if(bStat[s.ordinal()][sVal.LEVEL.ordinal()] >= maxLvl) { //it is about to level up past maxLvl
				bStat[s.ordinal()][sVal.LEVEL.ordinal()] = maxLvl;
				bStat[s.ordinal()][sVal.XP.ordinal()] = 0;
			}
			else {
				lvlUp(s);
			}
		}
	}
	
	
	private void lvlUp(cStats s) {
		//Incrementlevel and decrement XP by XP cap for level;
		this.cStat[s.ordinal()][sVal.XP.ordinal()] -= ((int) mul*Math.pow(this.cStat[s.ordinal()][sVal.LEVEL.ordinal()],rate));
		this.cStat[s.ordinal()][sVal.LEVEL.ordinal()]++;
		this.cStat[s.ordinal()][sVal.MAX.ordinal()] += lvlGain;
		// current = max (unless temp effect has it above max already)
		if(this.cStat[s.ordinal()][sVal.CURRENT.ordinal()] < this.cStat[s.ordinal()][sVal.MAX.ordinal()]) {
			this.cStat[s.ordinal()][sVal.CURRENT.ordinal()] = this.cStat[s.ordinal()][sVal.MAX.ordinal()];
		}
	}

	private void lvlUp(bStats s) {
		//Incrementlevel and decrement XP by XP cap for level;
		this.bStat[s.ordinal()][sVal.XP.ordinal()] -= ((int) mul*Math.pow(this.bStat[s.ordinal()][sVal.LEVEL.ordinal()],rate));
		this.bStat[s.ordinal()][sVal.LEVEL.ordinal()]++;
		this.bStat[s.ordinal()][sVal.MAX.ordinal()] += lvlGain;
		// current = max (unless temp effect has it above max already)
		if(this.bStat[s.ordinal()][sVal.CURRENT.ordinal()] < this.bStat[s.ordinal()][sVal.MAX.ordinal()]) {
			this.bStat[s.ordinal()][sVal.CURRENT.ordinal()] = this.bStat[s.ordinal()][sVal.MAX.ordinal()];
		}
	}
	
//Decode cStats into the corresponding bStats //////////////////////////////////
	public bStats decode(cStats s) {
		String temp[], str[] = s.toString().split("_");
		bStats names[] = bStats.values();
		cStats cnames[] = cStats.values();
		this.baseGain = 0;
		int rNum = -1;
		for(int i=0; i<bStats.TOTAL.ordinal(); i++) {
			//if(str[0].compareTo(names[i].toString()) == 0) {
			if((str[0].substring(0, 3)).compareTo((names[i].name().substring(0, 3))) == 0) {
				rNum = i;
				for(int j=0; j<cStats.TOTAL.ordinal(); j++) {
					temp = cnames[j].toString().split("_");
					//compares the first 3 characters between 
					if(str[0].substring(0, 3).compareTo(temp[0].substring(0, 3)) == 0) {
						this.baseGain++;
					}
				}
			}
		}
		//System.out.println(str[0]);
		if( (rNum > bStats.TOTAL.ordinal()) || (rNum < 0)) {
			return null;
		}
		return names[rNum];
	}
	
	public int getBaseGain() {
		return this.baseGain;
	}

/////////////////////////Attack methods//////////////////////////////////////
	public void attack(Square s) {
		for(int i=0; i<this.getNumArms(); i++) {
			//slots holds the number of non-arm slots in equipped
			// for each arm-slot, attack s
			this.equipped[slots+i].attack(s,getEffective(cStats.STR_PHYS_ATTACK,sVal.CURRENT),getEffective(cStats.TECH_WEAPON,sVal.CURRENT));
			//this.stat[cStats.STR_PHYS_ATTACK.ordinal()][sVal.CURRENT.ordinal()],this.stat[cStats.TECH_WEAPON.ordinal()][sVal.CURRENT.ordinal()]);
		}
	}
	
	
	public void takeDamage(Item i, int dmg) {
		for(Element e: i.getConsists()) {
			
		}
		//get resistances from universal reaction table
		
		//reflectDamage()
	}
	
	private int reflectDamage(Item i, int dmg) {
		// damage will be based on item elements/strength and how much damage is being dealt (harder hit = more damage to item as well)
		// also based on creature stats/armor
		
		return 0;
	}
	
///////////////////////// Inventory Stuff ////////////////////////////////////////////
	public boolean equip(Item i) {
		//equip if has required tech and enough hands (if applicable)
		if(i.getType() == iType.HAND) {
			if(availHands >= i.getHands() && getEffective(cStats.TECH_WEAPON,sVal.CURRENT) >= i.getTechRequired()) {
				weilding.add(i);
				availHands -= i.getHands();
				return true;
			}
			else {
				return false;
			}
		}
		else if(getEffective(cStats.TECH_ARMOR,sVal.CURRENT) >= i.getTechRequired()){
			equipped[i.getType().ordinal()] = i;
			return true;
		}
		return false;
	}

	public boolean unequip(Item i) {
		if(i.getType() == iType.HAND) {
			if(weilding.remove(i)) {
				availHands += i.getHands();
				return true;
			}
			else {
				return false;
			}
		}
		else {
			equipped[i.getType().ordinal()] = null;
			return true;
		}
	}

	public void pickup(Item i) {
		this.inventory.add(i);
		weight += i.getWeight();
	}
	
	public void drop(Item i) {
		this.inventory.remove(i);
		weight -= i.getWeight();
	}
	
	public ArrayList<Item> getInventory() {
		return this.inventory;
	}


	public String getEquipped(int i){
		if(i>=this.weilding.size() || i < 0) return "ERROR";
		else return this.weilding.get(i).getName();
	}
	
	public String getEquipped(Item i) {
		if(i.getType() != iType.HAND) {
			if(i.getType() == iType.TOTAL) return "Don't pass TOTAL to this";
			
			if(equipped[i.getType().ordinal()] == null) return "None";
			else return equipped[i.getType().ordinal()].getName();
		}
		else {
			return getEquipped(0); //get default (first) weapon
		}
	}
}
