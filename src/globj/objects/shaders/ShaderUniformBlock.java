package globj.objects.shaders;

import java.nio.IntBuffer;
import java.util.ArrayList;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL31;
import org.lwjgl.opengl.GL43;

import globj.core.GL;
import globj.core.utils.LWJGLBuffers;
import globj.objects.GLObject;
import globj.objects.GLObjectTracker;
import lwjgl.debug.GLDebug;

public class ShaderUniformBlock extends GLObject {
	
	public GLObjectTracker<ShaderUniform> uniforms = new GLObjectTracker<ShaderUniform>();
	public int blockIndex;
	public int minimumSize = -1;
	
	protected ArrayList<ShaderType> shaders = new ArrayList<ShaderType>();
	
	private ShaderUniformBlock(int program, int index, boolean legacy) {
		super(name31(program, index), index);
		blockIndex = GL31.glGetUniformBlockIndex(program, name);
		for (ShaderType shader : ShaderType.values())
			if (GL31.glGetActiveUniformBlocki(program, id, shader.referenceLegacy) != GL11.GL_FALSE)
				shaders.add(shader);
	}
	
	private ShaderUniformBlock(int program, int index) {
		super(name43(program, index), index);
		blockIndex = GL31.glGetUniformBlockIndex(program, name);
		for (ShaderType shader : ShaderType.values())
			if (getResource(program, shader.reference, index) != 0)
				shaders.add(shader);
		IntBuffer res = getResource(program, new int[] { GL43.GL_BUFFER_DATA_SIZE }, index, 1);
		minimumSize = res.get();
	}
	
	// TODO GL_BUFFER_BINDING GL_BUFFER_DATA_SIZE
	
	/**************************************************/
	
	public static ShaderUniformBlock buildUniformBlock(int program, int index) {
		if (GL.versionCheck(4, 3))
			return new ShaderUniformBlock(program, index);
		else
			return new ShaderUniformBlock(program, index, false);
	}
	
	private static IntBuffer getResource(int program, int[] args, int index, int results) {
		IntBuffer req = LWJGLBuffers.intBuffer(args);
		IntBuffer res = LWJGLBuffers.intBuffer(results);
		GL43.glGetProgramResource(program, GL43.GL_UNIFORM_BLOCK, index, req, null, res);
		return res;
	}
	
	private static int getResource(int program, int arg, int index) {
		return getResource(program, new int[] { arg }, index, 1).get();
	}
	
	/**************************************************/
	
	private static String name31(int program, int index) {
		int blockNameSize = GL31.glGetActiveUniformBlocki(program, index, GL31.GL_UNIFORM_BLOCK_NAME_LENGTH);
		return stripArray(GL31.glGetActiveUniformBlockName(program, index, blockNameSize));
	}
	
	private static String name43(int program, int index) {
		int length = getResource(program, GL43.GL_NAME_LENGTH, index);
		return stripArray(GL43.glGetProgramResourceName(program, GL43.GL_UNIFORM_BLOCK, index, length));
	}
	
	private static String stripArray(String name) {
		if (name.endsWith("[0]"))
			return name.substring(0, name.length() - 3) + "[]";
		return name;
	}
	
	/**************************************************/
	
	protected void addUniform(ShaderUniform uniform) {
		uniforms.add(uniform);
	}
	
	/**************************************************/
	
	@Override
	public void debug() {
		GLDebug.write(this);
	}
	
	@Override
	public String toString() {
	if (minimumSize != -1)
			return GLDebug.fixedString("Block:\t" + name, 30) + "Minimum Size: " + minimumSize + " bytes";
		return GLDebug.fixedString("Block:\t" + name, 30);
	}
	
}
