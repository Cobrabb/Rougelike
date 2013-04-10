package orig;

import java.util.ArrayList;

import orig.Creature.bStats;
import orig.Creature.cStats;
import orig.Creature.sVal;

public class Race {

	public Language L;
	
	private String name;
	private int diet;  //How it eats, not what it eats
	private Element consumes; //what it eats
	private Element produces; //what it poops
	private Element casing; //the flesh
	private Element fluid; //the blood
	private Element organs;
	private boolean genders; //true if the Race is not a hermaphodite
	private int numArms;
	private int numLegs;
	private ArrayList<Effect> effects;
	private ArrayList<Race> friendly;

	private int cStat[][];
	private int bStat[][];
	
	// behind the scenes
	final double base_creat_ratio = .2; //race stats * .2 + creat stats = effective
	final double mul = 10;
	final double rate = 1.15;
	final int numdiets = 4; //the possible diet options
	final int slots = 3;
	final int lvlGain = 1;
	private int maxLvl = 100;
	private int raceGain;
	private int initCap = 400;
	
	
	public Race(){ //completely random, probably not necessary.
		this(UET.getUET().getElementArray(),new Language());//makes a new race from all elements and a new language
		autoLvlXP(500*cStats.TOTAL.ordinal()); // allocate 500 per stat, not allocated evenly
	}

	public Race(Element[] e, Language L){ //semi random
		this.L = L;
		this.name  = L.generate();
		int i = (int)(Math.random()*e.length);
		this.casing = e[i];
		i = (int)(Math.random()*e.length);
		this.fluid = e[i];
		int count = 0;
		while(this.fluid.getDensity()>this.casing.getDensity()){ //organs and fluid must be less dense than the casing, otherwise we have problems.
			i = (int)(Math.random()*e.length);
			this.fluid = e[i];
			if(count>20){
				this.fluid = this.casing;
			}
			count++;
		}
		i = (int)(Math.random()*e.length);
		this.organs = e[i];
		while(this.organs.getDensity()>this.casing.getDensity()){
			i = (int)(Math.random()*e.length);
			
			this.organs = e[i];
			if(count>40){ //hard limit on how long it can try to get good organs
				this.organs = this.casing;
			}
			count++;
		}
		i = (int)(Math.random()*4+1);
		this.diet = i;
		if(this.diet == 1){ //if diet is 1, it either eats nothing and makes nothing, or it eats and makes the same thing.
			if(Math.random()<.5){
				this.produces = null;
				this.consumes = null;
			}
			else{
				i = (int)(Math.random()*e.length);
				this.produces = this.consumes = e[i];
			}
		}
		else if(this.diet == 2){ //if diet is 2, it consumes nothing and produces something
			this.consumes = null;
			i = (int)(Math.random()*e.length);
			this.produces = e[i];
		}
		else if(this.diet ==3){ //consumes something and produces nothing (useful)
			this.produces = null;
			i = (int)(Math.random()*e.length);
			this.consumes = e[i];
		}
		else{ //consumes something and produces something else
			i = (int)(Math.random()*e.length);
			this.produces = e[i];
			i = (int)(Math.random()*e.length);
			this.consumes = e[i];
		}
		this.genders = true;
		if(Math.random()<.5){
			this.genders = false;
		}
		this.cStat = new int[cStats.TOTAL.ordinal()][sVal.TOTAL.ordinal()];
		this.bStat = new int[bStats.TOTAL.ordinal()][sVal.TOTAL.ordinal()];
		
		//initialize stat to 0's
		for(int j=0; j<cStats.TOTAL.ordinal(); j++) {
			for(int k=0; k<sVal.TOTAL.ordinal(); k++) {
				this.cStat[j][k] = 0;
			}
		}		
		for(int j=0; j<bStats.TOTAL.ordinal(); j++) {
			for(int k=0; k<sVal.TOTAL.ordinal(); k++) {
				this.bStat[j][k] = 0;
			}
		}

		this.numArms = 2*(int) (Math.random()/.2)+1;
		this.numLegs = 2*(int) (Math.random()/.4);
		this.friendly = new ArrayList<Race>(0);
		this.effects = new ArrayList<Effect>(0);
		while(Math.random() < .3) {
			this.effects.add(new Effect());
		}
	}

	public Race(String name, int diet, Element produces, Element consumes, Element casing, Element fluid, Element organs, int numArms, int numLegs, ArrayList<Race> friendly){
		this.name = name;
		
		L =new Language();
		this.effects = new ArrayList<Effect>(0);
		if(diet>numdiets||diet<=0){
			this.diet = 1;
		}
		else this.diet = diet;
		this.produces = produces;
		this.consumes = consumes;
		this.casing = casing;
		this.fluid = fluid;
		this.organs = organs;
		this.numArms = numArms;
		this.numLegs = numLegs;
		this.cStat = new int[cStats.TOTAL.ordinal()][sVal.TOTAL.ordinal()];
		this.bStat = new int[bStats.TOTAL.ordinal()][sVal.TOTAL.ordinal()];
		
		//initialize stat to 0's
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
		
		this.friendly = new ArrayList<Race>(0);
		for(int i=0; i<friendly.size();i++){
			this.friendly.add(friendly.get(i));
		}
	}
	
	public boolean isFriendly(Race r) {
		for(int i=0; i<this.friendly.size();i++) {
			if(this.friendly.get(i) == r) return true;
		}
		return false;
	}
	
