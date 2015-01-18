package lwjgl.core.objects.shaders;

import lwjgl.core.objects.GLObject;
import lwjgl.debug.Logging;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;

public class Shader extends GLObject {
	
	protected final int type;
	
	protected Shader(String name, int type) {
		super(name, GL20.glCreateShader(type));
		this.type = type;
	}
	
	public void setShader(String[] code) {
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
		int length = GL20.glGetShaderi(id, GL20.GL_SHADER_SOURCE_LENGTH);
		String[] src = GL20.glGetShaderSource(id, length).split("\n");
		Logging.logInfo(src);
	}
	
}
