package roguelike.orig;

import java.util.ArrayList;
import java.util.Collections;

public class Planet {

		private ArrayList<ElementPair> e; //the elements present on the planet
		private Language L; //the language spoken on the planet
		private String name;
		private Element land; //the element that makes up the majority of ground, must be either e[0], e[1], or e[2]
		private int landNum; //the element number in the array
		private Element pools; //the element that makes up the majority of the rest of the surface, must be less dense than land and either e[0], e[1], or e[2]
		private int poolsNum; //same for pools
		private Element atmosphere; //the most prevalent element in the atmosphere, must be the least dense element in e. 
		private int atmosNum; //same for atmosphere
		private Creature[] residents; //the native creatures on the planet

		private types type;
		static enum types {GAS, AQUATIC, NORMAL};
		
		public int getElementCount(){
			return e.size();
		}
		
		public String getName(){
			return name;
		}
		
		public Element getElement(int i){
			if(i<0) return null;
			if(i>=e.size()) return null;
			else return e.get(i).first;
		}
		
		public double getElementPres(int i){
			if(i<0) return -1;
			if(i>=e.size()) return -1;
			else return e.get(i).second;
		}
		
		public Language getLanguage(){
			return L;
		}
		
		public Element getLand(){
			return land;
		}
		
		public Element getPools(){
			return pools;
		}
		
		public Element getAtmosphere(){
			return atmosphere;
		}
		
		public int getNumResidents(){
			return residents.length;
		}
		
		public Creature getResident(int i){
			return residents[i];
		}
		
		public int getAtmosNum(){
			return atmosNum;
		}
		
		public int getLandNum(){
			return landNum;
		}
		
		public int getPoolsNum(){
			return poolsNum;
		}
		
		public Planet(){//completely randomly generated, not sure if necessary.
		}
		
		public Planet(Element[] el){ //given a list of elements in the universe, generate a planet
			this.L = new Language();
			this.name = L.generate();
			
			Element[] included = new Element[el.length];
			int k = 0;
			while(k<3){
				for(int i=0; i<el.length; i++){ //first, randomly choose some of the elements to be included in this planet
					if(Math.random()<.4){
						included[k] = el[i];
						k++;
					}
				}
			}
			//runs again if less than three elements
			double percentremains = 1;
			this.e = new ArrayList<ElementPair>(k); //initialize the final arrayList
			for(int i=0; i<k; i++){
				e.add(new ElementPair(included[i]));
				e.get(i).second = Math.random()*(percentremains)-((k-1)/100); //might be a bad formula
				if(i==k-1){
					e.get(i).second = percentremains;
				}
				percentremains -= e.get(i).second;
			}

			Collections.sort(this.e); //sort the arraylist
			
			int j=0;
			type = types.NORMAL;
			atmosNum = 3;
			if(atmosphere.compareTo(this.e.get(0).first)==0){
				atmosNum = 0;
				j=1;
				type = types.GAS; //atmosphere is most abundant, gas planet
			}
			
			if(this.e.get(j).first.compareTo(this.e.get(j+1).first)>=0){
				landNum = j;
				poolsNum = j+1;
				this.land = this.e.get(j).first;
				this.pools = this.e.get(j+1).first;
			}
			else{
				landNum = j+1;
				poolsNum = j;
				this.land = this.e.get(j+1).first;
				this.pools = this.e.get(j).first;
				if(type!=types.GAS) type = types.AQUATIC; //pools are most abundant, aquatic planet
			}
			
			//generate creatures
			j = (int)(Math.random()*8+3);
			this.residents = new Creature[j];
			for(int i=0; i<j; i++){
				//residents[i] = new Creature(this.e, L);
			}
		}
		
/*		public String toString(){
			String out = "This is the planet "+name+". Common elements are ";
			double count=0;
			for(int i=0; i<pres.length; i++){
				out+=e[i].getName();
				out+=", with abundance ";
				out+=pres[i];
				out+=", ";
				count+=pres[i];
			}
			out+="and the total abundance is "+count+". ";
			out+="The surface is "+land.getName()+", the pools are "+pools.getName()+", and the atmosphere is made of "+atmosphere.getName()+".";
			return out;
		}
*/
//I simply commented out code that broke which was not related to planet's constructor, as it is not important right now.
			
			
	}
		
