package globj.objects.shaders;

import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL31;
import org.lwjgl.opengl.GL32;
import org.lwjgl.opengl.GL40;
import org.lwjgl.opengl.GL43;

public enum ShaderType {
	VERTEX("Vertex Shader", GL20.GL_VERTEX_SHADER, GL31.GL_UNIFORM_BLOCK_REFERENCED_BY_VERTEX_SHADER, GL43.GL_REFERENCED_BY_VERTEX_SHADER,
			GL20.GL_MAX_VERTEX_UNIFORM_COMPONENTS),
	FRAGMENT("Fragment Shader", GL20.GL_FRAGMENT_SHADER, GL31.GL_UNIFORM_BLOCK_REFERENCED_BY_FRAGMENT_SHADER, GL43.GL_REFERENCED_BY_FRAGMENT_SHADER,
			GL20.GL_MAX_FRAGMENT_UNIFORM_COMPONENTS),
	GEOMETRY("Geometry Shader", GL32.GL_GEOMETRY_SHADER, GL31.GL_UNIFORM_BLOCK_REFERENCED_BY_GEOMETRY_SHADER, GL43.GL_REFERENCED_BY_GEOMETRY_SHADER,
			GL32.GL_MAX_GEOMETRY_UNIFORM_COMPONENTS),
	TESSELATION_CONTROL("Tesselation Control Shader", GL40.GL_TESS_CONTROL_SHADER, GL40.GL_UNIFORM_BLOCK_REFERENCED_BY_TESS_CONTROL_SHADER,
			GL43.GL_REFERENCED_BY_TESS_CONTROL_SHADER, GL40.GL_MAX_TESS_CONTROL_UNIFORM_COMPONENTS),
	TESSELATION_EVALUATION("Tesselation Evaluation Shader", GL40.GL_TESS_EVALUATION_SHADER, GL40.GL_UNIFORM_BLOCK_REFERENCED_BY_TESS_EVALUATION_SHADER,
			GL43.GL_REFERENCED_BY_TESS_EVALUATION_SHADER, GL40.GL_MAX_TESS_EVALUATION_UNIFORM_COMPONENTS),
	COMPUTE("Compute Shader", GL43.GL_COMPUTE_SHADER, GL43.GL_UNIFORM_BLOCK_REFERENCED_BY_COMPUTE_SHADER, GL43.GL_REFERENCED_BY_COMPUTE_SHADER,
			GL43.GL_MAX_COMPUTE_UNIFORM_COMPONENTS);
	
	public final String name;
	public final int value;
	public final int referenceLegacy;
	public final int reference;
	public final int maxuni;
	
	private ShaderType(String name, int value, int referenceLegacy, int reference, int maxuni) {
		this.name = name;
		this.value = value;
		this.referenceLegacy = referenceLegacy;
		this.reference = reference;
		this.maxuni = maxuni;
	}
	
	public static ShaderType get(int i) {
		for (ShaderType target : values())
			if (target.value == i)
				return target;
		return null;
	}
	
	@Override
	public String toString() {
		return name;
	}
	
}
