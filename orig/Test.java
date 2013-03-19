package orig;
import java.util.*;
import java.io.*;

import orig.Creature.cStats;
import orig.Race.rVal;
//import orig.Creature.rVal;
import orig.Race.rStats;
import game.UniversalElements;

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
		rVal v[] = rVal.values();

		/*
		for(int i = 0; i < cStats.TOTAL.ordinal()+1; i++) {
			System.out.println(s[i] + " = " + s[i].ordinal());
		}
		*/
		cStats cs = cStats.DETECT_SIGHT;
		Race r = new Race();
		rStats rs = rStats.DETECTION;
		//ystem.out.println(rs + " ~ " + rs.ordinal());
		Creature c = new Creature(0);
		String statPrint;
		
		System.out.println("-----------------------Initial Stat Test----------------------------");
		for(int i = 0; i < cStats.TOTAL.ordinal(); i++) {
			statPrint = s[i] + " : ";
			for(int j = 0; j < rVal.TOTAL.ordinal(); j++) {
				statPrint += c.get(s[i], v[j]) + " , ";
			}
			statPrint += c.decode(s[i]).toString() + " , " + c.getRaceGain();
			System.out.println(statPrint);
		}
		/*
		System.out.println("----------------------Level Detect_Sight Test------------------------");
		for(int i=0; i<400; i++) {
			System.out.println(cs.name() + " > " + c.get(cs, rVal.LEVEL) + ", " + c.get(cs, rVal.XP));
			c.gain(cs, rVal.XP, 10);
		}
		System.out.println(cs.name() + " > " + c.get(cs, rVal.LEVEL) + ", " + c.get(cs, rVal.XP));
		c.gain(cs, rVal.XP, 300);
		System.out.println(cs.name() + " > " + c.get(cs, rVal.LEVEL) + ", " + c.get(cs, rVal.XP));
		c.gain(cs, rVal.XP, 3000);
		System.out.println(cs.name() + " > " + c.get(cs, rVal.LEVEL) + ", " + c.get(cs, rVal.XP));
		//String statPrint;
		*/
		System.out.println("----------------------------Current Value Test------------------------------");
		for(int i = 0; i < cStats.TOTAL.ordinal(); i++) {
			statPrint = s[i] + " : ";
			for(int j = 0; j < rVal.TOTAL.ordinal(); j++) {
				statPrint += c.get(s[i], v[j]) + " , ";
			}
			statPrint += c.decode(s[i]).toString() + " , " + c.getRaceGain();
			System.out.println(statPrint);
		}
		for(int k = 0; k<cStats.TOTAL.ordinal(); k++) {
			for(int i=0; i<k*1000; i++) {
				//System.out.println(cs.name() + " ~ " + c.get(cs, rVal.LEVEL) + ", " + c.get(cs, rVal.XP));
				c.gain(s[k], rVal.XP, 10);
			}
		}
		/*
		System.out.println("Level Detect Sight Test");
		System.out.println(cs.name() + " > " + c.get(cs, rVal.LEVEL) + ", " + c.get(cs, rVal.XP));
		c.gain(cs, rVal.XP, 300);
		System.out.println(cs.name() + " > " + c.get(cs, rVal.LEVEL) + ", " + c.get(cs, rVal.XP));
		c.gain(cs, rVal.XP, 3000);
		System.out.println(cs.name() + " > " + c.get(cs, rVal.LEVEL) + ", " + c.get(cs, rVal.XP));
		//String statPrint;
		*/
		
		System.out.println("-----------------------------Get Effective Test--------------------------");
		for(int i = 0; i < cStats.TOTAL.ordinal(); i++) {
			statPrint = s[i] + " : ";
			for(int j = 0; j < rVal.TOTAL.ordinal(); j++) {
				statPrint += c.get(s[i], v[j]) + ", ";
			}
			statPrint += c.decode(s[i]).toString() + ", " + c.getRaceGain() + ", " + c.getEffective(s[i], v[rVal.CURRENT.ordinal()]);
			System.out.println(statPrint);
		}
		
		System.out.println("-----------------------------RStats Test------------------------------------");
		rStats rstat[] = rStats.values();
		for(int i=0; i<rStats.TOTAL.ordinal(); i++) {
			statPrint = rstat[i] + " : " + c.getRace().get(rstat[i], v[0]);
			for(int j = 1; j < rVal.TOTAL.ordinal(); j++) {
				statPrint += ", " + c.getRace().get(rstat[i], v[j]);
			}
			//statPrint += c.decode(s[i]).toString() + " , " + c.getRaceGain();
			System.out.println(statPrint);
		}
		//System.out.println(cStats.SPEED_MOVE + " : " + c.get(cStats.SPEED_MOVE,0));
		
		UniversalElements uE = new UniversalElements();
		uE.printDmgTable();
	}
}
