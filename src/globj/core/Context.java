package globj.core;


import java.util.HashMap;
import java.util.Map;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL32;



/**
 * This Utility class is used to retreive values from the GL Context using glGet().
 */
@NonNullByDefault
public class Context {
	
	private static final Map<Integer, Boolean>	boolVals	= new HashMap<Integer, Boolean>();
	private static final Map<Integer, Float>	floatVals	= new HashMap<Integer, Float>();
	private static final Map<Integer, Integer>	intVals		= new HashMap<Integer, Integer>();
	private static final Map<Integer, Long>		longVals	= new HashMap<Integer, Long>();
	
	private Context() {
	}
	
	/**
	 * This method is meant to retrieve constants with glGet(). <br/>
	 * NOTE: This caches the retrieved value. If this value is not constant then further calls to this method may not
	 * return the proper value. If the value you need changes, then use {@link #boolValue(int)}.
	 * 
	 * @param glInt
	 *            representing the constant to be retrieved with glGet()
	 * @return the constant
	 */
	public static boolean boolConst(int name) {
		if (!boolVals.containsKey(name))
			boolVals.put(name, GL11.glGetBoolean(name));
		return boolVals.get(name);
	}
	
	/**
	 * This method is meant to retrieve constants with glGet(). <br/>
	 * NOTE: This caches the retrieved value. If this value is not constant then further calls to this method may not
	 * return the proper value. If the value you need changes, then use {@link #floatValue(int)}.
	 * 
	 * @param glInt
	 *            representing the constant to be retrieved with glGet()
	 * @return the constant
	 */
	public static float floatConst(int name) {
		if (!floatVals.containsKey(name))
			floatVals.put(name, GL11.glGetFloat(name));
		return floatVals.get(name);
	}
	
	/**
	 * This method is meant to retrieve constants with glGet(). <br/>
	 * NOTE: This caches the retrieved value. If this value is not constant then further calls to this method may not
	 * return the proper value. If the value you need changes, then use {@link #intValue(int)}.
	 * 
	 * @param glInt
	 *            representing the constant to be retrieved with glGet()
	 * @return the constant
	 */
	public static int intConst(int name) {
		if (!intVals.containsKey(name))
			intVals.put(name, GL11.glGetInteger(name));
		return intVals.get(name);
	}
	
	/**
	 * This method is meant to retrieve constants with glGet(). <br/>
	 * NOTE: This caches the retrieved value. If this value is not constant then further calls to this method may not
	 * return the proper value. If the value you need changes, then use {@link #longValue(int)}.
	 * 
	 * @param glInt
	 *            representing the constant to be retrieved with glGet()
	 * @return the constant
	 */
	public static long longConst(int name) {
		if (!longVals.containsKey(name))
			longVals.put(name, GL32.glGetInteger64(name));
		return longVals.get(name);
	}
	
	/**
	 * This method is meant to retrieve changing values with glGet(). <br/>
	 * NOTE: If the value you need is constant, then use {@link #boolConst(int)} for better performance.
	 * 
	 * @param glInt
	 *            representing the constant to be retrieved with glGet()
	 * @return the value
	 */
	public static boolean boolValue(int name) {
		return GL11.glGetBoolean(name);
	}
	
	/**
	 * This method is meant to retrieve changing values with glGet(). <br/>
	 * NOTE: If the value you need is constant, then use {@link #floatConst(int)} for better performance.
	 * 
	 * @param glInt
	 *            representing the constant to be retrieved with glGet()
	 * @return the value
	 */
	public static float floatValue(int name) {
		return GL11.glGetFloat(name);
	}
	
	/**
	 * This method is meant to retrieve changing values with glGet(). <br/>
	 * NOTE: If the value you need is constant, then use {@link #intConst(int)} for better performance.
	 * 
	 * @param glInt
	 *            representing the constant to be retrieved with glGet()
	 * @return the value
	 */
	public static int intValue(int name) {
		return GL11.glGetInteger(name);
	}
	
	/**
	 * This method is meant to retrieve changing values with glGet(). <br/>
	 * NOTE: If the value you need is constant, then use {@link #longConst(int)} for better performance.
	 * 
	 * @param glInt
	 *            representing the constant to be retrieved with glGet()
	 * @return the value
	 */
	public static long longValue(int name) {
		return GL32.glGetInteger64(name);
	}
	
}
