package orig;
import java.lang.Math;

public class Element {

	private String name; //name of the element
	private double density; //density
	private double mal;
	private double granularity;
	
	
	public String getName(){
		return this.name;
	}
	
	public double getDensity(){
		return this.density;
	}

	public double getGranularity(){
		return this.granularity;
	}
	
	public double getMal(){
		return this.mal;
	}
	
	
	public Element(){ //completely random constructor
		Language L = new Language();
		int i = (int)(Math.random()*6)+4;
		name = L.generate(i);
		this.density = Math.pow((Math.random()*200),.5);
		this.granularity = Math.pow((Math.random()*200),.5);
		this.mal = Math.pow((Math.random()*200),.5);
		
		
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
	
	public Element(String name, double density, double granularity, double mal){
		this.name = name;
		this.density = density;
		this.granularity = granularity;
		this.mal = mal;
	
	}
	
	public int compareTo(Element e){
		return (int)(this.density - e.getDensity());
	}
	
	public String toString(){
		return "This is the element "+name+". It has a density of "+density+".";
	}
}
