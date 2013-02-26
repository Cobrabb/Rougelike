package roguelike.orig;
import java.lang.Math;

public class Element {

	private String name; //name of the element
	private double density; //density
	private double granularity; //or viscosity
	private double mall; //how easy it gets smashed, I'm not going to spell the actual word
	static enum basephase {GAS, LIQUID, SOLID};
	
	public String getName(){
		return this.name;
	}
	
	public double getDensity(){
		return this.density;
	}
	
	public double getMall(){
		return this.mall;
	}
	
	public double getGranularity(){
		return this.granularity;
	}
	
	public Element(){ //completely random constructor
		Language L = new Language();
		int i = (int)(Math.random()*6)+4;
		name = L.generate(i);
		this.density = Math.pow((Math.random()*200),.5);
	}

	public Element(Language L){ //random, given a language
		int i = (int)(Math.random()*6)+4;
		name = L.generate(i);
		this.density = Math.pow((Math.random()*200),.5);
	}
	
	public Element(String name, double density){
		this.name = name;
		this.density = density;
	}
	
	public int compareTo(Element e){
		return (int)(this.density - e.getDensity());
	}
	
	public String toString(){
		return "This is the element "+name+". It has a density of "+density+".";
	}
}
