package globj.math;


import java.nio.FloatBuffer;

import org.lwjgl.BufferUtils;



public class Matrix4x4f {
	public float m00, m01, m02, m03, m10, m11, m12, m13, m20, m21, m22, m23, m30, m31, m32, m33;
	
	public Matrix4x4f(Matrix4x4f m) {
		set(m);
	}
	
	public Matrix4x4f() {
		setIdentity();
	}
	
	public Matrix4x4f set(Matrix4x4f m) {
		this.m00 = m.m00;
		this.m01 = m.m01;
		this.m02 = m.m02;
		this.m03 = m.m03;
		this.m10 = m.m10;
		this.m11 = m.m11;
		this.m12 = m.m12;
		this.m13 = m.m13;
		this.m20 = m.m20;
		this.m21 = m.m21;
		this.m22 = m.m22;
		this.m23 = m.m23;
		this.m30 = m.m30;
		this.m31 = m.m31;
		this.m32 = m.m32;
		this.m33 = m.m33;
		return this;
	}
	
	/**************************************************/
	
	public Matrix4x4f add(Matrix4x4f matrix) {
		Matrix4x4f m = new Matrix4x4f();
		m.m00 = m00 + matrix.m00;
		m.m01 = m01 + matrix.m01;
		m.m02 = m02 + matrix.m02;
		m.m03 = m03 + matrix.m03;
		m.m10 = m10 + matrix.m10;
		m.m11 = m11 + matrix.m11;
		m.m12 = m12 + matrix.m12;
		m.m13 = m13 + matrix.m13;
		m.m20 = m20 + matrix.m20;
		m.m21 = m21 + matrix.m21;
		m.m22 = m22 + matrix.m22;
		m.m23 = m23 + matrix.m23;
		m.m30 = m30 + matrix.m30;
		m.m31 = m31 + matrix.m31;
		m.m32 = m32 + matrix.m32;
		m.m33 = m33 + matrix.m33;
		return m;
	}
	
	public Matrix4x4f sub(Matrix4x4f matrix) {
		Matrix4x4f m = new Matrix4x4f();
		m.m00 = m00 - matrix.m00;
		m.m01 = m01 - matrix.m01;
		m.m02 = m02 - matrix.m02;
		m.m03 = m03 - matrix.m03;
		m.m10 = m10 - matrix.m10;
		m.m11 = m11 - matrix.m11;
		m.m12 = m12 - matrix.m12;
		m.m13 = m13 - matrix.m13;
		m.m20 = m20 - matrix.m20;
		m.m21 = m21 - matrix.m21;
		m.m22 = m22 - matrix.m22;
		m.m23 = m23 - matrix.m23;
		m.m30 = m30 - matrix.m30;
		m.m31 = m31 - matrix.m31;
		m.m32 = m32 - matrix.m32;
		m.m33 = m33 - matrix.m33;
		return m;
	}
	
	public Matrix4x4f transpose() {
		Matrix4x4f m = new Matrix4x4f();
		m.m00 = m00;
		m.m01 = m10;
		m.m02 = m20;
		m.m03 = m30;
		m.m10 = m01;
		m.m11 = m11;
		m.m12 = m21;
		m.m13 = m31;
		m.m20 = m02;
		m.m21 = m12;
		m.m22 = m22;
		m.m23 = m32;
		m.m30 = m03;
		m.m31 = m13;
		m.m32 = m23;
		m.m33 = m33;
		return m;
	}
	
	/**************************************************/
	
	public Matrix4x4f negate() {
		m00 = -m00;
		m01 = -m01;
		m02 = -m02;
		m03 = -m03;
		m10 = -m10;
		m11 = -m11;
		m12 = -m12;
		m13 = -m13;
		m20 = -m20;
		m21 = -m21;
		m22 = -m22;
		m23 = -m23;
		m30 = -m30;
		m31 = -m31;
		m32 = -m32;
		m33 = -m33;
		return this;
	}
	
	public Matrix4x4f scale(float f) {
		m00 *= f;
		m01 *= f;
		m02 *= f;
		m03 *= f;
		m10 *= f;
		m11 *= f;
		m12 *= f;
		m13 *= f;
		m20 *= f;
		m21 *= f;
		m22 *= f;
		m23 *= f;
		m30 *= f;
		m31 *= f;
		m32 *= f;
		m33 *= f;
		return this;
	}
	
