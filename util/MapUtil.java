package util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;

import orig.DungeonMap;

public final class MapUtil {
	private static HashMap<String, Integer> labelToGID = null;
	private static String tileSource; // file name of the string to gid map inside folderDirectory
	private static String folderDirectory; // location where the game maps are in the file system.
	private static int tileWidth;
	private static int tileHeight;
	private static String resourceFile; // file name of the .tsx file which labels things

	
	public static int getGID(String label) {
		if (labelToGID.containsKey(label)) {
			return labelToGID.get(label);
		}
		return 0;
	}
	
	public static String writeMap(String mapName, DungeonMap dm) {
		String mapPath = String.format("%s\\%s", MapUtil.folderDirectory, mapName);
		try {
			FileOutputStream fos = new FileOutputStream(mapPath);
			ObjectOutputStream oos = new ObjectOutputStream(fos);
			oos.writeObject(dm);
			oos.close();
		} catch (IOException e) {
			System.err.printf("Error writing dungeon map\n");
			e.printStackTrace();
			mapPath = null;
		}
		return mapPath;
	}
	
 	private static void createPlanetFolder(String mapPath) throws IOException {
 		String[] parts = mapPath.split("\\\\");
 		String first = mapPath.substring(0, mapPath.length() - parts[parts.length-1].length() - 1);
-		File planetFolder = new File(MapWriter.getFolderDirectory() + first);
+		File planetFolder = new File(MapUtil.getFolderDirectory() + first);
 		boolean success = planetFolder.mkdirs();
 		if (!success)
 			throw new IOException("Failed to create the planet directory for dungeon map " + mapPath);
 	}

	
	public static DungeonMap readMap(String mapPath) {
		DungeonMap ret = null;
		try {
			FileInputStream fis = new FileInputStream(mapPath);
			ObjectInputStream ois = new ObjectInputStream(fis);
			ret = (DungeonMap)ois.readObject();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return ret;
	}
	
	// loads from a file a mapping from String labels to the GID in the .tsx files
	public static void loadTiles() {
		if (MapUtil.labelToGID != null)
			return;
		HashMap<String, Integer> map = null;
		try {
			BufferedReader in = new BufferedReader(new FileReader(new File(MapUtil.getTileSource())));
			map = new HashMap<String, Integer>();
			int lineCount = Integer.parseInt(in.readLine());
			// reads the number of mappings, and then each mapping, placing it into the HashMap
			while (lineCount-->0) {
				String[] parts = (in.readLine()).split("=");
				map.put(parts[0], Integer.parseInt(parts[1]));
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			MapUtil.labelToGID = map;
		}
	}
	
	public static String getTileSource() {
		return MapUtil.getFolderDirectory() + MapUtil.tileSource;
	}
	
	public static void setTileSource(String fileName) {
		MapUtil.tileSource = fileName;
	}
	
	public static void setFolderDirectory(String directory) {
		MapUtil.folderDirectory = directory;
	}
	
	public static String getFolderDirectory() {
		return MapUtil.folderDirectory;
	}
	
	public static int tileWidth() {
		return MapUtil.tileWidth;
	}
	
	public static int tileHeight() {
		return MapUtil.tileHeight;
	}

	public static void setTileHeight(int i) {
		MapUtil.tileHeight = i;
	}
	
	public static void setTileWidth(int i) {
		MapUtil.tileWidth = i;
	}

	public static void setResourceFile(String file) {
		MapUtil.resourceFile = file;
	}

	public static String getResourceFile() {
		return MapUtil.resourceFile;
	}
}
