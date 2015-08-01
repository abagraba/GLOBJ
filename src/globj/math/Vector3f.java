package globj.math;


import java.nio.FloatBuffer;

import org.lwjgl.BufferUtils;



public class Vector3f {
	public float x, y, z;
	
	public Vector3f(Vector3f v) {
		this.x = v.x;
		this.y = v.y;
		this.z = v.z;
	}
	
	public Vector3f(float x, float y, float z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	public Vector3f() {
		this(0, 0, 0);
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
	
	/**************************************************/
	
	public void add(Vector3f v) {
		x += v.x;
		y += v.y;
		z += v.z;
	}
	
	public void sub(Vector3f v) {
		x -= v.x;
		y -= v.y;
		z -= v.z;
	}
	
	public static Vector3f add(Vector3f a, Vector3f b) {
		return new Vector3f(a.x + b.x, a.y + b.y, a.z + b.z);
	}
	
	public static Vector3f sub(Vector3f a, Vector3f b) {
		return new Vector3f(a.x - b.x, a.y - b.y, a.z - b.z);
	}
	
	/**************************************************/
	
	public void negate() {
		this.x = -x;
		this.y = -y;
		this.z = -z;
	}
	
	public void scale(float scale) {
		this.x *= scale;
		this.y *= scale;
		this.z *= scale;
	}
	
	public void normalize() {
		float norm2 = norm2();
		if (norm2 == 0) {
			x = y = z = 0;
			return;
		}
		float inorm = (float) Math.sqrt(1 / norm2);
		x *= inorm;
		y *= inorm;
		z *= inorm;
	}
	
	/**************************************************/
	
	public float dot(Vector3f v) {
		return x * v.x + y * v.y + z * v.z;
	}
	
	public float norm() {
		return (float) Math.sqrt(norm2());
	}
	
	public float norm2() {
		return x * x + y * y + z * z;
	}
	
	/**
	 * @return the cosine of the angle between the vectors
	 */
	public float cosAngle(Vector3f v) {
		float n2 = norm2();
		float vn2 = v.norm2();
		if (n2 == 0 || vn2 == 0)
			return Float.NaN;
		return dot(v) / (float) Math.sqrt(n2 * vn2);
	}
	
	/**
	 * @return the positive sine of the angle between the vectors
	 */
	public float sinAngle(Vector3f v) {
		float cos = cosAngle(v);
		return (float) Math.sqrt((1 + cos) * (1 - cos));
	}
	
	public float angle(Vector3f v) {
		return (float) Math.acos(cosAngle(v));
	}
	
	/**************************************************/
	
	/**
	 * Quick convenience method for quickly filling FloatBuffer.
	 * 
	 * @return a FloatBuffer filled with the vector.
	 */
	public FloatBuffer toBuffer() {
		FloatBuffer buffer = BufferUtils.createFloatBuffer(3);
		buffer.put(x);
		buffer.put(y);
		buffer.put(z);
		buffer.flip();
		return buffer;
	}
	
	@Override
	public String toString() {
		return String.format("<% .4f, % .4f, % .4f >", x, y, z);
	}
	
}