	public Matrix4x4f scaleColumns(Vector4f v) {
		m00 *= v.x;
		m01 *= v.x;
		m02 *= v.x;
		m03 *= v.x;
		m10 *= v.y;
		m11 *= v.y;
		m12 *= v.y;
		m13 *= v.y;
		m20 *= v.z;
		m21 *= v.z;
		m22 *= v.z;
		m23 *= v.z;
		m30 *= v.w;
		m31 *= v.w;
		m32 *= v.w;
		m33 *= v.w;
		return this;
	}
	
	public Matrix4x4f scaleRows(Vector4f v) {
		m00 *= v.x;
		m01 *= v.y;
		m02 *= v.z;
		m03 *= v.w;
		m10 *= v.x;
		m11 *= v.y;
		m12 *= v.z;
		m13 *= v.w;
		m20 *= v.x;
		m21 *= v.y;
		m22 *= v.z;
		m23 *= v.w;
		m30 *= v.x;
		m31 *= v.y;
		m32 *= v.z;
		m33 *= v.w;
		return this;
	}
	
	public Matrix4x4f setIdentity() {
		m00 = 1.0f;
		m01 = 0.0f;
		m02 = 0.0f;
		m03 = 0.0f;
		m10 = 0.0f;
		m11 = 1.0f;
		m12 = 0.0f;
		m13 = 0.0f;
		m20 = 0.0f;
		m21 = 0.0f;
		m22 = 1.0f;
		m23 = 0.0f;
		m30 = 0.0f;
		m31 = 0.0f;
		m32 = 0.0f;
		m33 = 1.0f;
		return this;
	}
	
	/**************************************************/
	
	public FloatBuffer toBuffer() {
		FloatBuffer buffer = BufferUtils.createFloatBuffer(16);
		buffer.put(m00);
		buffer.put(m01);
		buffer.put(m02);
		buffer.put(m03);
		buffer.put(m10);
		buffer.put(m11);
		buffer.put(m12);
		buffer.put(m13);
		buffer.put(m20);
		buffer.put(m21);
		buffer.put(m22);
		buffer.put(m23);
		buffer.put(m30);
		buffer.put(m31);
		buffer.put(m32);
		buffer.put(m33);
		buffer.flip();
		return buffer;
	}
	
	public FloatBuffer toBufferTranspose() {
		FloatBuffer buffer = BufferUtils.createFloatBuffer(16);
		buffer.put(m00);
		buffer.put(m10);
		buffer.put(m20);
		buffer.put(m30);
		buffer.put(m01);
		buffer.put(m11);
		buffer.put(m21);
		buffer.put(m31);
		buffer.put(m02);
		buffer.put(m12);
		buffer.put(m22);
		buffer.put(m32);
		buffer.put(m03);
		buffer.put(m13);
		buffer.put(m23);
		buffer.put(m33);
		buffer.flip();
		return buffer;
	}
	
	@Override
	public String toString() {
		return String.format(	"<% .4f, % .4f, % .4f, % .4f >%n|% .4f, % .4f, % .4f, % .4f |%n|% .4f, % .4f, % .4f, % .4f |%n<% .4f, % .4f, % .4f, % .4f >", m00, m10,
								m20, m30, m01, m11, m21, m31, m02, m12, m22, m32, m03, m13, m23, m33);
	}
	
