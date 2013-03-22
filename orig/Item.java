package orig;

public class Item {
	
	public enum iType {
		HEAD, ARMOR, BOOTS, HAND, TOTAL;
		
		public String toString() {
			return this.name();
		}
	}

	private Element[] consists; //need to know proportions
	private Element[] repairs; //need to know proportions
	//private Element[] improve;
	private int hands; //number of hands required to wield
	private int techRequired;
	private double phys_tech_ratio; //0 is pure physical, 1 is pure tech (projectile)
	private int baseDmg;
	private iType type;
	public String name;
	private double weight;
	
	public Item() {
		//generate random elements for consists and repairs
		name = "LOOOOL";
		type = iType.HAND;
		hands = 2;
	}
	
	public Item(Element[] consists, Element[] repairs, int hands, int techRequired, double phys_tech_ratio, int baseDmg, iType type, double weight) {
		this.consists = consists;
		this.repairs = repairs;
		this.hands = hands;
		this.techRequired = techRequired;
		this.phys_tech_ratio = phys_tech_ratio;
		this.baseDmg = baseDmg;
		this.type = type;
		this.weight = weight;
	}
	
	public void attack(Square s, int phys, int tech) {
		if(s.c != null) {
			int damage = (int) (baseDmg*((1-phys_tech_ratio)*phys + phys_tech_ratio*tech));
			s.c.takeDamage(consists, damage);
		}
	}
	
	public boolean repair(Element[] elements) {
		boolean bool = false;
		for(Element e: elements) {
			
		}
		return bool;
	}
	
	public iType getType() {
		return this.type;
	}
	
	public String getName(){
		return this.name;
	}
	
	public int getHands() {
		return this.hands;
	}
	
	public  double getWeight() {
		return this.weight;
	}
}
