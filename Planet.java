
public class Planet {

		private Element[] e; //the elements present on the planet
		private Double[] pres; //the %presence of the elements, 1-1 correspondence with e, most prevalent first
		private Language L; //the language spoken on the planet
		private Element land; //the element that makes up the majority of ground, must be either e[0] or e[1]
		private Element pools; //the element that makes up the majority of the rest of the surface, must be less dense than land and either e[0] or e[1]
		private Element atmosphere; //the most prevalent element in the atmosphere, must be the least dense element in e
		private Creature[] residents; //the native creatures on the planet
		
		public int getElementNum(){
			return e.length;
		}
		
		public Element getElement(int i){
			if(i<0) return null;
			if(i>=e.length) return null;
			else return e[i];
		}
		
		public double getElementPres(int i){
			if(i<0) return -1;
			if(i>=pres.length) return -1;
			else return pres[i];
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
		
		public Planet(){//completely randomly generated, not sure if necessary.
		}
		
		public Planet(Element[] e){ //given a list of elements in the universe, generate a planet
			this.L = new Language();
			Element[] included = new Element[e.length];
			int k = 0;
			for(int i=0; i<e.length; i++){ //first, randomly choose some of the elements to be included in this planet
				if(Math.random()<.4){
					included[k] = e[i];
					k++;
				}
			}
			if(k<3){ //must be at least 3 elements
				
			}
			this.e = new Element[k]; //initialize the final array
			int j=0;
			Element temp;
			for(int i=0; i<k; i++){
				if(Math.random()<.9){ //give each element a chance to swap with the elements in front of it
					j = (int)(Math.random()*(k-i))+i; //random number between i and k
					temp = included[i];
					included[i] = included[j];
					included[j] = temp;
				}
				this.e[i] = included[i]; //either way copy it over
			}
			
			//now, generate the double values corresponding with the elements
			
			
		}
		
}
