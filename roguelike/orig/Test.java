package orig;
import java.util.*;
import java.io.*;

import orig.Creature.cStats;
import orig.Creature.cVal;
import orig.Race.rStats;

public class Test {
	public static void main(String[] args){
		Language L = new Language();
		Element[] e = new Element[10];
		e[0] = new Element("Water", 1);
		e[1] = new Element("Meat", 3);
		e[2] = new Element("Stone", 10);
		e[3] = new Element("Fudge", 2);
		
		for(int i=4; i<10; i++){
			e[i] = new Element(L);
		}
		
		Planet pl = new Planet(e);
		System.out.println(pl.toString());
		//creature test
		//String stat = Stats.TOTAL;
		cStats s[] = cStats.values();
		cVal v[] = cVal.values();
		for(int i = 0; i < cStats.TOTAL.ordinal()+1; i++) {
			System.out.println(s[i] + " = " + s[i].ordinal());
		}
		cStats cs = cStats.DETECT_SIGHT;
		Race r = new Race();
		rStats rs = rStats.DETECTION;
		System.out.println(rs + " ~ " + rs.ordinal());
		Creature c = new Creature();
		for(int i = 0; i < cStats.TOTAL.ordinal(); i++) {
			for(int j = 0; j < cVal.TOTAL.ordinal(); j++) {
				System.out.println(s[i] + " : " + v[j] + " = "  + c.get(s[i],v[j]));
			}
		}
		c.gain(cs, cVal.XP, 300);
		System.out.println(cs.name() + " ~ " + c.get(cs, cVal.LEVEL) + ", " + c.get(cs, cVal.XP));
		//System.out.println(cStats.SPEED_MOVE + " : " + c.get(cStats.SPEED_MOVE,0));
	}
}
