package orig;

import orig.Creature.cStats;
import orig.Creature.bStats;
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
	

	private int cStat[][];
	private int bStat[][];
	
	//behind the scenes
	final double mul = 10;
	final double rate = 1.15;
	final int lvlGain = 1;
	final int numdiets = 4; //the possible diet options 
	
	public Race(){ //completely random, probably not necessary.
		this.L = new Language();
		this.name = L.generate();
		this.cStat = new int[cStats.TOTAL.ordinal()][sVal.TOTAL.ordinal()];
		this.bStat = new int[bStats.TOTAL.ordinal()][sVal.TOTAL.ordinal()];
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
		numArms = 4;
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
	
	public void gain(cStats s, sVal v, int n) {
		this.cStat[s.ordinal()][v.ordinal()] += n;
		checkXP(s);
	}
	
	private void gain(bStats s, sVal v, int n) {
		this.bStat[s.ordinal()][v.ordinal()] += n;
		checkXP(s);
	}
	
	//If XP >= mul*(lvl)^rate, then lvlUP
	private void checkXP(cStats s) {
		//while XP >= max for level, level up
		while( (this.cStat[s.ordinal()][sVal.XP.ordinal()]) >= ((int) mul*Math.pow(this.cStat[s.ordinal()][sVal.LEVEL.ordinal()],rate)) ){
			//System.out.println(stat[s.ordinal()][sVal.XP.ordinal()] + ", " + (int) (mul*Math.pow(this.stat[s.ordinal()][sVal.LEVEL.ordinal()],rate)));
			lvlUp(s, mul, rate);
		}
	}
	
	//If XP >= mul*(lvl)^rate, then lvlUP
	private void checkXP(bStats s) {
		//while XP >= max for level, level up
		while( (this.bStat[s.ordinal()][sVal.XP.ordinal()]) >= ((int) mul*Math.pow(this.bStat[s.ordinal()][sVal.LEVEL.ordinal()],rate)) ){
			//System.out.println(stat[s.ordinal()][sVal.XP.ordinal()] + ", " + (int) (mul*Math.pow(this.stat[s.ordinal()][sVal.LEVEL.ordinal()],rate)));
			lvlUp(s, mul, rate);
		}
	}

	private void lvlUp(cStats s, double mul, double rate) {
		//Incrementlevel and decrement XP by XP cap for level;
		this.cStat[s.ordinal()][sVal.XP.ordinal()] -= ((int) mul*Math.pow(this.cStat[s.ordinal()][sVal.LEVEL.ordinal()],rate));
		this.cStat[s.ordinal()][sVal.LEVEL.ordinal()]++;
		this.cStat[s.ordinal()][sVal.MAX.ordinal()] += lvlGain;
		if(this.cStat[s.ordinal()][sVal.CURRENT.ordinal()] < this.cStat[s.ordinal()][sVal.MAX.ordinal()]) {
			this.cStat[s.ordinal()][sVal.CURRENT.ordinal()] = this.cStat[s.ordinal()][sVal.MAX.ordinal()];
		}
	}
	
	private void lvlUp(bStats s, double mul, double rate) {
		//Incrementlevel and decrement XP by XP cap for level;
		this.bStat[s.ordinal()][sVal.XP.ordinal()] -= ((int) mul*Math.pow(this.bStat[s.ordinal()][sVal.LEVEL.ordinal()],rate));
		this.bStat[s.ordinal()][sVal.LEVEL.ordinal()]++;
		this.bStat[s.ordinal()][sVal.MAX.ordinal()] += lvlGain;
		if(this.bStat[s.ordinal()][sVal.CURRENT.ordinal()] < this.bStat[s.ordinal()][sVal.MAX.ordinal()]) {
			this.bStat[s.ordinal()][sVal.CURRENT.ordinal()] = this.bStat[s.ordinal()][sVal.MAX.ordinal()];
		}
	}
	
	public Creature spawn() {
		Creature c = new Creature(name, L.generate(),diet,consumes,produces,casing,fluid,organs,genders,numArms,numLegs,cStat,bStat,null,null,null,bStat[bStats.STAMINA.ordinal()][sVal.CURRENT.ordinal()],numArms,0,0);
		return c;
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

