package orig;

import orig.Creature.cVal;

public class Race {
	public enum rStats {
		STRENGTH,SPEED,DETECTION,STEALTH,TECH,STAMINA,TOTAL
	}
	public enum rVal {
		LEVEL,XP,MAX,CURRENT,TOTAL
	}

	private String name;
	private int diet;  //How it eats, not what it eats
	private Element consumes; //what it eats
	private Element produces; //what it poops
	private Element casing; //the flesh
	private Element fluid; //the blood
	private Element organs;
	private boolean genders; //true if the Race is not a hermaphodite
	final int numdiets = 4; //the possible diet options 
	private int numArms;
	private int numLegs;
	
	private int initCap; //initial cap on stat points

	private int stat[][];
	
	public Race(){ //completely random, probably not necessary. 
	}

	public Race(Element[] e, Language L){ //semi random
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
		this.stat = new int[rStats.TOTAL.ordinal()][rVal.TOTAL.ordinal()];
		//initialize stat to 0's
		for(int j=0; j<rStats.TOTAL.ordinal(); j++) {
			for(int k=0; k<rVal.TOTAL.ordinal(); k++) {
				this.stat[j][k] = 0;
			}
		}
		this.numArms = 2*(int) (Math.random()/.2);
		this.numLegs = 2*(int) (Math.random()/.4);
	}

	public Race(String name, int diet, Element produces, Element consumes, Element casing, Element fluid, Element organs, int numArms, int numLegs){
		this.name = name;
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
		this.stat = new int[rStats.TOTAL.ordinal()][rVal.TOTAL.ordinal()];
		//initialize stat to 0's
		for(int i=0; i<rStats.TOTAL.ordinal(); i++) {
			for(int j=0; j<rVal.TOTAL.ordinal(); j++) {
				this.stat[i][j] = 0;
			}
		}
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
	public int get(rStats s, rVal v) {
		return stat[s.ordinal()][v.ordinal()];
	}
	
	public void set(rStats s, rVal v, int n) {
		stat[s.ordinal()][v.ordinal()] = n;
	}

	public void gain(rStats s, rVal v, int n) {
		this.stat[s.ordinal()][v.ordinal()] += n;
		checkXP(s);
	}
	
	//if  XP >= mul*(lvl)^rate, then lvlUp
	public void checkXP(rStats s) {
		double mul = 10; 
		double rate = 1.15; 
		boolean test =	(this.stat[s.ordinal()][rVal.XP.ordinal()]) >= ((int) mul*Math.pow(this.stat[s.ordinal()][rVal.LEVEL.ordinal()],rate));
		if(test) {
			lvlUp(s);
		}
	}
	
	public void lvlUp(rStats s) {
		this.stat[s.ordinal()][rVal.LEVEL.ordinal()]++;
		this.stat[s.ordinal()][rVal.XP.ordinal()] = 0;
	}
	

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

