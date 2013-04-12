package orig;

import game.OnScreenChar;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;

import util.DungeonMapGenerator;
import util.General.Direction;
import util.General.GridPoint;
import util.MapUtil;

public class Planet {

		private Element[] e; //the elements present on the planet
		private Double[] pres; //the %presence of the elements, 1-1 correspondence with e, most prevalent first
		private Language L; //the language spoken on the planet
		private String name;
		private Element land; //the element that makes up the majority of ground, must be either e[0], e[1], or e[2]
		private int landNum; //the element number in the array
		private Element pools; //the element that makes up the majority of the rest of the surface, must be less dense than land and either e[0], e[1], or e[2]
		private int poolsNum; //same for pools
		private Element atmosphere; //the most prevalent element in the atmosphere, must be the least dense element in e. 
		private int atmosNum; //same for atmosphere
		private DungeonMapGenerator mapGenerator; // generates the maps for this planet.
		private DungeonMap currentDungeon; // the currently loaded dungeon.
		private String[][][] planetMap; // a 3d grid of dungeon names
		// TODO: maybe keep some of the maps open?
		// planetMap[floor][row][col], might want to limit planetMap[floor] to be at most a 3x3 grid
		private int dungeonFloor;
		private int dungeonRow;
		private int dungeonCol;
		private Race[] residents; //the native creatures on the planet
		
		final int numTypes = 3; //the number of types of planets. Currently 1 = normal, 2 = aquatic, and 3 = gaseous. This is based on the most common element in the first 3.
		int type;
		