	/**************************************************/
	/**
	 * Multiply the right matrix by the left and place the result in a third matrix.
	 * 
	 * @param left
	 *            The left source matrix
	 * @param right
	 *            The right source matrix
	 * @param dest
	 *            The destination matrix, or null if a new one is to be created
	 * @return the destination matrix
	 */
	public static Matrix4x4f mul(Matrix4x4f left, Matrix4x4f right, Matrix4x4f dest) {
		if (dest == null)
			dest = new Matrix4x4f();
			
		float m00 = left.m00 * right.m00 + left.m10 * right.m01 + left.m20 * right.m02 + left.m30 * right.m03;
		float m01 = left.m01 * right.m00 + left.m11 * right.m01 + left.m21 * right.m02 + left.m31 * right.m03;
		float m02 = left.m02 * right.m00 + left.m12 * right.m01 + left.m22 * right.m02 + left.m32 * right.m03;
		float m03 = left.m03 * right.m00 + left.m13 * right.m01 + left.m23 * right.m02 + left.m33 * right.m03;
		float m10 = left.m00 * right.m10 + left.m10 * right.m11 + left.m20 * right.m12 + left.m30 * right.m13;
		float m11 = left.m01 * right.m10 + left.m11 * right.m11 + left.m21 * right.m12 + left.m31 * right.m13;
		float m12 = left.m02 * right.m10 + left.m12 * right.m11 + left.m22 * right.m12 + left.m32 * right.m13;
		float m13 = left.m03 * right.m10 + left.m13 * right.m11 + left.m23 * right.m12 + left.m33 * right.m13;
		float m20 = left.m00 * right.m20 + left.m10 * right.m21 + left.m20 * right.m22 + left.m30 * right.m23;
		float m21 = left.m01 * right.m20 + left.m11 * right.m21 + left.m21 * right.m22 + left.m31 * right.m23;
		float m22 = left.m02 * right.m20 + left.m12 * right.m21 + left.m22 * right.m22 + left.m32 * right.m23;
		float m23 = left.m03 * right.m20 + left.m13 * right.m21 + left.m23 * right.m22 + left.m33 * right.m23;
		float m30 = left.m00 * right.m30 + left.m10 * right.m31 + left.m20 * right.m32 + left.m30 * right.m33;
		float m31 = left.m01 * right.m30 + left.m11 * right.m31 + left.m21 * right.m32 + left.m31 * right.m33;
		float m32 = left.m02 * right.m30 + left.m12 * right.m31 + left.m22 * right.m32 + left.m32 * right.m33;
		float m33 = left.m03 * right.m30 + left.m13 * right.m31 + left.m23 * right.m32 + left.m33 * right.m33;
		
		dest.m00 = m00;
		dest.m01 = m01;
		dest.m02 = m02;
		dest.m03 = m03;
		dest.m10 = m10;
		dest.m11 = m11;
		dest.m12 = m12;
		dest.m13 = m13;
		dest.m20 = m20;
		dest.m21 = m21;
		dest.m22 = m22;
		dest.m23 = m23;
		dest.m30 = m30;
		dest.m31 = m31;
		dest.m32 = m32;
		dest.m33 = m33;
		
		return dest;
	}
	
	/**
	 * Transform a Vector by a matrix and return the result in a destination vector.
	 * 
	 * @param left
	 *            The left matrix
	 * @param right
	 *            The right vector
	 * @param dest
	 *            The destination vector, or null if a new one is to be created
	 * @return the destination vector
	 */
	public static Vector4f transform(Matrix4x4f left, Vector4f right, Vector4f dest) {
		if (dest == null)
			dest = new Vector4f();
			
		float x = left.m00 * right.x + left.m10 * right.y + left.m20 * right.z + left.m30 * right.w;
		float y = left.m01 * right.x + left.m11 * right.y + left.m21 * right.z + left.m31 * right.w;
		float z = left.m02 * right.x + left.m12 * right.y + left.m22 * right.z + left.m32 * right.w;
		float w = left.m03 * right.x + left.m13 * right.y + left.m23 * right.z + left.m33 * right.w;
		
		dest.x = x;
		dest.y = y;
		dest.z = z;
		dest.w = w;
		
		return dest;
	}
	
	/**
	 * Rotates the matrix around the given axis the specified angle
	 * 
	 * @param angle
	 *            the angle, in radians.
	 * @param axis
	 *            The vector representing the rotation axis. Must be normalized.
	 * @return this
	 */
	public Matrix4x4f rotate(float angle, Vector3f axis) {
		return rotate(angle, axis, this);
	}
	
	/**
	 * Rotates the matrix around the given axis the specified angle
	 * 
	 * @param angle
	 *            the angle, in radians.
	 * @param axis
	 *            The vector representing the rotation axis. Must be normalized.
	 * @param dest
	 *            The matrix to put the result, or null if a new matrix is to be created
	 * @return The rotated matrix
	 */
	public Matrix4x4f rotate(float angle, Vector3f axis, Matrix4x4f dest) {
		return rotate(angle, axis, this, dest);
	}
	
