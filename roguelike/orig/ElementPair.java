package roguelike.orig;

public class ElementPair implements Comparable<ElementPair>{
	Element first;
	double second;
	
	public ElementPair(Element e){
		this.first = e;
	}

	@Override
	public int compareTo(ElementPair e) {
		return (int)(this.second-e.second);
	}
	
}
