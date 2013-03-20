package orig;
import java.util.*;
import java.io.*;

import orig.Creature.cStats;
import orig.Creature.bStats;
import orig.Creature.sVal;
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
		
	//	Planet pl = new Planet(e);
	//	System.out.println(pl.toString());
		//creature test
		cStats s[] = cStats.values();
		sVal v[] = sVal.values();

		cStats cs = cStats.DETECT_SIGHT;
		Race r = new Race(e, L);
		//bStats rs = bStats.DETECTION;
		//ystem.out.println(rs + " ~ " + rs.ordinal());
		Creature c = new Creature(r);
		Creature c2 = new Creature(r);
		Creature c3 = new Creature(r);
		//c2.gain(s[0], sVal.XP, 500000);
		for(int i=0; i<100;i++) {
			//c.autoLvlPointsBy(10);
			c2.autoLvlXP(11000);
		}
		String statPrint;
	/*	
		System.out.println("-----------------------Initial Stat Test----------------------------");
		for(int i = 0; i < cStats.TOTAL.ordinal(); i++) {
			statPrint = s[i] + " : ";
			for(int j = 0; j < sVal.TOTAL.ordinal(); j++) {
				statPrint += c.get(s[i], v[j]) + " , ";
			}
			statPrint += c.decode(s[i]).toString() + " , " + c.getRaceGain();
			System.out.println(statPrint);
		}
		/*
		System.out.println("----------------------Level Detect_Sight Test------------------------");
		for(int i=0; i<400; i++) {
			//System.out.println(cs.name() + " > " + c.get(cs, sVal.LEVEL) + ", " + c.get(cs, sVal.XP));
			c.gain(cs, sVal.XP, 10);
		}
		System.out.println(cs.name() + " > " + c.get(cs, sVal.LEVEL) + ", " + c.get(cs, sVal.XP));
		//c.gain(cs, sVal.XP, 300);
		//System.out.println(cs.name() + " > " + c.get(cs, sVal.LEVEL) + ", " + c.get(cs, sVal.XP));
		//c.gain(cs, sVal.XP, 3000);
		//System.out.println(cs.name() + " > " + c.get(cs, sVal.LEVEL) + ", " + c.get(cs, sVal.XP));
		//String statPrint;
		*/
		System.out.println("----------------------------Current Value Test------------------------------");
		for(int i = 0; i < cStats.TOTAL.ordinal(); i++) {
			statPrint = s[i] + " : ";
			for(int j = 0; j < sVal.TOTAL.ordinal(); j++) {
				statPrint += c.get(s[i], v[j]) + " , ";
			}
			statPrint += c.decode(s[i]).toString() + " , " + c.getRaceGain();
			System.out.println(statPrint);
		}
		for(int k = 0; k<cStats.TOTAL.ordinal(); k++) {
			for(int i=0; i<k*1000; i++) {
				//System.out.println(cs.name() + " ~ " + c.get(cs, sVal.LEVEL) + ", " + c.get(cs, sVal.XP));
				c.gain(s[k], sVal.XP, 10);
			}
		}
		
		//System.out.println("Level Detect Sight Test");
		//System.out.println(cs.name() + " > " + c.get(cs, sVal.LEVEL) + ", " + c.get(cs, sVal.XP));
		//c.gain(cs, sVal.XP, 300);
		//System.out.println(cs.name() + " > " + c.get(cs, sVal.LEVEL) + ", " + c.get(cs, sVal.XP));
		//c.gain(cs, sVal.XP, 3000);
		//System.out.println(cs.name() + " > " + c.get(cs, sVal.LEVEL) + ", " + c.get(cs, sVal.XP));
		//String statPrint;
		
		
		System.out.println("-----------------------------Get Effective Test--------------------------");
		for(int i = 0; i < cStats.TOTAL.ordinal(); i++) {
			statPrint = s[i] + " : ";
			for(int j = 0; j < sVal.TOTAL.ordinal(); j++) {
				statPrint += c.get(s[i], v[j]) + ", ";
			}
			statPrint += c.decode(s[i]).toString() + ", " + c.getRaceGain() + ", " + c.getEffective(s[i], v[sVal.CURRENT.ordinal()]);
			System.out.println(statPrint);
		}
		
		/*System.out.println("-----------------------------bStats Test------------------------------------");
		bStats rstat[] = bStats.values();
		for(int i=0; i<bStats.TOTAL.ordinal(); i++) {
			statPrint = rstat[i] + " : " + c.getRace().get(rstat[i], v[0]);
			for(int j = 1; j < sVal.TOTAL.ordinal(); j++) {
				statPrint += ", " + c.getRace().get(rstat[i], v[j]);
			}
			//statPrint += c.decode(s[i]).toString() + " , " + c.getRaceGain();
			System.out.println(statPrint);
		}
		//System.out.println(cStats.SPEED_MOVE + " : " + c.get(cStats.SPEED_MOVE,0));
<<<<<<< HEAD
		 * */
		System.out.println("-----------------------------Get Effective Test--------------------------");
		for(int i = 0; i < cStats.TOTAL.ordinal(); i++) {
			statPrint = s[i] + " : ";
			for(int j = 0; j < sVal.TOTAL.ordinal(); j++) {
				statPrint += c.get(s[i], v[j]) + ", ";
			}
			statPrint += c.decode(s[i]).toString() + ", " + c.getRaceGain() + ", " + c.getEffective(s[i], v[sVal.CURRENT.ordinal()]);
			System.out.println(statPrint);
		}

		System.out.println("-----------------------------Get Effective Test--------------------------");
		for(int i = 0; i < cStats.TOTAL.ordinal(); i++) {
			statPrint = s[i] + " : ";
			for(int j = 0; j < sVal.TOTAL.ordinal(); j++) {
				statPrint += c2.get(s[i], v[j]) + ", ";
			}
			statPrint += c2.decode(s[i]).toString() + ", " + c2.getRaceGain() + ", " + c2.getEffective(s[i], v[sVal.CURRENT.ordinal()]);
			System.out.println(statPrint);
		}

		System.out.println("-----------------------------Get Effective Test--------------------------");
		for(int i = 0; i < cStats.TOTAL.ordinal(); i++) {
			statPrint = s[i] + " : ";
			for(int j = 0; j < sVal.TOTAL.ordinal(); j++) {
				statPrint += c3.get(s[i], v[j]) + ", ";
			}
			statPrint += c3.decode(s[i]).toString() + ", " + c3.getRaceGain() + ", " + c3.getEffective(s[i], v[sVal.CURRENT.ordinal()]);
			System.out.println(statPrint);
		}

		
		UniversalElements uE = new UniversalElements();
		uE.printDmgTable();
	}
}