	/**
	 * Rotates the source matrix around the given axis the specified angle and put the result in the destination matrix.
	 * 
	 * @param angle
	 *            the angle, in radians.
	 * @param axis
	 *            The vector representing the rotation axis. Must be normalized.
	 * @param src
	 *            The matrix to rotate
	 * @param dest
	 *            The matrix to put the result, or null if a new matrix is to be created
	 * @return The rotated matrix
	 */
	public static Matrix4x4f rotate(float angle, Vector3f axis, Matrix4x4f src, Matrix4x4f dest) {
		if (dest == null)
			dest = new Matrix4x4f();
		float c = (float) Math.cos(angle);
		float s = (float) Math.sin(angle);
		float oneminusc = 1.0f - c;
		float xy = axis.x * axis.y;
		float yz = axis.y * axis.z;
		float xz = axis.x * axis.z;
		float xs = axis.x * s;
		float ys = axis.y * s;
		float zs = axis.z * s;
		
		float f00 = axis.x * axis.x * oneminusc + c;
		float f01 = xy * oneminusc + zs;
		float f02 = xz * oneminusc - ys;
		// n[3] not used
		float f10 = xy * oneminusc - zs;
		float f11 = axis.y * axis.y * oneminusc + c;
		float f12 = yz * oneminusc + xs;
		// n[7] not used
		float f20 = xz * oneminusc + ys;
		float f21 = yz * oneminusc - xs;
		float f22 = axis.z * axis.z * oneminusc + c;
		
		float t00 = src.m00 * f00 + src.m10 * f01 + src.m20 * f02;
		float t01 = src.m01 * f00 + src.m11 * f01 + src.m21 * f02;
		float t02 = src.m02 * f00 + src.m12 * f01 + src.m22 * f02;
		float t03 = src.m03 * f00 + src.m13 * f01 + src.m23 * f02;
		float t10 = src.m00 * f10 + src.m10 * f11 + src.m20 * f12;
		float t11 = src.m01 * f10 + src.m11 * f11 + src.m21 * f12;
		float t12 = src.m02 * f10 + src.m12 * f11 + src.m22 * f12;
		float t13 = src.m03 * f10 + src.m13 * f11 + src.m23 * f12;
		dest.m20 = src.m00 * f20 + src.m10 * f21 + src.m20 * f22;
		dest.m21 = src.m01 * f20 + src.m11 * f21 + src.m21 * f22;
		dest.m22 = src.m02 * f20 + src.m12 * f21 + src.m22 * f22;
		dest.m23 = src.m03 * f20 + src.m13 * f21 + src.m23 * f22;
		dest.m00 = t00;
		dest.m01 = t01;
		dest.m02 = t02;
		dest.m03 = t03;
		dest.m10 = t10;
		dest.m11 = t11;
		dest.m12 = t12;
		dest.m13 = t13;
		return dest;
	}
	
	/**
	 * @return the determinant of the matrix
	 */
	public float determinant() {
		float f = m00 * ((m11 * m22 * m33 + m12 * m23 * m31 + m13 * m21 * m32) - m13 * m22 * m31 - m11 * m23 * m32 - m12 * m21 * m33);
		f -= m01 * ((m10 * m22 * m33 + m12 * m23 * m30 + m13 * m20 * m32) - m13 * m22 * m30 - m10 * m23 * m32 - m12 * m20 * m33);
		f += m02 * ((m10 * m21 * m33 + m11 * m23 * m30 + m13 * m20 * m31) - m13 * m21 * m30 - m10 * m23 * m31 - m11 * m20 * m33);
		f -= m03 * ((m10 * m21 * m32 + m11 * m22 * m30 + m12 * m20 * m31) - m12 * m21 * m30 - m10 * m22 * m31 - m11 * m20 * m32);
		return f;
	}
	
	/**
	 * Calculate the determinant of a 3x3 matrix
	 * 
	 * @return result
	 */
	
	private static float determinant3x3(float t00, float t01, float t02, float t10, float t11, float t12, float t20, float t21, float t22) {
		return t00 * (t11 * t22 - t12 * t21) + t01 * (t12 * t20 - t10 * t22) + t02 * (t10 * t21 - t11 * t20);
	}
	
