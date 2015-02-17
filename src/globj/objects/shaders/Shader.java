package globj.objects.shaders;

import globj.core.GL;
import globj.objects.GLObject;
import lwjgl.debug.GLDebug;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;

public class Shader extends GLObject {
	
	protected final ShaderType type;
	
	private Shader(String name, ShaderType type) {
		super(name, GL20.glCreateShader(type.value));
		this.type = type;
	}
	
	public static Shader create(String name, ShaderType type) {
		Shader s = new Shader(name, type);
		if (s.id == 0) {
			GLDebug.globjError(Shader.class, name, "Cannot create", "No ID could be allocated");
			return null;
		}
		return s;
	}
	
	public void setShaderData(String[] code) {
		GL20.glShaderSource(id, code);
		GL20.glCompileShader(id);
		String[] errors = getErrors();
		if (errors != null)
			for (String error : errors)
				GLDebug.write(error);
	}
	
	public String[] getErrors() {
		if (GL20.glGetShaderi(id, GL20.GL_COMPILE_STATUS) != GL11.GL_FALSE)
			return null;
		int length = GL20.glGetShaderi(id, GL20.GL_INFO_LOG_LENGTH);
		return GL20.glGetShaderInfoLog(id, length).split("\n");
	}
	
	@Override
	public void debug() {
		GL.flushErrors();
		GLDebug.setPad(24);
		
		GLDebug.write(GLDebug.fixedString("Shader:") + this);
		GLDebug.indent();
		
		String[] errors = getErrors();
		if (errors != null)
			for (String error : errors)
				GLDebug.write(error);
		
		GLDebug.unindent();
		GLDebug.unsetPad();
		GL.flushErrors();
	}
	
	@Override
	public String toString() {
		return GLDebug.fixedString(name) + type;
	}
	
}
