package lwjgl.core.objects.shaders;

import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL32;
import org.lwjgl.opengl.GL40;
import org.lwjgl.opengl.GL43;

public enum ShaderType {
	VERTEX("Vertex Shader", GL20.GL_VERTEX_SHADER),
	FRAGMENT("Fragment Shader", GL20.GL_FRAGMENT_SHADER),
	GEOMETRY("Geometry Shader", GL32.GL_GEOMETRY_SHADER),
	TESSELATION_CONTROL("Tesselation Control Shader", GL40.GL_TESS_CONTROL_SHADER),
	TESSELATION_EVALUATION("Tesselation Evaluation Shader", GL40.GL_TESS_EVALUATION_SHADER),
	COMPUTE("Compute Shader", GL43.GL_COMPUTE_SHADER);
	
	public final String name;
	public final int value;
	
	private ShaderType(String name, int value) {
		this.name = name;
		this.value = value;
	}
	
	public static ShaderType get(int i) {
		for (ShaderType target : values())
			if (target.value == i)
				return target;
		return null;
	}
	
	public String toString() {
		return name;
	}
	
}
