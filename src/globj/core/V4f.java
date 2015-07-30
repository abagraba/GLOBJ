package globj.core;


import java.nio.FloatBuffer;

import org.lwjgl.BufferUtils;



public class V4f {
	
	public float	x, y, z, w;
	
	
	public V4f(float x, float y, float z, float w) {
		this.x = x;
		this.y = y;
		this.z = z;
		this.w = w;
	}
	
	public V4f() {
		this(0, 0, 0, 1);
	}
	
	public FloatBuffer asBuffer() {
		FloatBuffer buffer = BufferUtils.createFloatBuffer(4);
		buffer.put(new float[] { x, y, z, w }).flip();
		return buffer;
		
	}
	
	@Override
	public String toString() {
		return String.format("(%.4f, %.4f %.4f, %.4f)", x, y, z, w);
	}
	
}
