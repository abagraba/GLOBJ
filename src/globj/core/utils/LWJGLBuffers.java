package globj.core.utils;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.nio.ShortBuffer;

import org.lwjgl.BufferUtils;

public class LWJGLBuffers {
	
	public static FloatBuffer floatBuffer(float[] data) {
		FloatBuffer buffer = BufferUtils.createFloatBuffer(data.length);
		buffer.put(data).flip();
		return buffer;
	}
	
	public static IntBuffer intBuffer(int[] data) {
		IntBuffer buffer = BufferUtils.createIntBuffer(data.length);
		buffer.put(data).flip();
		return buffer;
	}
	
	public static ShortBuffer shortBuffer(short[] data) {
		ShortBuffer buffer = BufferUtils.createShortBuffer(data.length);
		buffer.put(data).flip();
		return buffer;
	}
	
	public static ByteBuffer byteBuffer(byte[] data) {
		ByteBuffer buffer = BufferUtils.createByteBuffer(data.length);
		buffer.put(data).flip();
		return buffer;
	}
	
	public static FloatBuffer floatBuffer(int size) {
		return BufferUtils.createFloatBuffer(size);
	}
	
	public static IntBuffer intBuffer(int size) {
		return BufferUtils.createIntBuffer(size);
	}
	
	public static ShortBuffer shortBuffer(int size) {
		return BufferUtils.createShortBuffer(size);
	}
	
	public static ByteBuffer byteBuffer(int size) {
		return BufferUtils.createByteBuffer(size);
	}
	
}
