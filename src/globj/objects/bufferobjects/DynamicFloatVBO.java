package globj.objects.bufferobjects;


import static lwjgl.debug.GLDebug.ATTRIB_STRING;
import static lwjgl.debug.GLDebug.writef;

import java.nio.FloatBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL15;

import globj.core.DataType;
import globj.objects.bufferobjects.values.VBOTarget;
import lwjgl.debug.GLDebug;



public class DynamicFloatVBO extends DynamicVBO<float[]> {
	
	private DynamicFloatVBO(String name, VBOTarget target) {
		super(name, DataType.FLOAT, target);
	}
	
	public static DynamicFloatVBO create(String name, VBOTarget target) {
		return new DynamicFloatVBO(name, target);
	}
	
	@Override
	public void write(float[] data) {
		FloatBuffer buffer = BufferUtils.createFloatBuffer(data.length);
		buffer.put(data).flip();
		write(buffer);
	}
	
	public void write(FloatBuffer data) {
		orphan();
		GL15.glBufferData(target().value(), data, usage().value());
		size = data.limit() * dataType().size();
	}
	
	@Override
	public void update(float[] data, int off, int len, long start) {
		FloatBuffer buffer = BufferUtils.createFloatBuffer(data.length);
		buffer.put(data, off, len).flip();
		GL15.glBufferSubData(target().value(), start * dataType().size(), buffer);
	}
	
	@Override
	public void debugContents() {
		bind();
		GLDebug.writef(ATTRIB_STRING, "Dynamic Float VBO", toString());
		FloatBuffer buffer = BufferUtils.createFloatBuffer(size / dataType().size());
		GL15.glGetBufferSubData(target().value(), 0, buffer);
		while (buffer.remaining() >= 4)
			writef("% 8f % 8f % 8f % 8f", buffer.get(), buffer.get(), buffer.get(), buffer.get());
		switch (buffer.remaining()) {
			case 1:
				writef("% 8f", buffer.get());
				break;
			case 2:
				writef("% 8f % 8f", buffer.get(), buffer.get());
				break;
			case 3:
				writef("% 8f % 8f % 8f", buffer.get(), buffer.get(), buffer.get());
				break;
			default:
		}
		undobind();
	}
	
}
