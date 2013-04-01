package util;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.HashMap;
import java.util.zip.GZIPOutputStream;

import javax.xml.bind.DatatypeConverter;

public class MapWriter {
	private static final String indent = "               "; // 15 characters
	
	// TODO: Encode the data properly in the base64 compression, likely using the Base64 classes
	public static String arrayToXML(String mapPath, String[] layers, int[][][] tiles) throws IOException {
		if (layers.length != tiles.length) {
			String message = String.format("Input mismatch: Size of layers array(%d) different from size of tiles array(%d).", layers.length, tiles.length);
			throw new RuntimeException(message);
		}
		int width = tiles[0].length;
		int height = tiles[0][0].length;
		createPlanetFolder(mapPath);
		PrintWriter pw = new PrintWriter(new File(MapUtil.getFolderDirectory() + mapPath));
		pw.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
		pw.write("<map version=\"1.0\" orientation=\"orthogonal\" width=\"" + width + "\" height=\"" + height +
				"\" tilewidth=\"" + MapUtil.tileWidth() + "\" tileheight=\"" + MapUtil.tileHeight() + "\">\n");
		pw.write(idt(1) + "<tileset firstgid=\"1\" source=\"" + MapUtil.getResourceFile() + "\"/>\n");
		pw.write(idt(1) + "<!-- Layer data is compressed (GZip) binary data, encoded in Base64 -->\n");
		for (int curLayer = 0; curLayer < layers.length; ++curLayer) {
			pw.write(idt(1) + "<layer name=\"" + layers[curLayer] + "\" width=\"" + width + "\" height=\"" + height + "\">\n");
			//pw.write(idt(2) + "<data encoding=\"base64\" compression=\"gzip\">\n");
			pw.write(idt(2) + "<data>\n");
			int[] layerGids = new int[width*height];
			for (int x = 0; x < width; ++x) {
				for (int y = 0; y < height; ++y) {
					pw.write(idt(3) + "<tile gid=\"" + tiles[curLayer][x][y] + "\"/>\n");
					layerGids[x*height + y] = tiles[curLayer][x][y];
				}
			}
			String base64gzip = MapWriter.base64gzipCompression(layerGids);
			System.err.println(base64gzip);
			//pw.write(idt(3) + base64gzip + "\n");
			pw.write(idt(2) + "</data>\n");
			pw.write(idt(1) + "</layer>\n");
		}
		pw.write("</map>");
		pw.flush();
		pw.close();
		return (MapUtil.getFolderDirectory() + mapPath);
	}
	
	private static void createPlanetFolder(String mapPath) throws IOException {
		String[] parts = mapPath.split("\\\\");
		String first = mapPath.substring(0, mapPath.length() - parts[parts.length-1].length() - 1);
		File planetFolder = new File(MapUtil.getFolderDirectory() + first);
		boolean success = planetFolder.mkdirs();
		if (!success)
			throw new IOException("Failed to create the planet directory for dungeon map " + mapPath);
	}
	
	// This method takes the list of gids and applies gzip compression followed by Base64 conversion
	// The technicalities of the first part are based off of the C++ code used by Tiled
	// See https://github.com/bjorn/tiled/blob/master/src/libtiled/mapwriter.cpp#L398
	private static String base64gzipCompression(int[] gids) throws IOException {
		byte[] uncompressed = new byte[gids.length*4];
		for (int i = 0; i < gids.length; ++i) {
			int idx = 4*i;
			uncompressed[idx] = (byte)gids[i];
			uncompressed[idx+1] = (byte)(gids[i] >> 8);
			uncompressed[idx+2] = (byte)(gids[i] >> 16);
			uncompressed[idx+3] = (byte)(gids[i] >> 24);
			if (i % 100 == 0) {
				System.err.printf("gid=%d, ret: %d, %d\n", gids[i], uncompressed[idx], uncompressed[idx+1]);
			}
		}
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		GZIPOutputStream zipper = new GZIPOutputStream(out, true);
		zipper.write(uncompressed);
		System.err.printf("size of uncompressed=%d\n", uncompressed.length);
		zipper.flush();
		out.flush();
		byte[] gzipped = out.toByteArray();
		System.err.printf("size of gzip=%d\n", gzipped.length);
		System.err.printf("gzipped data: %s\n", Arrays.toString(gzipped));
		String base64gzip = DatatypeConverter.printBase64Binary(gzipped);
		return base64gzip;
	}

	private static String idt(int n) {
		return MapWriter.indent.substring(0, n);
	}
}
