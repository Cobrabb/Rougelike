package orig;

public class Language {

	private String name;
	private int strLen; //length of the typical word
	private String letters; //letters used in the language
	private String vowels; //vowels used in the language (note, these will also be stored in letters)
	private boolean sensical; //true if the langange generates words of the form cvcvcv... or vcvcvc...
	//other properties??
	
	public Language(){ //completely random constructor
		this.strLen = (int)(Math.random()*4)+4;
		this.letters = "";
		this.vowels = "";
		for(int i=97; i<123; i++){
			if(Math.random()<.80){
				letters+=(char)i;
				if(isVowel((char)i)) vowels+=(char)i;
			}
 
		}
		this.sensical = false;
		if(Math.random()<.35) this.sensical = true;
		
		//if sensical is true, there must be at least one vowel available.
		if(sensical&&vowels.length()==0){
			double d = Math.random();
			if(d<.2) vowels+='a';
			else if(d<.4) vowels+='e';
			else if(d<.6) vowels+='i';
			else if(d<.8) vowels+='o';
			else vowels+='u';
		}
		name = generate();
	}
	
	public Language(String name, int strLen, String letters, boolean sensical){
		this.name = name;
		this.strLen = strLen;
		this.letters = letters;
		this.vowels = "";
		for(int i=0; i<this.letters.length(); i++){
			if(isVowel(this.letters.charAt(i))) this.vowels+= this.letters.charAt(i); 
		}
		this.sensical = sensical;
	}
	
	public int getStrLen(){
		return this.strLen;
	}
	
	public boolean getSensical(){
		return this.sensical;
	}
	
	public String getLetter(){
		return this.letters;
	}
	
	public String generate(int i){ //generates a word of length i
		if(i<1) return "";
		int count = i-1;
		boolean vowel = false;
		String output = (""+makeLetter(vowel)).toUpperCase();
		if(Math.random()<.50&&sensical) vowel = true; 
		while(count>0){
			output+=makeLetter(vowel);
			vowel = !vowel;
			count--;
		}
		return output;
	}
	
	public String generate(){
		int i = strLen+(int)(Math.random()*5)-2;
		if(i<1) i=1;
		return generate(i);
	}
	
	private boolean isVowel(char a){ //helper method to determine whether a character is a vowel or not
		return (a=='a'||a=='e'||a=='i'||a=='o'||a=='u');
	}
	
	private char makeLetter(boolean vowel){ // helper method returns a random letter. If sensical is true, the letter depends on the input argument
		if(sensical){
			if(vowel){
				return vowels.charAt((int)(Math.random()*vowels.length()));
			}
			char a = 'a';
			while(isVowel(a)){
				a = letters.charAt((int)(Math.random()*letters.length()));
			}
			return a;
		}
		return letters.charAt((int)(Math.random()*letters.length()));
	}
	
	public String toString(){
		return "This is the Language "+name+". A typical word might be "+generate()+", "+generate()+", or the infamous "+generate(11)+".";
	}
}
