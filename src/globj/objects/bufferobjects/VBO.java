package globj.objects.bufferobjects;

import globj.core.DataType;
import globj.objects.BindTracker;
import globj.objects.BindableGLObject;
import globj.objects.bufferobjects.values.VBOUsage;

import java.nio.ByteBuffer;
import java.util.HashMap;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL15;

public abstract class VBO extends BindableGLObject {
	
	public final VBOTarget target;
	public final DataType dataType;
	public final VBOUsage usage;
	
	protected int size;
	
	protected VBO(String name, VBOTarget target, DataType dataType, VBOUsage usage) {
		super(name, GL15.glGenBuffers());
		this.target = target;
		this.dataType = dataType;
		this.usage = usage;
	}
	
	public void update() {
		bind();
		resolveStates();
		undobind();
	}
	
	protected void resolveStates() {
	}
	
	// protected static VBO create(String name, VBOTarget target, DataType
	// dataType, VBOUsage usage) {
	// VBO vbo = new VBO(name, target, dataType, usage);
	// if (vbo.id == 0) {
	// GLDebug.globjError(vbo, "Could not create", "No ID could be allocated");
	// return null;
	// }
	// return vbo;
	// }
	
	/**************************************************/
	/********************** Bind **********************/
	/**************************************************/
	
	protected static final HashMap<VBOTarget, BindTracker> trackers = new HashMap<VBOTarget, BindTracker>();
	static {
		for (VBOTarget target : VBOTarget.values()) {
			trackers.put(target, new BindTracker());
		}
	}
	
	@Override
	protected final BindTracker bindingTracker() {
		return trackers.get(target);
	}
	
	@Override
	protected final void bindOP(int id) {
		GL15.glBindBuffer(target.value, id);
	}
	
	@Override
	protected final void destroyOP() {
		GL15.glDeleteBuffers(id);
	}
	
	@Override
	public final void destroy() {
		super.destroy();
		VBOs.unregisterVBO(this);
	}
	
	public static void tempDebug() {
		for (VBOTarget target : trackers.keySet()) {
			System.out.println(target + ": " + trackers.get(target).value());
		}
	}
	
	/**************************************************/
	
	/**
	 * TODO: BUFFER SUBRANGES TODO: BUFFER STORAGE
	 */
	
	@Override
	public void debugQuery() {
	}
	
	public void debugContents() {
		ByteBuffer buffer = BufferUtils.createByteBuffer(size);
		GL15.glGetBufferSubData(target.value, 0, buffer);
		char[] chars = new char[20];
		int i;
		for (i = 0; i < size / 4; i++) {
			byteText(buffer.get(), chars, 0);
			byteText(buffer.get(), chars, 5);
			byteText(buffer.get(), chars, 10);
			byteText(buffer.get(), chars, 15);
			System.out.println(new String(chars));
		}
		// chars = new char[size % 4];
		for (int j = 0; j < size % 4; j++) {
			byteText(buffer.get(), chars, i * 5);
			System.out.println(new String(chars));
		}
	}
	
	protected final static void byteText(byte byt, char[] chars, int offset) {
		chars[offset] = 48;
		chars[offset + 1] = 120;
		int a = (48 + ((byt >> 4) & 0xF));
		chars[offset + 2] = (char) (a = a > 57 ? a + 7 : a);
		a = (48 + (byt & 0xF));
		chars[offset + 3] = (char) (a = a > 57 ? a + 7 : a);
		chars[offset + 4] = 9;
	}
	
}