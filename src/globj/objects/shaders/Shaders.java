package globj.objects.shaders;

import globj.objects.GLObjectTracker;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import org.lwjgl.opengl.GL20;

import lwjgl.debug.GLDebug;

public class Shaders {
	
	private static final GLObjectTracker<Shader> tracker = new GLObjectTracker<Shader>();
	
	public static Shader createShader(String name, ShaderType type) {
		if (tracker.contains(name)) {
			GLDebug.globjError(Shader.class, name, "Cannot create", "Already exists");
			return null;
		}
		Shader s = Shader.create(name, type);
		if (s != null)
			tracker.add(s);
		return s;
	}
	
	public static Shader createShader(String name, ShaderType type, InputStream in) throws IOException {
		if (tracker.contains(name)) {
			GLDebug.globjError(Shader.class, name, "Cannot create", "Already exists");
			return null;
		}
		Shader s = Shader.create(name, type);
		if (s != null)
			tracker.add(s);
		BufferedReader br = new BufferedReader(new InputStreamReader(in));
		ArrayList<String> data = new ArrayList<String>();
		String line;
		while ((line = br.readLine()) != null)
			data.add(line + '\n');
		s.setShaderData(data.toArray(new String[data.size()]));
		return s;
	}
	
	public static Shader destroyShader(Shader shader) {
		if (shader != null) {
			GL20.glDeleteShader(shader.id);
			tracker.remove(shader);
		}
		return null;
	}

	public static Shader destroyShader(String name) {
		if (!tracker.contains(name)) {
			GLDebug.globjError(Shader.class, name, "Cannot destroy", "Does not exist");
			return null;
		}
		return destroyShader(getShader(name));
	}
	
	public static Shader getShader(String name) {
		return tracker.get(name);
	}
	
}
