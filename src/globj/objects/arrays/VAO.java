package globj.objects.arrays;

import globj.core.DataType;
import globj.objects.BindTracker;
import globj.objects.BindableGLObject;
import globj.objects.bufferobjects.VBO;
import globj.objects.shaders.Attribute;
import lwjgl.debug.GLDebug;
import lwjgl.debug.Logging;

import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

public class VAO extends BindableGLObject{
	
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
	
	
	public void attach(VBO vbo, VBOFormat format, Attribute attribute){
		VBOs.vbo.bind();
		GL30.glVertexAttribIPointer(attribute.location, format.type.bytes, format.type.value, format.type.bytes * format.components, format.type.bytes * format.offset);
		vbo.un
	}
	
	
	@Override
	public void debug() {
		// TODO Auto-generated method stub
	}


	
}