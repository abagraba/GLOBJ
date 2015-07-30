package globj.objects.arrays;


import globj.objects.BindTracker;
import globj.objects.BindableGLObject;
import globj.objects.bufferobjects.VBO;
import globj.objects.bufferobjects.VBOTarget;
import globj.objects.shaders.Programs;
import lwjgl.debug.GLDebug;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL14;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;



public class VAO extends BindableGLObject {
	
	private static final String	NON_ARRAY_ERROR	= "VBO target must be Array in order to be attached to VAOs.";
	
	public static final VAO		defaultVAO		= new VAO() {
													@Override
													public void destroy() {
														GLDebug.write("Cannot destroy default VAO");
													}
												};
	
	
	private VAO(String name) {
		super(name, GL30.glGenVertexArrays());
	}
	
	private VAO() {
		super("Default VAO", 0);
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
	
	protected static final BindTracker	bindTracker	= new BindTracker();
	
	
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
	public void attachBuffer(int vertexAttrib, VBO vbo, VBOFormat format) {
		if (vbo.target != VBOTarget.ARRAY) {
			GLDebug.glError(NON_ARRAY_ERROR, null);
			return;
		}
		bind();
		vbo.bind();
		GL20.glVertexAttribPointer(vertexAttrib, format.components(), format.type().value(), false, format.stride(), format.offset());
		vbo.undobind();
		undobind();
	}
	
	// Attaches VBO to the uniform location specified in the currently bound
	// shader program.
	public void attachBuffer(String uniform, VBO vbo, VBOFormat format) {
		if (vbo.target != VBOTarget.ARRAY) {
			GLDebug.glError(NON_ARRAY_ERROR, null);
			return;
		}
		vbo.bind();
		GL30.glVertexAttribIPointer(Programs.current().uniformLocation(uniform), format.components(), format.type().value(),
									format.type().size() * format.stride(), (long) format.type().size() * format.offset());
		vbo.undobind();
	}
	
	/**************************************************/
	/******************* Deprecated *******************/
	/**************************************************/
	
	/**
	 * Instead use {@link #attachBuffer(VBO, VBOFormat, int)}.
	 * 
	 * @deprecated This functionality is deprecated in Core 3.1+ contexts.
	 */
	@Deprecated
	public void attachVertexBuffer(VBO vbo, VBOFormat format) {
		if (vbo.target != VBOTarget.ARRAY) {
			GLDebug.glError(NON_ARRAY_ERROR, null);
			return;
		}
		bind();
		vbo.bind();
		GL11.glVertexPointer(format.components(), format.type().value(), format.stride(), format.offset());
		vbo.undobind();
		undobind();
	}
	
	/**
	 * Instead use {@link #attachBuffer(VBO, VBOFormat, int)}.
	 * 
	 * @deprecated This functionality is deprecated in Core 3.1+ contexts.
	 */
	@Deprecated
	public void attachTexCoordBuffer(VBO vbo, VBOFormat format) {
		if (vbo.target != VBOTarget.ARRAY) {
			GLDebug.glError(NON_ARRAY_ERROR, null);
			return;
		}
		bind();
		vbo.bind();
		GL11.glTexCoordPointer(format.components(), format.type().value(), format.stride(), format.offset());
		vbo.undobind();
		undobind();
	}
	
	/**
	 * Instead use {@link #attachBuffer(VBO, VBOFormat, int)}.
	 * 
	 * @deprecated This functionality is deprecated in Core 3.1+ contexts.
	 */
	@Deprecated
	public void attachColorBuffer(VBO vbo, VBOFormat format) {
		if (vbo.target != VBOTarget.ARRAY) {
			GLDebug.glError(NON_ARRAY_ERROR, null);
			return;
		}
		bind();
		vbo.bind();
		GL11.glColorPointer(format.components(), format.type().value(), format.stride(), format.offset());
		vbo.undobind();
		undobind();
	}
	
	/**
	 * Instead use {@link #attachBuffer(VBO, VBOFormat, int)}.
	 * 
	 * @deprecated This functionality is deprecated in Core 3.1+ contexts.
	 */
	@Deprecated
	public void attachNormalBuffer(VBO vbo, VBOFormat format) {
		if (vbo.target != VBOTarget.ARRAY) {
			GLDebug.glError(NON_ARRAY_ERROR, null);
			return;
		}
		bind();
		vbo.bind();
		GL11.glNormalPointer(format.type().value(), format.stride(), format.offset());
		vbo.undobind();
		undobind();
	}
	
	/*
	 * The glIndexPointer function appears to be missing from LWJGL.
	 * @Deprecated public void attachColorIndexBuffer(VBO vbo, VBOFormat format) { if (vbo.target != VBOTarget.ARRAY) {
	 * GLDebug.glError("VBO target must be Array to be attached to VAOs." , null); return; } vbo.bind();
	 * GL11.glIndexPointer(format.type().size * format.stride(), format.type().size * format.offset()); vbo.undobind();
	 * }
	 */
	
	/**
	 * Instead use {@link #attachBuffer(VBO, VBOFormat, int)}.
	 * 
	 * @deprecated This functionality is deprecated in Core 3.1+ contexts.
	 */
	@Deprecated
	public void attachEdgeFlagBuffer(VBO vbo, VBOFormat format) {
		if (vbo.target != VBOTarget.ARRAY) {
			GLDebug.glError(NON_ARRAY_ERROR, null);
			return;
		}
		bind();
		vbo.bind();
		GL11.glEdgeFlagPointer(format.stride(), format.offset());
		vbo.undobind();
		undobind();
	}
	
	/**
	 * Instead use {@link #attachBuffer(VBO, VBOFormat, int)}.
	 * 
	 * @deprecated This functionality is deprecated in Core 3.1+ contexts.
	 */
	@Deprecated
	public void attachFogCoordBuffer(VBO vbo, VBOFormat format) {
		if (vbo.target != VBOTarget.ARRAY) {
			GLDebug.glError(NON_ARRAY_ERROR, null);
			return;
		}
		bind();
		vbo.bind();
		GL14.glFogCoordPointer(format.type().value(), format.stride(), format.offset());
		vbo.undobind();
		undobind();
	}
	
	// booleans to track which arrays are enabled.
	
	/**************************************************/
	/********************** Debug *********************/
	/**************************************************/
	
	@Override
	public void debugQuery() {
		// TODO Auto-generated method stub
	}
	
}