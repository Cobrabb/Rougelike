
public class Planet {

		private Element[] e; //the elements present on the planet
		private Double[] pres; //the %presence of the elements, 1-1 correspondence with e, most prevalent first
		private Language L; //the language spoken on the planet
		private String name;
		private Element land; //the element that makes up the majority of ground, must be either e[0], e[1], or e[2]
		private Element pools; //the element that makes up the majority of the rest of the surface, must be less dense than land and either e[0], e[1], or e[2]
		private Element atmosphere; //the most prevalent element in the atmosphere, must be the least dense element in e. 
		private Creature[] residents; //the native creatures on the planet
		
		public int getElementNum(){
			return e.length;
		}
		
		public String getName(){
			return name;
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
			this.name = L.generate();
			
			Element[] included = new Element[e.length];
			int k = 0;
			for(int i=0; i<e.length; i++){ //first, randomly choose some of the elements to be included in this planet
				if(Math.random()<.4){
					included[k] = e[i];
					k++;
				}
			}
			int j = 0;
			if(k<3){ //must be at least 3 elements, if not, add the first unused elements until 3 is reaches.
				for(int i=0; i<e.length; i++){
					if(included[j]!=e[i]){
						included[k] = e[i];
						k++;
					}
					else{
						j++;
					}
				}
			}
			
			this.e = new Element[k]; //initialize the final array
			j = 0;
			Element temp;
			this.atmosphere = e[0]; 
			for(int i=0; i<k; i++){
				if(Math.random()<.9){ //give each element a chance to swap with the elements in front of it
					j = (int)(Math.random()*(k-i))+i; //random number between i and k
					temp = included[i];
					included[i] = included[j];
					included[j] = temp;
				}
				this.e[i] = included[i]; //either way copy it over
				if(this.e[i].getDensity()<atmosphere.getDensity()){
					atmosphere = this.e[i]; //must be the lightest element
				}
			}
			
			//now, generate the double values corresponding with the elements. The first one is always >50% (to ensure that it truly is the most prevalent).
			this.pres = new Double[this.e.length];
			double percentremains = 1;
			pres[0] = Math.random()*.2+.5;
			percentremains-=pres[0];
			
			for(int i=1; i<pres.length-1; i++){ //generate everything except for the last element
				pres[i] = Math.random()*(percentremains-((pres.length-i-2)/100.0)); //the idea is to keep at least 1% per element.
				percentremains-=pres[i];
			}
			
			//get everything in its correct order
			pres[pres.length-1] = percentremains;
			if(percentremains>pres[pres.length-2]){
				int i=pres.length-3; 
				while(i>1&&percentremains>pres[i]) i--;
				for(j=i+1; j<pres.length-1; j++){
					percentremains=pres[j];
					pres[j]=pres[pres.length-1];
					pres[pres.length-1]=percentremains;
				}
			}
			
			//generate the land and pools
			j=0;
			if(atmosphere == this.e[0]){
				j=1;
			}
			
			if(this.e[j].compareTo(this.e[j+1])>=0){
				this.land = this.e[j];
				this.pools = this.e[j+1];
			}
			else{
				this.land = this.e[j+1];
				this.pools = this.e[j];
			}
			
			//generate creatures
			j = (int)(Math.random()*8+3);
			this.residents = new Creature[j];
			for(int i=0; i<j; i++){
				residents[i] = new Creature(this.e, L);
			}
		}
		
		public String toString(){
			String out = "This is the planet "+name+". Common elements are ";
			double count=0;
			for(int i=0; i<pres.length; i++){
				out+=e[i].getName();
				out+=", with abundance ";
				out+=pres[i];
				out+=", ";
				count+=pres[i];
			}
			out+="and the total abundance is "+count+".";
			for(int i=0; i<residents.length; i++){
				out+=residents[i].toString();
				out+=" ";
			}
			out+="The surface is "+land.getName()+", the pools are "+pools.getName()+", and the atmosphere is made of "+atmosphere.getName()+".";
			return out;
		}

			
			
	}
		