		public int getElementCount(){
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
		
		public Race getResident(int i){
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
		
		protected Planet(){//completely randomly generated, not sure if necessary.
		}
		
		public Planet(Element[] e, int[] dungeonSizes) { //given a list of elements in the universe, generate a planet
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
			this.atmosphere = included[0]; 
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
			//need to insert some sorting here.
			
			//generate the land and pools
			j=0;
			type = 0; //default case, normal planet
			atmosNum = 3;
			if(atmosphere == this.e[0]){
				atmosNum = 0;
				j=1;
				type = 3; //atmosphere is most abundant, gas planet
			}
			
			if(this.e[j].compareTo(this.e[j+1])>=0){
				landNum = j;
				poolsNum = j+1;
				this.land = this.e[j];
				this.pools = this.e[j+1];
			}
			else{
				landNum = j+1;
				poolsNum = j;
				this.land = this.e[j+1];
				this.pools = this.e[j];
				if(type!=3) type = 2; //pools are most abundant, aquatic planet
			}
			
			//generate creatures
			j = (int)(Math.random()*8+3);
			this.residents = new Race[j];
			for(int i=0; i<j; i++){
				residents[i] = new Race(this.e, L);
			}
			
			// create map generator
			this.mapGenerator = new DungeonMapGenerator(this);
			int numFloors = dungeonSizes[0];
			int numRows = dungeonSizes[1];
			int numCols = dungeonSizes[2];
			this.planetMap = new String[numFloors][numRows][numCols];
			this.dungeonFloor = 0;
			this.dungeonCol = (0 + numCols)/2;
			this.dungeonRow = (0 + numRows)/2;
			String path = this.generateMap("beginning");
			this.setCurrentDungeon(path);
		}
		
		public String toString(){
			String out = "This is the planet " + name + ". Common elements are ";
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

		// returns the String referring to the fileLocation of this dungeon
		protected String generateMap(String mapName) {
			return this.generateMap(mapName, 60, 60);
		}
		
		protected String generateMap(String mapName, int width, int height) {
			mapName = String.format("planet\\%s\\%s", this.name, mapName);
			String mapPath = this.mapGenerator.generateBlankSquareMap(mapName, 60, 60);
			planetMap[dungeonFloor][dungeonRow][dungeonCol] = mapPath;
			return mapPath;
		}
		
		protected String generateConnectedMap(String mapName, int dWidth, int dHeight) {
			mapName = String.format("planet\\%s\\%s", this.name, mapName);
			// first, we need to look at the maps that are left, right, up down, lower, higher
			// for the ones that exist, we need to match doors, stairs
			ArrayList<GridPoint> doorLocs = new ArrayList<GridPoint>();
			ArrayList<GridPoint> stairLocs = new ArrayList<GridPoint>();
			Direction[] around = new Direction[] {Direction.LEFT, Direction.RIGHT, Direction.UP, Direction.DOWN, Direction.LOWER, Direction.HIGHER};
			HashSet<Direction> forbidden = new HashSet<Direction>();
			// we can't have doors that go off the grid.
			// either that, or we need to wrap around.
			// TODO: code a choice in here - if we want to have map wrap around for the lolzies
			// right now, no wrap around.
			
			for(Direction cur: around) {
				// check if this location is a valid square
				int newFloor = this.dungeonFloor;
				int newRow   = this.dungeonRow;
				int newCol   = this.dungeonCol;
				
				switch (cur) {
				case LOWER:
				case HIGHER:
					newFloor += cur.getY();
					break;
				default:
					newRow += cur.getY();
					newCol += cur.getX(); // [row][col] == [y][x]
				}
				
				if(!validLocation(newFloor, newRow, newCol)) {
					forbidden.add(cur);
					continue; // this isn't even on the map
				}
				// it is on the map
				String nearMapName = planetMap[newFloor][newRow][newCol];
				// if it is null, ignore it
				if (nearMapName == null)
					continue;
				// it wasn't null, we need to load this map
				//System.err.printf("\tnearmapname = %s\n", nearMapName);
				DungeonMap dm = MapUtil.readMap(nearMapName);
				HashSet<GridPoint> doors = dm.getDoorList();
				Iterator<GridPoint> it = doors.iterator();
				while (it.hasNext()) {
					GridPoint nextPt = it.next();
					Door d = dm.getDoor(nextPt);
					if (d.getDirection() == cur.opposite()) {
						// this door is on the appropriate side to matter
						GridPoint mirror = new GridPoint(nextPt.getX(), nextPt.getY());
						mirror.mirror(60, 60);
						doorLocs.add(mirror);
					}
				}
				HashSet<GridPoint> stairs = dm.getStairList();
				it = stairs.iterator();
				while (it.hasNext()) {
					GridPoint nextPt = it.next();
					Stairs s = dm.getStairs(nextPt);
					if (s.getDirection() == cur.opposite()) {
						// this staircase matters.
						GridPoint cpy = new GridPoint(nextPt.getX(), nextPt.getY());
						// if the staircase goes up, we need to indicate this
						// I have done it in a hacky way -- by making the coordinates negative
						// then I detect this in DungeonMapGenerator and make the appropriate decisions
						if (cur == Direction.LOWER)
							cpy.flipSigns();
						stairLocs.add(cpy);
						forbidden.add(cur);
					}
				}
			}
			GridPoint[] doorLocations = new GridPoint[doorLocs.size()];
			//System.out.printf("Planet: Needed doors:\n");
			for (int i = 0; i < doorLocs.size(); ++i) {
				doorLocations[i] = doorLocs.get(i);
				//System.out.printf("%s ", doorLocations[i]);
			}
			GridPoint[] stairLocations = new GridPoint[stairLocs.size()];
			for (int i = 0; i < stairLocs.size(); ++i) {
				stairLocations[i] = stairLocs.get(i);
			}
			String mapPath = this.mapGenerator.generateConnectedSquareMap(mapName, dWidth, dHeight, doorLocations, forbidden, stairLocations);
			planetMap[dungeonFloor][dungeonRow][dungeonCol] = mapPath;
			return mapPath;
		}
		
		private boolean validLocation(int newFloor, int newRow, int newCol) {
			return (newFloor >= 0 && newRow >= 0 && newCol >= 0
					&& newFloor < planetMap.length && newRow < planetMap[0].length && newCol < planetMap[0][0].length);
		}

		protected void setCurrentDungeon(String mapPath) {
			this.currentDungeon = MapUtil.readMap(mapPath);
			//System.err.printf("Current map name is %s\n", mapPath);
			this.currentDungeon.setPlanet(this);
		}
		
		protected void saveCurrentDungeon() {
			MapUtil.writeMap(planetMap[dungeonFloor][dungeonRow][dungeonCol], this.currentDungeon);
		}
		
		public DungeonMap getCurrentDungeon() {
			return this.currentDungeon;
		}
		
		boolean moveMap(Direction d, OnScreenChar c, GridPoint gp) {
			//System.err.printf("Reaching moveMap: %s, char, %s\n", d, gp);
			// save the current dungeon
			this.saveCurrentDungeon();
			
			int newFloor = this.dungeonFloor;
			int newRow = this.dungeonRow;
			int newCol = this.dungeonCol;
			
			switch (d) {
			case LOWER:
			case HIGHER:
				newFloor += d.getY();
				break;
			default:
				newRow += d.getY(); // [row][col] = [y][x]
				newCol += d.getX();
			}
			// check that these dimensions are valid
			if (!validLocation(newFloor, newRow, newCol)) {
				// failure: out of bounds in the map
				System.err.printf("Bad move map call: Tried to move to %d, %d, %d\n", newFloor, newRow, newCol);
				return false;
			}
			// we need to load the map in this new location.
			String newName = planetMap[newFloor][newRow][newCol];
			this.dungeonFloor = newFloor;
			this.dungeonRow = newRow;
			this.dungeonCol = newCol;
			String mapPath = newName;
			if (newName == null) {
				// we need to generate a map here.
				newName = this.L.generate(6);
				newName = String.format("%s_%d_%d_%d", newName, newFloor, newRow, newCol);
				planetMap[newFloor][newRow][newCol] = newName;
				//System.err.printf("new name is %s\n", newName);
				mapPath = this.generateConnectedMap(newName, 60, 60);
			}
			// now that we are certain a map is defined here
			this.setCurrentDungeon(mapPath);
			DungeonMap dm = this.getCurrentDungeon();
			c.setPosition(gp.getX(), gp.getY());
			dm.putOnScreenChar(gp.getX(), gp.getY(), c, false);
			//System.err.printf("placed char at %s\n", gp);
			int pSightRadius = 5;
			dm.reveal(pSightRadius, gp.getX(), gp.getY());
			return true;
		}
}