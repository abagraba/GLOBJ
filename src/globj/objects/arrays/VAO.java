package globj.objects.arrays;

import globj.objects.BindTracker;
import globj.objects.BindableGLObject;
import globj.objects.bufferobjects.VBO;
import globj.objects.bufferobjects.VBOTarget;
import globj.objects.shaders.Program;
import lwjgl.debug.GLDebug;

import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL30;

public class VAO extends BindableGLObject {
	
	private VAO(String name) {
		super(name, GL15.glGenBuffers());
	}
	
	public static VAO create(String name) {
		VAO vao = new VAO(name);
		if (vao.id == 0) {
			GLDebug.glError("Cannot create VAO. No ID could be allocated for VAO [" + name + "].", null);
			return null;
		}
		return vao;
	}
	
	/**************************************************/
	/********************** Bind **********************/
	/**************************************************/
	
	protected static final BindTracker bindTracker = new BindTracker();
	
	@Override
	protected BindTracker bindingTracker() {
		return bindTracker;
	}
	
	@Override
	protected void bindOP(int id) {
		GL30.glBindVertexArray(id);
	}
	
	@Override
	protected void destroyOP() {
		GL30.glDeleteVertexArrays(id);
	}
	
	/**************************************************/
	
	public void attach(VBO vbo, VBOFormat format, Program program, String uniform) {
		if (vbo.target != VBOTarget.ARRAY) {
			GLDebug.glError("Only VBOs bound to Array Targets can be attached to VAOs.", null);
		}
		vbo.bind();
		GL30.glVertexAttribIPointer(program.uniformLocation(uniform), format.type.bytes, format.type.value, format.type.bytes * format.components,
				format.type.bytes * format.offset);
		vbo.undobind();
	}
	
	@Override
	public void debug() {
		// TODO Auto-generated method stub
	}
	
}