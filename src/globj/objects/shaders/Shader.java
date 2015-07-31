package globj.objects.shaders;


import globj.objects.GLObject;
import lwjgl.debug.GLDebug;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.jdt.annotation.Nullable;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;



@NonNullByDefault
public class Shader extends GLObject {
	
	protected final ShaderType	type;
	
	@Nullable
	private String[]			errors;
	
	
	private Shader(String name, ShaderType type) {
		super(name, GL20.glCreateShader(type.value()));
		this.type = type;
	}
	
	@Nullable
	public static Shader create(String name, ShaderType type) {
		Shader s = new Shader(name, type);
		if (s.id == 0) {
			GLDebug.glObjError(Shader.class, name, "Cannot create", "No ID could be allocated");
			return null;
		}
		return s;
	}
	
	public void setShaderData(String[] code) {
		GL20.glShaderSource(id, code);
		GL20.glCompileShader(id);
		getErrors();
		if (errors != null)
			for (String error : errors)
				GLDebug.write(error);
	}
	
	public void getErrors() {
		if (GL20.glGetShaderi(id, GL20.GL_COMPILE_STATUS) == GL11.GL_FALSE) {
			int length = GL20.glGetShaderi(id, GL20.GL_INFO_LOG_LENGTH);
			errors = GL20.glGetShaderInfoLog(id, length).split("\n");
		}
	}
	
	public void destroy() {
		GL20.glDeleteShader(id);
	}
	
	/**************************************************
	********************** Debug *********************
	**************************************************/
	
	@Override
	public void debugQuery() {
		GLDebug.flushErrors();
		
		GLDebug.writef(GLDebug.ATTRIB_STRING, "Shader:", this);
		
		GLDebug.indent();
		if (errors != null)
			for (String error : errors)
				GLDebug.write(error);
		GLDebug.unindent();
		
		GLDebug.flushErrors();
	}
	
	@Override
	@Nullable
	public String toString() {
		return String.format("%24s:\t\t%s", name, type);
	}
	
}
