package orig;
import java.io.Serializable;

public class Element implements Serializable {

	private static final long serialVersionUID = 692007231011169197L;
	
	private String name; //name of the element
	private double density; //density
	
	public String getName(){
		return this.name;
	}
	
	public double getDensity(){
		return this.density;
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
