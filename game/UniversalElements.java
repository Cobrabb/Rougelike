package game;

import java.util.ArrayList;

import orig.Element;

public class UniversalElements {
	public final int WATER = 0;
	public final int MEAT = 1;
	public final int STONE = 2;
	public final int FUDGE = 3;
	public final int METAL = 4;
	public final int DIRT = 5;
	public final int WOOD = 6;
	
	public final int TOTAL = 20;
	ArrayList<Element> elementList = new ArrayList<Element>();
	
	
	
	double dmgRatioMultiplier[][] = new double[TOTAL][TOTAL];
	
	public UniversalElements()
	{
		elementList.add(new Element("Water", 1 ,1 , 1));
		elementList.add(new Element("Meat", 2, 1 , 1));
		elementList.add(new Element("Stone", 3.2 ,1 , 1));
		elementList.add(new Element("Fudge", 1.5 ,1 , 1));
		elementList.add(new Element("Metal", 5 ,1 , 1));
		elementList.add(new Element("Dirt", 2.5 , 9 , 1));
		elementList.add(new Element("Wood", 2.5 , 2 , 1));
		while(elementList.size() < TOTAL)
		{
			elementList.add(new Element());
		}
		
		
		
		
		dmgRatioMultiplier[WATER][WATER] = 1;
		dmgRatioMultiplier[WATER][MEAT] = 1;
		dmgRatioMultiplier[WATER][STONE] = .5;
		dmgRatioMultiplier[WATER][FUDGE] = 1;
		dmgRatioMultiplier[WATER][METAL] = .1;//RUSTING
		dmgRatioMultiplier[WATER][DIRT] = -2;//HEALS
		dmgRatioMultiplier[WATER][WOOD] = .2;
		dmgRatioMultiplier[MEAT][WATER] = 1;
		dmgRatioMultiplier[MEAT][MEAT] = 1;
		dmgRatioMultiplier[MEAT][STONE] = .75;
		dmgRatioMultiplier[MEAT][FUDGE] = 1.2;
		dmgRatioMultiplier[MEAT][METAL] = .3;
		dmgRatioMultiplier[MEAT][DIRT] = 1;
		dmgRatioMultiplier[MEAT][WOOD] = 1.5;
		dmgRatioMultiplier[STONE][WATER] = .5;
		dmgRatioMultiplier[STONE][MEAT] = 1.5;
		dmgRatioMultiplier[STONE][STONE] = 1;
		dmgRatioMultiplier[STONE][FUDGE] = 1.7;
		dmgRatioMultiplier[STONE][METAL] = .5;
		dmgRatioMultiplier[STONE][DIRT] = 1;
		dmgRatioMultiplier[STONE][WOOD] = 1.2;
		dmgRatioMultiplier[FUDGE][WATER] = .5;
		dmgRatioMultiplier[FUDGE][MEAT] = -.5;
		dmgRatioMultiplier[FUDGE][STONE] = .5;
		dmgRatioMultiplier[FUDGE][FUDGE] = 1;
		dmgRatioMultiplier[FUDGE][METAL] = .1;
		dmgRatioMultiplier[FUDGE][DIRT] = .5;
		dmgRatioMultiplier[FUDGE][WOOD] = .5;
		dmgRatioMultiplier[METAL][WATER] = 1;
		dmgRatioMultiplier[METAL][MEAT] = 3;
		dmgRatioMultiplier[METAL][STONE] = 1.7;
		dmgRatioMultiplier[METAL][FUDGE] = 4;
		dmgRatioMultiplier[METAL][METAL] = 1;
		dmgRatioMultiplier[METAL][DIRT] = 1;
		dmgRatioMultiplier[METAL][WOOD] = 2.5;
		dmgRatioMultiplier[DIRT][WATER] = 1;
		dmgRatioMultiplier[DIRT][MEAT] = .3;
		dmgRatioMultiplier[DIRT][STONE] = .9;
		dmgRatioMultiplier[DIRT][FUDGE] = 9;
		dmgRatioMultiplier[DIRT][METAL] = .2;
		dmgRatioMultiplier[DIRT][DIRT] = -.2;
		dmgRatioMultiplier[DIRT][WOOD] = .7;
		dmgRatioMultiplier[WOOD][WATER] = 1;
		dmgRatioMultiplier[WOOD][MEAT] = 2;
		dmgRatioMultiplier[WOOD][STONE] = .5;
		dmgRatioMultiplier[WOOD][FUDGE] = 1.3;
		dmgRatioMultiplier[WOOD][METAL] = .3;
		dmgRatioMultiplier[WOOD][DIRT] = 1.1;
		dmgRatioMultiplier[WOOD][WOOD] = 1;
		
		for(int i=0; i<elementList.size(); i++) {
			Element a = elementList.get(i);
			for(int j=0; j<elementList.size(); j++) {
				if(i > WOOD || j > WOOD) {
					Element b = elementList.get(j);
					dmgRatioMultiplier[i][j] = (a.getDensity()/b.getDensity())*b.getGranularity()*(b.getMal()/a.getMal());
				}
			}
		}
		
		
		
	}
	
	public double getDmg(Element attack, Element defend) {
		return this.dmgRatioMultiplier[this.elementList.indexOf(attack)][this.elementList.indexOf(defend)];
	}
	
	
}
