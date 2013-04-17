package orig;

import java.util.ArrayList;
import java.util.HashMap;

import orig.Element;
import orig.Language;

public class UET {
	private static UET uET = null;
	
	private final Language l;
	public static final int ICE = 0;
	public static final int SILVERBRICK = 1;
	public final int COBBLESTONE = 2;
	public final int WATER = 3;
	public final int MEAT = 4;
	public final int STONE = 5;
	public final int FUDGE = 6;
	public final int METAL = 7;
	public final int DIRT = 8;
	public final int WOOD = 9;
	
	public static final int TOTAL = 25;
	private ArrayList<Element> elementList = new ArrayList<Element>();
	
	
	
	double dmgRatioMultiplier[][] = new double[TOTAL][TOTAL];
	
	private UET()
	{
		String temp = "";
		for(char c = 'a'; c != 'Z'+1; c++) {
			temp += c;
			if(c == 'z'){
				c = 'A'-1;
			}
		}
		Element elem1 = new Element("ice", 1.0);
		Element elem2 = new Element("silver brick", 2.0);
		Element elem3 = new Element("cobblestone", 2.0);
	
		l = new Language("Language",0,temp,true);
		elementList.add(elem1);
		elementList.add(elem2);
		elementList.add(elem3);
		
		elementList.add(new Element("Water", 1 ,1 , 1));
		elementList.add(new Element("Meat", 2, 1 , 1));
		elementList.add(new Element("Stone", 3.2 ,1 , 1));
		elementList.add(new Element("Fudge", 1.5 ,1 , 1));
		elementList.add(new Element("Metal", 5 ,1 , 1));
		elementList.add(new Element("Dirt", 2.5 , 9 , 1));
		elementList.add(new Element("Wood", 2.5 , 2 , 1));
		while(elementList.size() < TOTAL)
		{
			elementList.add(new Element(l));
		}
		
		
		
		for(int i=0; i<elementList.size(); i++) {
			for(int j=0; j<elementList.size(); j++) {
				this.dmgRatioMultiplier[i][j] = 1;
			}
		}
		dmgRatioMultiplier[WATER][STONE] = .5;
		dmgRatioMultiplier[WATER][METAL] = .1;//RUSTING
		dmgRatioMultiplier[WATER][DIRT] = -2;//HEALS
		dmgRatioMultiplier[WATER][WOOD] = .2;
		dmgRatioMultiplier[MEAT][STONE] = .75;
		dmgRatioMultiplier[MEAT][FUDGE] = 1.2;
		dmgRatioMultiplier[MEAT][METAL] = .3;
		dmgRatioMultiplier[MEAT][WOOD] = 1.5;
		dmgRatioMultiplier[STONE][WATER] = .5;
		dmgRatioMultiplier[STONE][MEAT] = 1.5;
		dmgRatioMultiplier[STONE][FUDGE] = 1.7;
		dmgRatioMultiplier[STONE][METAL] = .5;
		dmgRatioMultiplier[STONE][WOOD] = 1.2;
		dmgRatioMultiplier[FUDGE][WATER] = .5;
		dmgRatioMultiplier[FUDGE][MEAT] = -.5;
		dmgRatioMultiplier[FUDGE][STONE] = .5;
		dmgRatioMultiplier[FUDGE][METAL] = .1;
		dmgRatioMultiplier[FUDGE][DIRT] = .5;
		dmgRatioMultiplier[FUDGE][WOOD] = .5;
		dmgRatioMultiplier[METAL][MEAT] = 3;
		dmgRatioMultiplier[METAL][STONE] = 1.7;
		dmgRatioMultiplier[METAL][FUDGE] = 4;
		dmgRatioMultiplier[METAL][WOOD] = 2.5;
		dmgRatioMultiplier[DIRT][MEAT] = .3;
		dmgRatioMultiplier[DIRT][STONE] = .9;
		dmgRatioMultiplier[DIRT][FUDGE] = 9;
		dmgRatioMultiplier[DIRT][METAL] = .2;
		dmgRatioMultiplier[DIRT][DIRT] = -.2;
		dmgRatioMultiplier[DIRT][WOOD] = .7;
		dmgRatioMultiplier[WOOD][MEAT] = 2;
		dmgRatioMultiplier[WOOD][STONE] = .5;
		dmgRatioMultiplier[WOOD][FUDGE] = 1.3;
		dmgRatioMultiplier[WOOD][METAL] = .3;
		dmgRatioMultiplier[WOOD][DIRT] = 1.1;
		
		for(int i=0; i<elementList.size(); i++) {
			//Element a = elementList.get(i);
			for(int j=0; j<elementList.size(); j++) {
				if(i > WOOD || j > WOOD) {
					//Element b = elementList.get(j);
					if(Math.random() < .45) this.dmgRatioMultiplier[i][j] = Math.random();
					
					this.dmgRatioMultiplier[i][j] = ((int) 100*this.dmgRatioMultiplier[i][j])/100.0;
				}
			}
		}
	}
	
	public ArrayList<Element> getElementList() {
		return this.elementList;
	}
	
	public Element[] getElementArray() {
		Element[] array = new Element[this.elementList.size()];
		for(int i=0; i<this.elementList.size(); i++) {
			array[i] = this.elementList.get(i);
		}
		return array;
	}
	
	public double getDmg(Element attack, Element defend) {
		if (attack == null)
			throw new RuntimeException("Attack was passed in as null.");
		if (defend == null)
			throw new RuntimeException("Defend was passed in as null.");
		double dRM, density, gran, mal;
		if(this.elementList.indexOf(attack)!=-1&&this.elementList.indexOf(defend)!=-1)
		dRM = this.dmgRatioMultiplier[this.elementList.indexOf(attack)][this.elementList.indexOf(defend)];
		else dRM = 0;
		density = attack.getDensity()/defend.getDensity();
		gran = defend.getGranularity();
		mal = attack.getMal()/defend.getMal();
		return Math.cbrt(dRM*(density+gran)/mal);
	}
	
	public void printDmgTable() {		
		String printUE = "\t\t" + elementList.get(0).getName();
		int longest = 0;
		for(int i=1; i<elementList.size(); i++) {
			printUE += ",\t" + elementList.get(i).getName();
			if(elementList.get(i).getName().length() > longest) {
				longest = elementList.get(i).getName().length();
			}
		}
		printUE += "\n";
		for(int i=0; i<elementList.size(); i++) {
			Element a = elementList.get(i);
			printUE += a.getName() + ":";
			for(int j=((a.getName().length()+1)/8); j<((longest+2)/8+1);j++) {
				printUE += "\t";
			}
			printUE += getDmg(a,elementList.get(0));
			for(int j=1; j<elementList.size(); j++) {
				printUE += ",";
				for(int k=0; k<((elementList.get(j).getName().length()+1)/8+1);k++){
					printUE += "\t";
				}
				printUE += getDmg(a,elementList.get(j));
			}
			printUE += "\n";
		}
		System.out.println(printUE);
	}
	
	public static UET getUET() {
		if(uET == null) {
			uET = new UET();
		}
		return uET;
	}
}
