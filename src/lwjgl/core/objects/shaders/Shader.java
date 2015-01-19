package lwjgl.core.objects.shaders;

import lwjgl.core.objects.GLObject;
import lwjgl.debug.Logging;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;

public class Shader extends GLObject {
	
	protected final ShaderType type;
	
	private Shader(String name, ShaderType type) {
		super(name, GL20.glCreateShader(type.value));
		this.type = type;
	}
	
	public static Shader createShader(String name, ShaderType type) {
		Shader s = new Shader(name, type);
		if (s.id == 0) {
			Logging.globjError(Shader.class, name, "Cannot create", "No ID could be allocated");
			return null;
		}
		return s;
	}
	
	public void setShaderData(String[] code) {
		GL20.glShaderSource(id, code);
		GL20.glCompileShader(id);
	}
	
	public String[] getErrors() {
		if (GL20.glGetShaderi(id, GL20.GL_COMPILE_STATUS) != GL11.GL_FALSE)
			return null;
		int length = GL20.glGetShaderi(id, GL20.GL_INFO_LOG_LENGTH);
		return GL20.glGetShaderInfoLog(id, length).split("\n");
	}
	
	@Override
	public void debug() {
		Logging.writeOut(Logging.fixedString("Shader:") + name);
		Logging.indent();
		
		Logging.writeOut(Logging.fixedString("Type:") + type);
		Logging.indent();
		
		String[] errors = getErrors();
		if (errors != null)
			for (String error : errors)
				Logging.writeOut(error);
		/*
		 * int length = GL20.glGetShaderi(id, GL20.GL_SHADER_SOURCE_LENGTH);
		 * String[] src = GL20.glGetShaderSource(id, length).split("\n");
		 * Logging.logInfo(src);
		 */
		
		Logging.unindent();
		
		Logging.unindent();
	}
	
}
