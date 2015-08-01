package globj.math;


import java.nio.FloatBuffer;

import org.lwjgl.BufferUtils;



public class Vector4f {
	public float x, y, z, w;
	
	public Vector4f(Vector4f v) {
		this.x = v.x;
		this.y = v.y;
		this.z = v.z;
		this.w = v.w;
	}
	
	public Vector4f(float x, float y, float z, float w) {
		this.x = x;
		this.y = y;
		this.z = z;
		this.w = w;
	}
	
	public Vector4f() {
		this(0, 0, 0, 1);
	}
	
	public void set(float x, float y) {
		this.x = x;
		this.y = y;
	}
	
	public void set(float x, float y, float z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	public void set(float x, float y, float z, float w) {
		this.x = x;
		this.y = y;
		this.z = z;
		this.w = w;
	}
	
	public void set(Vector4f v) {
		this.x = v.x;
		this.y = v.y;
		this.z = v.z;
		this.w = v.w;
	}
	
	/**************************************************/
	
	public Vector4f add(Vector4f v) {
		return new Vector4f(x + v.x, y + v.y, z + v.z, w + v.w);
	}
	
	public Vector4f sub(Vector4f v) {
		return new Vector4f(x - v.x, y - v.y, z - v.z, w - v.w);
	}
	
	/**************************************************/
	
	public Vector4f negate() {
		this.x = -x;
		this.y = -y;
		this.z = -z;
		this.w = -w;
		return this;
	}
	
	public Vector4f scale(float scale) {
		this.x *= scale;
		this.y *= scale;
		this.z *= scale;
		this.w *= scale;
		return this;
	}
	
	public Vector4f normalize() {
		float norm2 = norm2();
		if (norm2 == 0) {
			x = y = z = w = 0;
			return this;
		}
		float inorm = (float) Math.sqrt(1 / norm2);
		x *= inorm;
		y *= inorm;
		z *= inorm;
		w *= inorm;
		return this;
	}
	
	/**************************************************/
	
	public float dot(Vector4f v) {
		return x * v.x + y * v.y + z * v.z + w * v.w;
	}
	
	public float norm() {
		return (float) Math.sqrt(norm2());
	}
	
	public float norm2() {
		return x * x + y * y + z * z + w * w;
	}
	
	/**
	 * /
	 **************************************************/
	
	/**
	 * Quick convenience method for quickly filling FloatBuffer.
	 * 
	 * @return a FloatBuffer filled with the vector.
	 */
	public FloatBuffer toBuffer() {
		FloatBuffer buffer = BufferUtils.createFloatBuffer(4);
		buffer.put(x);
		buffer.put(y);
		buffer.put(z);
		buffer.put(w);
		buffer.flip();
		return buffer;
	}
	
	@Override
	public String toString() {
		return String.format("<% .4f, % .4f, % .4f, % .4f >", x, y, z, w);
	}
	
}
