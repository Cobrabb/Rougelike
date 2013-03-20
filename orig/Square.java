package orig;

public class Square {

	boolean passable; //true if this square is floor type, false if it is wall type
	Element consists; //the element that this square is made of
	//MapObject[] contains - It may be useful to store what is "here" in the square class, but I'm thinking not. If it is, we can fill this in later
	
	public Creature c; //at most one creature may be on a square
	
	
	/*public MapObject getTop() {
		return null; // indicates no items on this Square.
	}*/
	
}
