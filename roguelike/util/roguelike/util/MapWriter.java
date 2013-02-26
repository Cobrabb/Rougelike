package roguelike.util;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;

public class MapWriter {
	private static final String indent = "               "; // 15 characters
	private static int tileWidth;
	private static int tileHeight;
	private static String tileSource;
	private static String folderDirectory;
	private static HashMap<String, Integer> labelToGID;
	
	public static void arrayToXML(String mapPath, String[] layers, String[][][] tiles) throws IOException {
		if (layers.length != tiles.length) {
			String message = String.format("Input mismatch: Size of layers array(%d) different from size of tiles array(%d).", layers.length, tiles.length);
			throw new RuntimeException(message);
		}
		MapWriter.loadTiles();
		int width = tiles[0].length;
		int height = tiles[0][0].length;
		PrintWriter pw = new PrintWriter(new File(MapWriter.getFolderDirectory() + mapPath));
		pw.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
		pw.write("<map version=\"1.0\" orientation=\"orthogonal\" width=\"" + width + "\" height=\"" + height +
				"\" tilewidth=\"" + MapWriter.tileWidth() + "\" tileheight=\"" + MapWriter.tileHeight() + "\">\n");
		pw.write(idt(1) + "<tileset firstgid=\"1\" source=\"" + MapWriter.getTileSource() + "\"/>\n");
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
		pw.flush();
		pw.close();
	}
	
	public static int getGID(String label) {
		if (labelToGID.containsKey(label)) {
			return labelToGID.get(label);
		}
		return 0;
	}
	
	public static String getFolderDirectory() {
		return MapWriter.folderDirectory;
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
	
	public static void loadTiles() throws IOException {
		
	}
}
