package globj.objects.shaders;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.jdt.annotation.Nullable;

import globj.objects.GLObjectTracker;
import lwjgl.debug.GLDebug;



@NonNullByDefault
public class Shaders {
	
	private static final GLObjectTracker<Shader> tracker = new GLObjectTracker<Shader>();
	
	private Shaders() {
	}
	
	@Nullable
	public static Shader createShader(String name, ShaderType type) {
		if (tracker.contains(name)) {
			GLDebug.glObjError(Shader.class, name, "Cannot create", "Already exists");
			return null;
		}
		Shader s = Shader.create(name, type);
		if (s != null)
			tracker.add(s);
		return s;
	}
	
	@Nullable
	public static Shader createShader(String name, ShaderType type, InputStream in) throws IOException {
		if (tracker.contains(name)) {
			GLDebug.glObjError(Shader.class, name, "Cannot create", "Already exists");
			return null;
		}
		Shader s = Shader.create(name, type);
		if (s != null) {
			tracker.add(s);
			BufferedReader br = new BufferedReader(new InputStreamReader(in));
			List<String> data = new ArrayList<String>();
			String line;
			while ((line = br.readLine()) != null)
				data.add(line + '\n');
			s.setShaderData(data.toArray(new String[data.size()]));
		}
		return s;
	}
	
	@Nullable
	protected static Shader getShader(int id) {
		return tracker.get(id);
	}
	
	@Nullable
	public static Shader getShader(String name) {
		return tracker.get(name);
	}
	
}
