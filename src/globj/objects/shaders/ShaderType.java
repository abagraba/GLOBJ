package globj.objects.shaders;


import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.jdt.annotation.Nullable;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL31;
import org.lwjgl.opengl.GL32;
import org.lwjgl.opengl.GL40;
import org.lwjgl.opengl.GL43;



@NonNullByDefault
public enum ShaderType {
	VERTEX("Vertex Shader", GL20.GL_VERTEX_SHADER, GL31.GL_UNIFORM_BLOCK_REFERENCED_BY_VERTEX_SHADER, GL43.GL_REFERENCED_BY_VERTEX_SHADER, GL20.GL_MAX_VERTEX_UNIFORM_COMPONENTS),
	FRAGMENT("Fragment Shader", GL20.GL_FRAGMENT_SHADER, GL31.GL_UNIFORM_BLOCK_REFERENCED_BY_FRAGMENT_SHADER, GL43.GL_REFERENCED_BY_FRAGMENT_SHADER, GL20.GL_MAX_FRAGMENT_UNIFORM_COMPONENTS),
	GEOMETRY("Geometry Shader", GL32.GL_GEOMETRY_SHADER, GL31.GL_UNIFORM_BLOCK_REFERENCED_BY_GEOMETRY_SHADER, GL43.GL_REFERENCED_BY_GEOMETRY_SHADER, GL32.GL_MAX_GEOMETRY_UNIFORM_COMPONENTS),
	TESSELATION_CONTROL("Tesselation Control Shader", GL40.GL_TESS_CONTROL_SHADER, GL40.GL_UNIFORM_BLOCK_REFERENCED_BY_TESS_CONTROL_SHADER, GL43.GL_REFERENCED_BY_TESS_CONTROL_SHADER, GL40.GL_MAX_TESS_CONTROL_UNIFORM_COMPONENTS),
	TESSELATION_EVALUATION("Tesselation Evaluation Shader", GL40.GL_TESS_EVALUATION_SHADER, GL40.GL_UNIFORM_BLOCK_REFERENCED_BY_TESS_EVALUATION_SHADER, GL43.GL_REFERENCED_BY_TESS_EVALUATION_SHADER, GL40.GL_MAX_TESS_EVALUATION_UNIFORM_COMPONENTS),
	COMPUTE("Compute Shader", GL43.GL_COMPUTE_SHADER, GL43.GL_UNIFORM_BLOCK_REFERENCED_BY_COMPUTE_SHADER, GL43.GL_REFERENCED_BY_COMPUTE_SHADER, GL43.GL_MAX_COMPUTE_UNIFORM_COMPONENTS);
	
	private final String	name;
	private final int		value;
	private final int		referenceLegacy;
	private final int		reference;
	private final int		maxUniforms;
	
	
	private ShaderType(String name, int value, int referenceLegacy, int reference, int maxuni) {
		this.name = name;
		this.value = value;
		this.referenceLegacy = referenceLegacy;
		this.reference = reference;
		this.maxUniforms = maxuni;
	}
	
	/**
	 * @param glInt
	 *            the GLint representing a shader type
	 * @return the ShaderType object represented by glInt
	 */
	@Nullable
	public static ShaderType get(int glInt) {
		for (ShaderType target : values())
			if (target.value == glInt)
				return target;
		return null;
	}
	
	/**
	 * @return the name of this shader type
	 */
	public String typeName() {
		return name;
	}
	
	/**
	 * @return the GLint representing this shader type
	 */
	public int value() {
		return value;
	}
	
	/**
	 * @return the GLint for checking referenced blocks for this shader type for GL versions < 4.3
	 */
	public int referenceLegacy() {
		return referenceLegacy;
	}
	
	/**
	 * @return the GLint for checking referenced blocks for this shader type
	 */
	public int reference() {
		return reference;
	}
	
	/**
	 * @return the GLint for checking the maximum number of uniforms for this shader type
	 */
	public int maxUniforms() {
		return maxUniforms;
	}
	
	@Override
	public String toString() {
		return name;
	}
	
}