	/**
	 * Invert this matrix
	 * 
	 * @return this if successful, null otherwise
	 */
	public Matrix4x4f invert() {
		return invert(this, this);
	}
	
	/**
	 * Invert the source matrix and put the result in the destination
	 * 
	 * @param src
	 *            The source matrix
	 * @param dest
	 *            The destination matrix, or null if a new matrix is to be created
	 * @return The inverted matrix if successful, null otherwise
	 */
	public static Matrix4x4f invert(Matrix4x4f src, Matrix4x4f dest) {
		float determinant = src.determinant();
		
		if (determinant != 0) {
			/*
			 * m00 m01 m02 m03 m10 m11 m12 m13 m20 m21 m22 m23 m30 m31 m32 m33
			 */
			if (dest == null)
				dest = new Matrix4x4f();
			float determinant_inv = 1f / determinant;
			
			// first row
			float t00 = determinant3x3(src.m11, src.m12, src.m13, src.m21, src.m22, src.m23, src.m31, src.m32, src.m33);
			float t01 = -determinant3x3(src.m10, src.m12, src.m13, src.m20, src.m22, src.m23, src.m30, src.m32, src.m33);
			float t02 = determinant3x3(src.m10, src.m11, src.m13, src.m20, src.m21, src.m23, src.m30, src.m31, src.m33);
			float t03 = -determinant3x3(src.m10, src.m11, src.m12, src.m20, src.m21, src.m22, src.m30, src.m31, src.m32);
			// second row
			float t10 = -determinant3x3(src.m01, src.m02, src.m03, src.m21, src.m22, src.m23, src.m31, src.m32, src.m33);
			float t11 = determinant3x3(src.m00, src.m02, src.m03, src.m20, src.m22, src.m23, src.m30, src.m32, src.m33);
			float t12 = -determinant3x3(src.m00, src.m01, src.m03, src.m20, src.m21, src.m23, src.m30, src.m31, src.m33);
			float t13 = determinant3x3(src.m00, src.m01, src.m02, src.m20, src.m21, src.m22, src.m30, src.m31, src.m32);
			// third row
			float t20 = determinant3x3(src.m01, src.m02, src.m03, src.m11, src.m12, src.m13, src.m31, src.m32, src.m33);
			float t21 = -determinant3x3(src.m00, src.m02, src.m03, src.m10, src.m12, src.m13, src.m30, src.m32, src.m33);
			float t22 = determinant3x3(src.m00, src.m01, src.m03, src.m10, src.m11, src.m13, src.m30, src.m31, src.m33);
			float t23 = -determinant3x3(src.m00, src.m01, src.m02, src.m10, src.m11, src.m12, src.m30, src.m31, src.m32);
			// fourth row
			float t30 = -determinant3x3(src.m01, src.m02, src.m03, src.m11, src.m12, src.m13, src.m21, src.m22, src.m23);
			float t31 = determinant3x3(src.m00, src.m02, src.m03, src.m10, src.m12, src.m13, src.m20, src.m22, src.m23);
			float t32 = -determinant3x3(src.m00, src.m01, src.m03, src.m10, src.m11, src.m13, src.m20, src.m21, src.m23);
			float t33 = determinant3x3(src.m00, src.m01, src.m02, src.m10, src.m11, src.m12, src.m20, src.m21, src.m22);
			
			// transpose and divide by the determinant
			dest.m00 = t00 * determinant_inv;
			dest.m11 = t11 * determinant_inv;
			dest.m22 = t22 * determinant_inv;
			dest.m33 = t33 * determinant_inv;
			dest.m01 = t10 * determinant_inv;
			dest.m10 = t01 * determinant_inv;
			dest.m20 = t02 * determinant_inv;
			dest.m02 = t20 * determinant_inv;
			dest.m12 = t21 * determinant_inv;
			dest.m21 = t12 * determinant_inv;
			dest.m03 = t30 * determinant_inv;
			dest.m30 = t03 * determinant_inv;
			dest.m13 = t31 * determinant_inv;
			dest.m31 = t13 * determinant_inv;
			dest.m32 = t23 * determinant_inv;
			dest.m23 = t32 * determinant_inv;
			return dest;
		}
		else
			return null;
	}
}
