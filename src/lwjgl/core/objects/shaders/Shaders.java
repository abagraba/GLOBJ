package lwjgl.core.objects.shaders;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;

import org.lwjgl.opengl.GL20;

import lwjgl.core.objects.GLObjectTracker;
import lwjgl.debug.Logging;

public class Shaders {
	
	private static final GLObjectTracker<Shader> tracker = new GLObjectTracker<Shader>();
	
	public static void loadShader(String name, ShaderType type) {
		if (tracker.contains(name)) {
			Logging.globjError(Shader.class, name, "Cannot create", "Already exists");
			return;
		}
		Shader s = Shader.createShader(name, type);
		if (s != null)
			tracker.add(s);
	}
	
	public static void loadShader(String name, ShaderType type, InputStream in) throws IOException {
		if (tracker.contains(name)) {
			Logging.globjError(Shader.class, name, "Cannot create", "Already exists");
			return;
		}
		Shader s = Shader.createShader(name, type);
		if (s != null)
			tracker.add(s);
		BufferedReader br = new BufferedReader(new InputStreamReader(in));
		ArrayList<String> data = new ArrayList<String>();
		String line;
		while ((line = br.readLine()) != null)
			data.add(line);
		s.setShaderData(data.toArray(new String[data.size()]));
	}
	
	public static void unloadShader(String name) {
		if (!tracker.contains(name)) {
			Logging.globjError(Shader.class, name, "Cannot destroy", "Does not exist");
			return;
		}
		Shader s = tracker.get(name);
		GL20.glDeleteShader(s.id);
		tracker.remove(s);
	}
	
	public static Shader getShader(String name) {
		return tracker.get(name);
	}
	
}
