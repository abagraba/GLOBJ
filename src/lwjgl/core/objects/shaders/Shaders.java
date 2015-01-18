package lwjgl.core.objects.shaders;

import java.io.InputStream;
import java.util.HashMap;

public class Shaders {
	
	public static final HashMap<String, Shader> shaders = new HashMap<String, Shader>();
	
	public static void loadShader(String name, int target, InputStream in) {
		Shader s = new Shader(name, target);
	}
	
	public static void unloadShader(String name) {
		
	}
	
	public static Shader getShader(String name) {
		return shaders.get(name);
	}
	
}
