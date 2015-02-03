package globj.core;

import java.util.HashMap;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL32;

public class Context {

	private static final HashMap<Integer, Boolean> boolVals = new HashMap<Integer, Boolean>();
	private static final HashMap<Integer, Float> floatVals = new HashMap<Integer, Float>();
	private static final HashMap<Integer, Integer> intVals = new HashMap<Integer, Integer>();
	private static final HashMap<Integer, Long> longVals = new HashMap<Integer, Long>();

	public static boolean boolConst(int name) {
		if (!boolVals.containsKey(name))
			boolVals.put(name, GL11.glGetBoolean(name));
		return boolVals.get(name);
	}

	public static float floatConst(int name) {
		if (!floatVals.containsKey(name))
			floatVals.put(name, GL11.glGetFloat(name));
		return floatVals.get(name);
	}

	public static int intConst(int name) {
		if (!intVals.containsKey(name))
			intVals.put(name, GL11.glGetInteger(name));
		return intVals.get(name);
	}

	public static long longConst(int name) {
		if (!longVals.containsKey(name))
			longVals.put(name, GL32.glGetInteger64(name));
		return longVals.get(name);
	}
	
	public static boolean boolValue(int name) {
		return GL11.glGetBoolean(name);
	}

	public static float floatValue(int name) {
		return GL11.glGetFloat(name);
	}

	public static int intValue(int name) {
		return GL11.glGetInteger(name);
	}

	public static long longValue(int name) {
		return GL32.glGetInteger64(name);
	}

}
