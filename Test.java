import java.util.*;
import java.io.*;

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
	}
}
