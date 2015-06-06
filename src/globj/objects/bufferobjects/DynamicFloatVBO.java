package globj.objects.bufferobjects;

import java.nio.FloatBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL15;

import globj.core.DataType;

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
		GL15.glBufferData(target.value, data, usage.value);
		size = data.limit();
	}
	
	@Override
	public void update(float[] data, int off, int len, int start) {
		FloatBuffer buffer = BufferUtils.createFloatBuffer(data.length);
		buffer.put(data, off, len).flip();
		GL15.glBufferSubData(target.value, start * 4, buffer);
	}
	
	public int debugCount = 4;
	
	@Override
	public void debugContents() {
		FloatBuffer buffer = BufferUtils.createFloatBuffer(size / 4);
		GL15.glGetBufferSubData(target.value, 0, buffer);
		for (int i = 0; i < size / 4; i++) {
			if ((i + 1) % debugCount == 0)
				System.out.println(buffer.get() + "\t");
			else
				System.out.print(buffer.get() + "\t");
		}
	}
}
