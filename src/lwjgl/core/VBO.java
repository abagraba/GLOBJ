package lwjgl.core;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.nio.ShortBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL15;

public class VBO {

	private int vbo = 0;
	private int target;

	public VBO(int target) {
		this.target = target;
	}
	
	public void bufferData(ShortBuffer data) {
		if (vbo == 0) {
			vbo = GL15.glGenBuffers();
		}
		GL15.glBindBuffer(target, vbo);
		GL15.glBufferData(target, data, GL15.GL_DYNAMIC_DRAW);
	}
	
	public void bufferData(short[] data) {
		ShortBuffer buffer = BufferUtils.createShortBuffer(data.length);
		buffer.put(data);
		buffer.flip();
		bufferData(buffer);
	}

	public void bufferData(IntBuffer data) {
		if (vbo == 0) {
			vbo = GL15.glGenBuffers();
		}
		GL15.glBindBuffer(target, vbo);
		GL15.glBufferData(target, data, GL15.GL_DYNAMIC_DRAW);
	}
	
	public void bufferData(int[] data) {
		IntBuffer buffer = BufferUtils.createIntBuffer(data.length);
		buffer.put(data);
		buffer.flip();
		bufferData(buffer);
	}

	public void bufferData(FloatBuffer data) {
		if (vbo == 0) {
			vbo = GL15.glGenBuffers();
		}
		GL15.glBindBuffer(target, vbo);
		GL15.glBufferData(target, data, GL15.GL_DYNAMIC_DRAW);
	}
	
	public void bufferData(float[] data) {
		FloatBuffer buffer = BufferUtils.createFloatBuffer(data.length);
		buffer.put(data);
		buffer.flip();
		bufferData(buffer);
	}

	public void deleteBuffer() {
		GL15.glDeleteBuffers(vbo);
		vbo = 0;
	}

	public boolean exists(){
		return vbo != 0;
	}

	public void use(){
		GL15.glBindBuffer(target, vbo);
	}

	public static void useNone(int target){
		GL15.glBindBuffer(target, 0);
	}
}