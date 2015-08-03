package globj.objects.bufferobjects;


import static lwjgl.debug.GLDebug.write;
import static lwjgl.debug.GLDebug.writef;

import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL15;

import globj.core.DataType;
import globj.objects.BindTracker;
import globj.objects.BindableGLObject;
import globj.objects.bufferobjects.values.VBOTarget;
import globj.objects.bufferobjects.values.VBOUsage;



public abstract class VBO extends BindableGLObject {
	
	private final VBOTarget	target;
	private final DataType	dataType;
	private final VBOUsage	usage;
	
	protected int size;
	
	protected VBO(String name, VBOTarget target, DataType dataType, VBOUsage usage) {
		super(name, GL15.glGenBuffers());
		this.target = target;
		this.dataType = dataType;
		this.usage = usage;
	}
	
	public VBOTarget target() {
		return target;
	}
	
	public DataType dataType() {
		return dataType;
	}
	
	public VBOUsage usage() {
		return usage;
	}
	
	/**************************************************
	 ********************** Bind **********************
	 **************************************************/
	
	protected static final Map<VBOTarget, BindTracker> trackers = new HashMap<VBOTarget, BindTracker>();
	
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
		GL15.glBindBuffer(target.value(), id);
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
		for (VBOTarget target : trackers.keySet())
			write(target + ": " + trackers.get(target).value());
	}
	
	/**************************************************/
	
	@Override
	public void debugQuery() {
		writef(toString());
	}
	
	/**
	 * TODO: BUFFER SUBRANGES TODO: BUFFER STORAGE
	 */
	
	public void debugContents() {
		ByteBuffer buffer = BufferUtils.createByteBuffer(size);
		GL15.glGetBufferSubData(target.value(), 0, buffer);
		while (buffer.remaining() >= 4)
			writef("0x%02X 0x%02X 0x%02X 0x%02X", buffer.get(), buffer.get(), buffer.get(), buffer.get());
		switch (buffer.remaining()) {
			case 1:
				writef("0x%02X", buffer.get());
				break;
			case 2:
				writef("0x%02X 0x%02X", buffer.get(), buffer.get());
				break;
			case 3:
				writef("0x%02X 0x%02X 0x%02X", buffer.get(), buffer.get(), buffer.get());
				break;
			default:
		}
	}
	
}