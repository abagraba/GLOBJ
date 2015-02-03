package globj.objects.arrays;

import globj.core.DataType;
import globj.objects.BindTracker;
import globj.objects.BindableGLObject;
import globj.objects.bufferobjects.VBO;
import globj.objects.shaders.Attribute;
import lwjgl.debug.Logging;

import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL30;

public class VAO extends BindableGLObject{
	
	private static final BindTracker curr = new BindTracker();
	
	private VAO(String name) {
		super(name, GL15.glGenBuffers());
	}
	
	public static VAO create(String name) {
		VAO vao = new VAO(name);
		if (vao.id == 0) {
			Logging.glError("Cannot create VAO. No ID could be allocated for VAO [" + name + "].", null);
			return null;
		}
		return vao;
	}
	
	/**************************************************/
	/********************** Bind **********************/
	/**************************************************/
	
	private static void bind(int vao) {
		curr.update(vao);
		if (!curr.changed())
			return;
		GL30.glBindVertexArray(vao);
	}

	@Override
	protected void bind() {
		bind(id);
	}
	
	@Override
	protected void bindNone() {
		bind(0);
	}
	
	@Override
	protected void undobind() {
		if (curr.changed())
			GL30.glBindVertexArray(curr.revert());
	}
	
	protected void destroy() {
		if (curr.value() == id)
			bindNone();
		GL30.glDeleteVertexArrays(id);
	}
	
	/**************************************************/
	
	
	public void attach(VBO vbo, VBOFormat format, Attribute attribute){
		vbo.bind();
		GL30.glVertexAttribIPointer(attribute.location, format.type.bytes, format.type.value, format.type.bytes * format.components, format.type.bytes * format.offset);
		vbo.un
	}
	
	
	@Override
	public void debug() {
		// TODO Auto-generated method stub
	}


	
}