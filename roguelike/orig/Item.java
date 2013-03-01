package orig;

public class Item {

	private Element[] consists;
	private Element[] repairs;
	private int hands; //number of hands required to wield
	private int techRequired;
	private double phys_tech_ratio; //0 is pure physical, 1 is pure tech (projectile)
	private int baseDmg;
	
	
	public void attack(Square s, int phys, int tech) {
		if(s.c != null) {
			int damage = (int) (baseDmg*((1-phys_tech_ratio)*phys + phys_tech_ratio*tech));
			s.c.takeDamage(consists, damage);
		}
	}
}
