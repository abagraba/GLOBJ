package globj.objects.bufferobjects;


import static lwjgl.debug.GLDebug.ATTRIB_STRING;
import static lwjgl.debug.GLDebug.writef;

import java.nio.IntBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL15;

import globj.core.DataType;
import globj.objects.bufferobjects.values.VBOTarget;
import lwjgl.debug.GLDebug;



public class DynamicIntVBO extends DynamicVBO<int[]> {
	
	private DynamicIntVBO(String name, VBOTarget target) {
		super(name, DataType.INT, target);
	}
	
	public static DynamicIntVBO create(String name, VBOTarget target) {
		return new DynamicIntVBO(name, target);
	}
	
	@Override
	public void write(int[] data) {
		orphan();
		IntBuffer buffer = BufferUtils.createIntBuffer(data.length);
		buffer.put(data).flip();
		GL15.glBufferData(target().value(), buffer, usage().value());
		size = data.length * dataType().size();
	}
	
	public void write(IntBuffer data) {
		orphan();
		GL15.glBufferData(target().value(), data, usage().value());
		size = data.limit() * dataType().size();
	}
	
	@Override
	public void update(int[] data, int off, int len, long start) {
		IntBuffer buffer = BufferUtils.createIntBuffer(data.length);
		buffer.put(data, off, len).flip();
		GL15.glBufferSubData(target().value(), start * dataType().size(), buffer);
	}
	
	@Override
	public void debugContents() {
		bind();
		GLDebug.writef(ATTRIB_STRING, "Dynamic Int VBO", toString());
		IntBuffer buffer = BufferUtils.createIntBuffer(size / dataType().size());
		GL15.glGetBufferSubData(target().value(), 0, buffer);
		while (buffer.remaining() >= 4)
			writef("% 8d % 8d % 8d % 8d", buffer.get(), buffer.get(), buffer.get(), buffer.get());
		switch (buffer.remaining()) {
			case 1:
				writef("% 8d", buffer.get());
				break;
			case 2:
				writef("% 8d % 8d", buffer.get(), buffer.get());
				break;
			case 3:
				writef("% 8d % 8d % 8d", buffer.get(), buffer.get(), buffer.get());
				break;
			default:
		}
		undobind();
	}
}