	public void addFriendly(Race r) {
		for(int i=0; i<this.friendly.size();i++) {
			if(this.friendly.get(i) == r) return;
		}
		this.friendly.add(r);
	}
	public ArrayList<Effect> getEffects() {
		return this.effects;
	}
	
	public void addEffect(Effect e) {
		this.effects.add(e);
	}
	
	public ArrayList<Race> getFriendly() {
		return this.friendly;
	}
	
	public String getName(){
		return this.name;
	}

	public Element getCasing(){
		return this.casing;
	}

	public Element getFluid(){
		return this.fluid;
	}

	public Element getOrgans(){
		return this.organs;
	}

	public int getDiet(){
		return this.diet;
	}

	public Element getConsumes(){
		return this.consumes;
	}

	public Element getProduces(){
		return this.produces;
	}

	public boolean getGenders(){
		return this.genders;
	}

	public int getNumArms() {
		return this.numArms;
	}
	
	public  void setNumArms(int n) {
		this.numArms = n;
	}
	
	public int getNumLegs() {
		return this.numLegs;
	}
	
	public void setNumLegs(int n) {
		this.numLegs = n;
	}
	public int get(cStats s, sVal v) {
		return cStat[s.ordinal()][v.ordinal()];
	}
	
	public void set(cStats s, sVal v, int n) {
		cStat[s.ordinal()][v.ordinal()] = n;
	}
	
	public int get(bStats s, sVal v) {
		return bStat[s.ordinal()][v.ordinal()];
	}
	
	public void set(bStats s, sVal v, int n) {
		bStat[s.ordinal()][v.ordinal()] = n;
	}

	public Language getL() {
		return this.L;
	}
	
	//Random_auto leveling
		public void randomInitXP() {
			int total = 0, xp, min = cStat[0][sVal.LEVEL.ordinal()], max = cStat[0][sVal.LEVEL.ordinal()], stat;
			cStats c[] = cStats.values();
			for(int i = 0; i < cStats.TOTAL.ordinal(); i++) {
				total += cStat[i][sVal.LEVEL.ordinal()]; // = cStats.values()[i].ordinal();
				if(min > cStat[i][sVal.LEVEL.ordinal()]) {
					min = cStat[i][sVal.LEVEL.ordinal()];
				}
				if(max < cStat[i][sVal.LEVEL.ordinal()]) {
					max = cStat[i][sVal.LEVEL.ordinal()];
				}
			}
			while(total < initCap) {
				xp = (int) (mul*Math.pow((min+max)/2+1,rate)*Math.random());// can't level up by anymore than 1 level (lowest level)
				//System.out.println("Total = " + total + "\nXP = " + xp);
				stat = (int) (cStats.TOTAL.ordinal()*Math.random());
				gain(c[stat],sVal.XP,xp);
				total = 0;
				for(int i = 0; i < cStats.TOTAL.ordinal(); i++) {
					min = cStat[i][sVal.LEVEL.ordinal()];
					total += cStat[i][sVal.LEVEL.ordinal()]; // = cStats.values()[i].ordinal();
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
				total += cStat[i][sVal.LEVEL.ordinal()]; // = cStats.values()[i].ordinal();
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
				givenXP = (int) ((10*Math.random())*div*(Math.random()));
				if(givenXP > (xp - total))
					givenXP = (xp-total);
				
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
				gain(b, v, n/raceGain); //Race gains XP in corresponding stat, divided by number of cstats that map to it
				this.cStat[s.ordinal()][v.ordinal()] += n;
			}
			else {
				this.cStat[s.ordinal()][v.ordinal()] += n;
				if(v == sVal.LEVEL  && cStat[s.ordinal()][v.ordinal()] > maxLvl) { 
					cStat[s.ordinal()][sVal.LEVEL.ordinal()] = maxLvl; 
				}
			}
			return checkXP(s);
		}

		//Same as above but for base stats
		private void gain(bStats s, sVal v, int n) {
			if(v == sVal.XP) { 
				this.bStat[s.ordinal()][v.ordinal()] += n;
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
			this.raceGain = 0;
			int rNum = -1;
			for(int i=0; i<bStats.TOTAL.ordinal(); i++) {
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
			if( (rNum > bStats.TOTAL.ordinal()) || (rNum < 0)) {
				return null;
			}
			return names[rNum];
		}
		
		public int getRaceGain() {
			return this.raceGain;
		}
/*
	public Creature spawn() {
		Creature c = new Creature(name, L.generate(),diet,consumes,produces,casing,fluid,organs,genders,numArms,numLegs,cStat,bStat,null,null,null,bStat[bStats.STAMINA.ordinal()][sVal.CURRENT.ordinal()],numArms,0,0);
		return c;
	}
*/	
	public String toString(){
		String str = "This is the Race "+name+". Its flesh is "+casing.getName()+". Its blood is "+fluid.getName()+". Its organs are "+organs.getName()+". It consumes ";
		if(consumes==null){
			str+="nothing";
		}
		else{
			str+=consumes.getName();
		}
		str+=", and produces ";
		if(produces==null){
			str+="nothing";
		}
		else{
			str+=produces.getName();
		}
		str+=". It is a ";
		if(genders){
			str+="gendered";
		}
		else{
			str+="hermaphroditic";
		}
		str+=" species.";
		return str;
	}

}

