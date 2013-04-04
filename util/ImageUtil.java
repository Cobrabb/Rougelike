package util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;

import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

public final class ImageUtil {
	private static HashMap<String, Image> nameToImage;
	private static boolean loaded = false;
	private static String relativeFolder;
	private static String parseFile;
	private static String tilesetImageFile;
	private static Image tileset;
	private static int tileWidth;
	private static int tileHeight;
	
	public static void loadImages() {
		if (loaded)
			return;
		int errorCount = 0;
		int mode = 0;
		int height = -1;
		int width = -1;
		int tileCount = -1;
		boolean done = false;
		ImageUtil.nameToImage = new HashMap<String, Image>();
		try {
			ImageUtil.tileset = new Image(ImageUtil.getFullTilesetImageFile());
			BufferedReader parseReader = new BufferedReader(new FileReader(new File(ImageUtil.getFullParseFile())));
			mode = 0;
			height = -1;
			width = -1;
			tileCount = -1;
			done = false;
			while (!done) {
				String line = parseReader.readLine();
				// ignore lines that are empty
				if (line.equals(""))
					continue;
				// ignore lines beginning with #
				if (line.charAt(0) == '#')
					continue;
				switch (mode) {
				// we are to read in the width
				case 0:
					width = Integer.parseInt(line);
					++mode;
					break;
				// we are to read in the height
				case 1:
					height = Integer.parseInt(line);
					++mode;
					break;
				// we are to read in the tileCount
				case 2:
					tileCount = Integer.parseInt(line);
					++mode;
					break;
				// we are to read in a tile
				case 3:
					if (tileCount > 0)
						--tileCount;
					String[] parts = line.split(" ");
					int x = ImageUtil.interpret(width, height, parts[0]);
					int y = ImageUtil.interpret(width, height, parts[1]);
					// begin at the character after both integer inputs
					String name = line.substring(parts[0].length() + parts[1].length() + 2);
					Image subimage = (ImageUtil.tileset).getSubImage(x, y, width, height);
					if (ImageUtil.nameToImage.containsKey(name)) {
						System.err.printf("Warning: There are tile elements with the same name.\n");
					}
					ImageUtil.nameToImage.put(name, subimage);
					// we are done
					if (tileCount == 0)
						done = true;
					break;
				}
			}
		} catch (SlickException e) {
			System.err.printf("Something went wrong in the loadImages method.\n");
			e.printStackTrace();
			++errorCount;
		} catch (FileNotFoundException e) {
			System.err.printf("A file is missing, or was named incorrectly.\n");
			e.printStackTrace();
			++errorCount;
		} catch (IOException e) {
			System.err.printf("Likely, the parse file was formatted incorrectly.\n");
			e.printStackTrace();
			++errorCount;
		}
		if (errorCount == 0) {
			ImageUtil.loaded = true;
			ImageUtil.tileHeight = height;
			ImageUtil.tileWidth = width;
		}
	}
	
	private static int interpret(int width, int height, String equation) {
		// the format of the string is either:
		// 1.  A single number
		// 2.  An equation of the form a*b+c, with a, b, c, either integers or the characters W or H
		//	   where W means width, H means height
		
		int result = -1; // this should give an error if used, I hope
		if (!equation.contains("*")) {
			// we are in form 1.
			result = Integer.parseInt(equation);
		} else {
			// we are in form 2.
			String[] parts = equation.split("[*+]");
			if (parts.length != 3)
				throw new NumberFormatException("The tileset file is not properly formed: " + equation);
			int[] nums = new int[3];
			for (int i = 0; i < 3; ++i) {
				if (parts[i].toLowerCase().equals("h")) {
					nums[i] = height;
				} else if (parts[i].toLowerCase().equals("w")) {
					nums[i] = width;
				} else {
					nums[i] = Integer.parseInt(parts[i]);
				}
			}
			result = nums[0] * nums[1] + nums[2];
		}
		return result;
	}

	private static String getFullParseFile() {
			// WINDOWS DEPENDENCY (for windows use next)
		//String ret = String.format("%s\\%s", ImageUtil.getRelativeFolder(), ImageUtil.getTPF());
			//For Linux use next instead
		String ret = String.format("%s/%s", ImageUtil.getRelativeFolder(), ImageUtil.getTPF());
		return ret;
	}

	private static String getFullTilesetImageFile() {
			// WINDOWS DEPENDENCY (for windows use next)
		//String ret = String.format("%s\\%s", ImageUtil.getRelativeFolder(), ImageUtil.getTilesetImageFile());
			//For Linux use next instead
		String ret = String.format("%s/%s", ImageUtil.getRelativeFolder(), ImageUtil.getTilesetImageFile());
		return ret;
	}

	public static String getTilesetImageFile() {
		return ImageUtil.tilesetImageFile;
	}
	
	public static void setTilesetImageFile(String tif) {
		ImageUtil.tilesetImageFile = tif;
	}
	
	public static String getTPF() {
		return ImageUtil.parseFile;
	}
	
	public static void setTPF(String pf) {
		ImageUtil.parseFile = pf;
	}
	
	public static String getRelativeFolder() {
		return ImageUtil.relativeFolder;
	}
	
	public static void setRelativeFolder(String rf) {
		ImageUtil.relativeFolder = rf;
	}
	
	public static Image getImage(String key) {
		return ImageUtil.nameToImage.get(key);
	}

	public static int getTileWidth() {
		return ImageUtil.tileWidth;
	}
	
	public static int getTileHeight() {
		return ImageUtil.tileHeight;
	}
}
