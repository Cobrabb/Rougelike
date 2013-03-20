package util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;

public class MapWriter {
	private static final String indent = "               "; // 15 characters
	private static int tileWidth;
	private static int tileHeight;
	private static String resourceFile; // file name of the .tsx file which labels things
	private static String tileSource; // file name of the string to gid map inside folderDirectory
	private static String folderDirectory; // location where the game maps are in the file system.
	private static HashMap<String, Integer> labelToGID;
	
	// TODO: Encode the data properly in the base64 compression, likely using the Base64 classes
	public static String arrayToXML(String mapPath, String[] layers, String[][][] tiles) throws IOException {
		if (layers.length != tiles.length) {
			String message = String.format("Input mismatch: Size of layers array(%d) different from size of tiles array(%d).", layers.length, tiles.length);
			throw new RuntimeException(message);
		}
		MapWriter.loadTiles();
		int width = tiles[0].length;
		int height = tiles[0][0].length;
		createPlanetFolder(mapPath);
		PrintWriter pw = new PrintWriter(new File(MapWriter.getFolderDirectory() + mapPath));
		pw.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
		pw.write("<map version=\"1.0\" orientation=\"orthogonal\" width=\"" + width + "\" height=\"" + height +
				"\" tilewidth=\"" + MapWriter.tileWidth() + "\" tileheight=\"" + MapWriter.tileHeight() + "\">\n");
		pw.write(idt(1) + "<tileset firstgid=\"1\" source=\"" + MapWriter.getResourceFile() + "\"/>\n");
		for (int curLayer = 0; curLayer < layers.length; ++curLayer) {
			pw.write(idt(1) + "<layer name=\"" + layers[curLayer] + "\" width=\"" + width + "\" height=\"" + height + "\">\n");
			pw.write(idt(2) + "<data>\n");
			for (int x = 0; x < width; ++x) {
				for (int y = 0; y < height; ++y) {
					pw.write(idt(3) + "<tile gid=\"" + MapWriter.getGID(tiles[curLayer][x][y]) + "\"/>\n");
				}
			}
			pw.write(idt(2) + "</data>\n");
			pw.write(idt(1) + "</layer>\n");
		}
		pw.write("</map>");
		pw.flush();
		pw.close();
		return (MapWriter.getFolderDirectory() + mapPath);
	}
	
	private static void createPlanetFolder(String mapPath) throws IOException {
		String[] parts = mapPath.split("\\\\");
		String first = mapPath.substring(0, mapPath.length() - parts[parts.length-1].length() - 1);
		File planetFolder = new File(MapWriter.getFolderDirectory() + first);
		boolean success = planetFolder.mkdirs();
		if (!success)
			throw new IOException("Failed to create the planet directory for dungeon map " + mapPath);
	}

	public static int getGID(String label) {
		if (labelToGID.containsKey(label)) {
			return labelToGID.get(label);
		}
		return 0;
	}
	
	public static void setFolderDirectory(String directory) {
		MapWriter.folderDirectory = directory;
	}
	
	public static void setTileSource(String fileName) {
		MapWriter.tileSource = fileName;
	}
	
	public static String getFolderDirectory() {
		return MapWriter.folderDirectory;
	}
	
	public static String getResourceFile() {
		return MapWriter.resourceFile;
	}

	public static String idt(int n) {
		return MapWriter.indent.substring(0, n);
	}
	
	public static String getTileSource() {
		return MapWriter.getFolderDirectory() + MapWriter.tileSource;
	}
	
	public static int tileWidth() {
		return MapWriter.tileWidth;
	}
	
	public static int tileHeight() {
		return MapWriter.tileHeight;
	}
	
	// loads from a file a mapping from String labels to the GID in the .tsx files
	public static void loadTiles() throws IOException {
		BufferedReader in = new BufferedReader(new FileReader(new File(MapWriter.getTileSource())));
		HashMap<String, Integer> map = new HashMap<String, Integer>();
		int lineCount = Integer.parseInt(in.readLine());
		// reads the number of mappings, and then each mapping, placing it into the HashMap
		while (lineCount-->0) {
			String[] parts = (in.readLine()).split("=");
			map.put(parts[0], Integer.parseInt(parts[1]));
		}
		MapWriter.labelToGID = map;
	}

	public static void setTileHeight(int i) {
		MapWriter.tileHeight = i;
	}
	
	public static void setTileWidth(int i) {
		MapWriter.tileWidth = i;
	}

	public static void setResourceFile(String file) {
		MapWriter.resourceFile = file;
	}
}
