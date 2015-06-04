package globj.objects.bufferobjects;

import java.nio.IntBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL15;

import globj.core.DataType;

public class DynamicIntVBO extends DynamicVBO<int[]> {
	
	private DynamicIntVBO(String name, VBOTarget target) {
		super(name, DataType.FLOAT, target);
	}
	
	public static DynamicIntVBO create(String name, VBOTarget target) {
		return new DynamicIntVBO(name, target);
	}
	
	@Override
	public void write(int[] data) {
		orphan();
		IntBuffer buffer = BufferUtils.createIntBuffer(data.length);
		buffer.put(data).flip();
		GL15.glBufferData(target.value, buffer, usage.value);
		size = data.length * 4;
	}
	
	@Override
	public void update(int[] data, int off, int len, int start) {
		IntBuffer buffer = BufferUtils.createIntBuffer(data.length);
		buffer.put(data, off, len).flip();
		GL15.glBufferSubData(target.value, start * 4, buffer);
	}
	
	public int debugCount = 4;
	@Override
	public void debugContents() {
		IntBuffer buffer = BufferUtils.createIntBuffer(size / 4);
		GL15.glGetBufferSubData(target.value, 0, buffer);
		for (int i = 0; i < size / 4; i++){
			if ((i + 1) % debugCount == 0)
				System.out.println(buffer.get() + "\t");
			else 
				System.out.print(buffer.get() + "\t");
		}
	}
}
