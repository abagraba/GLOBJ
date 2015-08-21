package globj.objects.arrays;


import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL14;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

import globj.core.Context;
import globj.core.DataType;
import globj.objects.BindTracker;
import globj.objects.BindableGLObject;
import globj.objects.bufferobjects.VBO;
import globj.objects.bufferobjects.values.VBOTarget;
import globj.objects.shaders.Programs;
import lwjgl.debug.GLDebug;



public class VAO extends BindableGLObject {
	
	private static final String NON_ARRAY_ERROR = "VBO target must be Array in order to be attached to VAOs.";
	
	public static final VAO defaultVAO = new VAO() {
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
	public void attachBuffer(int vertexAttrib, VBO vbo, VBOFormat format) {
		if (vbo.target() != VBOTarget.ARRAY) {
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
		if (vbo.target() != VBOTarget.ARRAY) {
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
	 * Instead use {@link #attachBuffer(int, VBO, VBOFormat)}.
	 * 
	 * @param vbo
	 *            the VBO to be attached to this attribute.
	 * @param format
	 *            the format of the data in the VBO.
	 * @deprecated This function is deprecated in Core profiles.
	 */
	@Deprecated
	public void attachVertexBuffer(VBO vbo, VBOFormat format) {
		if (vbo.target() != VBOTarget.ARRAY) {
			GLDebug.glError(NON_ARRAY_ERROR, null);
			return;
		}
		if (format.components() < 2 || format.components() > 4) {
			GLDebug.glError("Invalid Number of Components", this);
			return;
		}
		if (!isTypeOneOf(format.type(), DataType.FLOAT, DataType.DOUBLE, DataType.INT, DataType.SHORT)) {
			GLDebug.glError("Invalid Data Type", this);
			return;
		}
		bind();
		vbo.bind();
		GL11.glVertexPointer(format.components(), format.type().value(), format.stride(), format.offset());
		vbo.undobind();
		undobind();
	}
	
	/**
	 * Instead use {@link #attachBuffer(int, VBO, VBOFormat)}.
	 * 
	 * @param vbo
	 *            the VBO to be attached to this attribute.
	 * @param format
	 *            the format of the data in the VBO.
	 * @deprecated This function is deprecated in Core profiles.
	 */
	@Deprecated
	public void attachTexCoordBuffer(VBO vbo, VBOFormat format) {
		if (vbo.target() != VBOTarget.ARRAY) {
			GLDebug.glError(NON_ARRAY_ERROR, null);
			return;
		}
		if (format.components() < 1 || format.components() > 4) {
			GLDebug.glError("Invalid Number of Components", this);
			return;
		}
		if (!isTypeOneOf(format.type(), DataType.FLOAT, DataType.DOUBLE, DataType.INT, DataType.SHORT)) {
			GLDebug.glError("Invalid Data Type", this);
			return;
		}
		bind();
		vbo.bind();
		GL11.glTexCoordPointer(format.components(), format.type().value(), format.stride(), format.offset());
		vbo.undobind();
		undobind();
	}
	
	/**
	 * Instead use {@link #attachBuffer(int, VBO, VBOFormat)}.
	 * 
	 * @param vbo
	 *            the VBO to be attached to this attribute.
	 * @param format
	 *            the format of the data in the VBO.
	 * @deprecated This function is deprecated in Core profiles.
	 */
	@Deprecated
	public void attachColorBuffer(VBO vbo, VBOFormat format) {
		if (vbo.target() != VBOTarget.ARRAY) {
			GLDebug.glError(NON_ARRAY_ERROR, null);
			return;
		}
		if (format.components() != 3 && format.components() != 4) {
			GLDebug.glError("Invalid Number of Components", this);
			return;
		}
		if (!isTypeOneOf(	format.type(), DataType.FLOAT, DataType.DOUBLE, DataType.INT, DataType.UINT, DataType.SHORT, DataType.USHORT, DataType.BYTE,
							DataType.UBYTE)) {
			GLDebug.glError("Invalid Data Type", this);
			return;
		}
		bind();
		vbo.bind();
		GL11.glColorPointer(format.components(), format.type().value(), format.stride(), format.offset());
		vbo.undobind();
		undobind();
	}
	
	/**
	 * Instead use {@link #attachBuffer(int, VBO, VBOFormat)}.
	 * 
	 * @param vbo
	 *            the VBO to be attached to this attribute.
	 * @param format
	 *            the format of the data in the VBO.
	 * @deprecated This function is deprecated in Core profiles.
	 */
	@Deprecated
	public void attachNormalBuffer(VBO vbo, VBOFormat format) {
		if (vbo.target() != VBOTarget.ARRAY) {
			GLDebug.glError(NON_ARRAY_ERROR, null);
			return;
		}
		if (!isTypeOneOf(format.type(), DataType.FLOAT, DataType.DOUBLE, DataType.INT, DataType.SHORT, DataType.BYTE)) {
			GLDebug.glError("Invalid Data Type", this);
			return;
		}
		bind();
		vbo.bind();
		GL11.glNormalPointer(format.type().value(), format.stride(), format.offset());
		vbo.undobind();
		undobind();
	}
	
	/**
	 * Instead use {@link #attachBuffer(int, VBO, VBOFormat)}.
	 * 
	 * @param vbo
	 *            the VBO to be attached to this attribute.
	 * @param format
	 *            the format of the data in the VBO.
	 * @deprecated This function is deprecated in Core profiles.
	 */
	@Deprecated
	public void attachColorIndexBuffer(VBO vbo, VBOFormat format) {
		if (vbo.target() != VBOTarget.ARRAY) {
			GLDebug.glError(NON_ARRAY_ERROR, null);
			return;
		}
		if (!isTypeOneOf(format.type(), DataType.FLOAT, DataType.DOUBLE, DataType.INT, DataType.SHORT, DataType.UBYTE)) {
			GLDebug.glError("Invalid Data Type", this);
			return;
		}
		bind();
		vbo.bind();
		GL11.glIndexPointer(format.type().value(), format.stride(), format.offset());
		vbo.undobind();
		undobind();
	}
	
	/**
	 * Instead use {@link #attachBuffer(int, VBO, VBOFormat)}.
	 * 
	 * @param vbo
	 *            the VBO to be attached to this attribute.
	 * @param format
	 *            the format of the data in the VBO.
	 * @deprecated This function is deprecated in Core profiles.
	 */
	@Deprecated
	public void attachEdgeFlagBuffer(VBO vbo, VBOFormat format) {
		if (vbo.target() != VBOTarget.ARRAY) {
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
	 * Instead use {@link #attachBuffer(int, VBO, VBOFormat)}.
	 * 
	 * @param vbo
	 *            the VBO to be attached to this attribute.
	 * @param format
	 *            the format of the data in the VBO.
	 * @deprecated This function is deprecated in Core profiles.
	 */
	@Deprecated
	public void attachSecondaryColorBuffer(VBO vbo, VBOFormat format) {
		if (vbo.target() != VBOTarget.ARRAY) {
			GLDebug.glError(NON_ARRAY_ERROR, null);
			return;
		}
		if (!isTypeOneOf(	format.type(), DataType.FLOAT, DataType.DOUBLE, DataType.INT, DataType.UINT, DataType.SHORT, DataType.USHORT, DataType.BYTE,
							DataType.UBYTE)) {
			GLDebug.glError("Invalid Data Type", this);
			return;
		}
		bind();
		vbo.bind();
		GL14.glSecondaryColorPointer(3, format.type().value(), format.stride(), format.offset());
		vbo.undobind();
		undobind();
	}
	
	/**
	 * Instead use {@link #attachBuffer(int, VBO, VBOFormat)}.
	 * 
	 * @param vbo
	 *            the VBO to be attached to this attribute.
	 * @param format
	 *            the format of the data in the VBO.
	 * @deprecated This function is deprecated in Core profiles.
	 */
	@Deprecated
	public void attachFogCoordBuffer(VBO vbo, VBOFormat format) {
		if (vbo.target() != VBOTarget.ARRAY) {
			GLDebug.glError(NON_ARRAY_ERROR, this);
			return;
		}
		if (!isTypeOneOf(format.type(), DataType.FLOAT, DataType.DOUBLE)) {
			GLDebug.glError("Invalid Data Type", this);
			return;
		}
		bind();
		vbo.bind();
		GL14.glFogCoordPointer(format.type().value(), format.stride(), format.offset());
		vbo.undobind();
		undobind();
	}
	
	private static boolean isTypeOneOf(DataType type, DataType... args) {
		for (DataType arg : args) {
			if (type == arg)
				return true;
		}
		return false;
	}
	
	// booleans to track which arrays are enabled.
	
	/**************************************************
	 ********************** Debug *********************
	 **************************************************/
	
	private void debugLegacyAttach(String name, int enable, int type, int size, int stride, int pointer, int binding) {
		if (GL11.glIsEnabled(enable)) {
			GLDebug.write(name + " active");
			GLDebug.indent();
			GLDebug.writef(	"Type: %-12sSize: %-12sStride: %-12sOffset: %-12s", Context.intValue(size), Context.intValue(size), Context.intValue(stride),
							Context.intValue(pointer));
			GLDebug.unindent();
		}
	}
	
	private void debugLegacyAttachments() {
		if (GL11.glIsEnabled(GL11.GL_VERTEX_ARRAY)) {
			GLDebug.write("Vertex Data active");
			GLDebug.indent();
			GLDebug.writef(	"Type: %-12sSize: %-12sStride: %-12sOffset: %-12s", Context.intValue(GL11.GL_VERTEX_ARRAY_TYPE),
							Context.intValue(GL11.GL_VERTEX_ARRAY_SIZE), Context.intValue(GL11.GL_VERTEX_ARRAY_STRIDE),
							Context.intValue(GL11.GL_VERTEX_ARRAY_POINTER));
			GLDebug.unindent();
		}
		if (GL11.glIsEnabled(GL11.GL_TEXTURE_COORD_ARRAY)) {
			GLDebug.write("Texture Coord Data active");
			GLDebug.indent();
			GLDebug.writef(	"Type: %-12sSize: %-12sStride: %-12sOffset: %-12s", Context.intValue(GL11.GL_TEXTURE_COORD_ARRAY_TYPE),
							Context.intValue(GL11.GL_TEXTURE_COORD_ARRAY_SIZE), Context.intValue(GL11.GL_TEXTURE_COORD_ARRAY_STRIDE),
							Context.intValue(GL11.GL_TEXTURE_COORD_ARRAY_POINTER));
			GLDebug.unindent();
		}
		if (GL11.glIsEnabled(GL11.GL_COLOR_ARRAY)) {
			GLDebug.write("Vertex Data active");
			GLDebug.indent();
			GLDebug.writef(	"Type: %-12sSize: %-12sStride: %-12sOffset: %-12s", Context.intValue(GL11.GL_COLOR_ARRAY_TYPE),
							Context.intValue(GL11.GL_COLOR_ARRAY_SIZE), Context.intValue(GL11.GL_COLOR_ARRAY_STRIDE),
							Context.intValue(GL11.GL_COLOR_ARRAY_POINTER));
			GLDebug.unindent();
		}
		if (GL11.glIsEnabled(GL11.GL_NORMAL_ARRAY)) {
			GLDebug.write("Vertex Data active");
			GLDebug.indent();
			GLDebug.writef(	"Type: %-12sSize: %-12sStride: %-12sOffset: %-12s", Context.intValue(GL11.GL_NORMAL_ARRAY_TYPE), 3,
							Context.intValue(GL11.GL_NORMAL_ARRAY_STRIDE), Context.intValue(GL11.GL_NORMAL_ARRAY_POINTER));
			GLDebug.unindent();
		}
		if (GL11.glIsEnabled(GL11.GL_INDEX_ARRAY)) {
			GLDebug.write("Vertex Data active");
			GLDebug.indent();
			GLDebug.writef(	"Type: %-12sSize: %-12sStride: %-12sOffset: %-12s", Context.intValue(GL11.GL_INDEX_ARRAY_TYPE), 1,
							Context.intValue(GL11.GL_INDEX_ARRAY_STRIDE), Context.intValue(GL11.GL_INDEX_ARRAY_POINTER));
			GLDebug.unindent();
		}
		if (GL11.glIsEnabled(GL11.GL_EDGE_FLAG_ARRAY)) {
			GLDebug.write("Vertex Data active");
			GLDebug.indent();
			GLDebug.writef(	"Type: %-12sSize: %-12sStride: %-12sOffset: %-12s", DataType.BYTE, 1, Context.intValue(GL11.GL_EDGE_FLAG_ARRAY_STRIDE),
							Context.intValue(GL11.GL_EDGE_FLAG_ARRAY_POINTER));
			GLDebug.unindent();
		}
		if (GL11.glIsEnabled(GL14.GL_SECONDARY_COLOR_ARRAY)) {
			GLDebug.write("Vertex Data active");
			GLDebug.indent();
			GLDebug.writef(	"Type: %-12sSize: %-12sStride: %-12sOffset: %-12s", Context.intValue(GL14.GL_SECONDARY_COLOR_ARRAY_TYPE),
							Context.intValue(GL14.GL_SECONDARY_COLOR_ARRAY_SIZE), Context.intValue(GL14.GL_SECONDARY_COLOR_ARRAY_STRIDE),
							Context.intValue(GL14.GL_SECONDARY_COLOR_ARRAY_POINTER));
			GLDebug.unindent();
		}
		if (GL11.glIsEnabled(GL14.GL_FOG_COORDINATE_ARRAY)) {
			GLDebug.write("Vertex Data active");
			GLDebug.indent();
			GLDebug.writef(	"Type: %-12sSize: %-12sStride: %-12sOffset: %-12s", Context.intValue(GL14.GL_FOG_COORDINATE_ARRAY_TYPE), 1,
							Context.intValue(GL14.GL_FOG_COORDINATE_ARRAY_STRIDE), Context.intValue(GL14.GL_FOG_COORDINATE_ARRAY_POINTER));
			GLDebug.unindent();
		}
	}
	
	@Override
	public void debug() {
	
	}
	
	@Override
	public void debugQuery() {
	}
	
}