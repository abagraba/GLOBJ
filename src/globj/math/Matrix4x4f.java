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
	
	public Matrix4x4f mul(Matrix4x4f matrix) {
		Matrix4x4f m = new Matrix4x4f();
		m.m00 = m00 * matrix.m00 + m10 * matrix.m01 + m20 * matrix.m02 + m30 * matrix.m03;
		m.m01 = m01 * matrix.m00 + m11 * matrix.m01 + m21 * matrix.m02 + m31 * matrix.m03;
		m.m02 = m02 * matrix.m00 + m12 * matrix.m01 + m22 * matrix.m02 + m32 * matrix.m03;
		m.m03 = m03 * matrix.m00 + m13 * matrix.m01 + m23 * matrix.m02 + m33 * matrix.m03;
		m.m10 = m00 * matrix.m10 + m10 * matrix.m11 + m20 * matrix.m12 + m30 * matrix.m13;
		m.m11 = m01 * matrix.m10 + m11 * matrix.m11 + m21 * matrix.m12 + m31 * matrix.m13;
		m.m12 = m02 * matrix.m10 + m12 * matrix.m11 + m22 * matrix.m12 + m32 * matrix.m13;
		m.m13 = m03 * matrix.m10 + m13 * matrix.m11 + m23 * matrix.m12 + m33 * matrix.m13;
		m.m20 = m00 * matrix.m20 + m10 * matrix.m21 + m20 * matrix.m22 + m30 * matrix.m23;
		m.m21 = m01 * matrix.m20 + m11 * matrix.m21 + m21 * matrix.m22 + m31 * matrix.m23;
		m.m22 = m02 * matrix.m20 + m12 * matrix.m21 + m22 * matrix.m22 + m32 * matrix.m23;
		m.m23 = m03 * matrix.m20 + m13 * matrix.m21 + m23 * matrix.m22 + m33 * matrix.m23;
		m.m30 = m00 * matrix.m30 + m10 * matrix.m31 + m20 * matrix.m32 + m30 * matrix.m33;
		m.m31 = m01 * matrix.m30 + m11 * matrix.m31 + m21 * matrix.m32 + m31 * matrix.m33;
		m.m32 = m02 * matrix.m30 + m12 * matrix.m31 + m22 * matrix.m32 + m32 * matrix.m33;
		m.m33 = m03 * matrix.m30 + m13 * matrix.m31 + m23 * matrix.m32 + m33 * matrix.m33;
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
		return String.format(	"<% .4f, % .4f, % .4f, % .4f >%n|% .4f, % .4f, % .4f, % .4f |%n|% .4f, % .4f, % .4f, % .4f |%n<% .4f, % .4f, % .4f, % .4f >", m00,
								m10, m20, m30, m01, m11, m21, m31, m02, m12, m22, m32, m03, m13, m23, m33);
	}
	
	public float determinant() {
		float f = m00 * ((m11 * m22 * m33 + m12 * m23 * m31 + m13 * m21 * m32) - m13 * m22 * m31 - m11 * m23 * m32 - m12 * m21 * m33);
		f -= m01 * ((m10 * m22 * m33 + m12 * m23 * m30 + m13 * m20 * m32) - m13 * m22 * m30 - m10 * m23 * m32 - m12 * m20 * m33);
		f += m02 * ((m10 * m21 * m33 + m11 * m23 * m30 + m13 * m20 * m31) - m13 * m21 * m30 - m10 * m23 * m31 - m11 * m20 * m33);
		f -= m03 * ((m10 * m21 * m32 + m11 * m22 * m30 + m12 * m20 * m31) - m12 * m21 * m30 - m10 * m22 * m31 - m11 * m20 * m32);
		return f;
	}
	
	private static float determinant3x3(float t00, float t01, float t02, float t10, float t11, float t12, float t20, float t21, float t22) {
		return t00 * (t11 * t22 - t12 * t21) + t01 * (t12 * t20 - t10 * t22) + t02 * (t10 * t21 - t11 * t20);
	}
	
	public static Matrix4x4f invert(Matrix4x4f src) {
		float det = src.determinant();
		
		if (det == 0)
			return null;
		Matrix4x4f m = new Matrix4x4f();
		float idet = 1f / det;
		
		m.m00 = idet * determinant3x3(src.m11, src.m12, src.m13, src.m21, src.m22, src.m23, src.m31, src.m32, src.m33);
		m.m11 = idet * determinant3x3(src.m00, src.m02, src.m03, src.m20, src.m22, src.m23, src.m30, src.m32, src.m33);
		m.m22 = idet * determinant3x3(src.m00, src.m01, src.m03, src.m10, src.m11, src.m13, src.m30, src.m31, src.m33);
		m.m33 = idet * determinant3x3(src.m00, src.m01, src.m02, src.m10, src.m11, src.m12, src.m20, src.m21, src.m22);
		m.m01 = idet * -determinant3x3(src.m01, src.m02, src.m03, src.m21, src.m22, src.m23, src.m31, src.m32, src.m33);
		m.m10 = idet * -determinant3x3(src.m10, src.m12, src.m13, src.m20, src.m22, src.m23, src.m30, src.m32, src.m33);
		m.m20 = idet * determinant3x3(src.m10, src.m11, src.m13, src.m20, src.m21, src.m23, src.m30, src.m31, src.m33);
		m.m02 = idet * determinant3x3(src.m01, src.m02, src.m03, src.m11, src.m12, src.m13, src.m31, src.m32, src.m33);
		m.m12 = idet * -determinant3x3(src.m00, src.m02, src.m03, src.m10, src.m12, src.m13, src.m30, src.m32, src.m33);
		m.m21 = idet * -determinant3x3(src.m00, src.m01, src.m03, src.m20, src.m21, src.m23, src.m30, src.m31, src.m33);
		m.m03 = idet * -determinant3x3(src.m01, src.m02, src.m03, src.m11, src.m12, src.m13, src.m21, src.m22, src.m23);
		m.m30 = idet * -determinant3x3(src.m10, src.m11, src.m12, src.m20, src.m21, src.m22, src.m30, src.m31, src.m32);
		m.m13 = idet * determinant3x3(src.m00, src.m02, src.m03, src.m10, src.m12, src.m13, src.m20, src.m22, src.m23);
		m.m31 = idet * determinant3x3(src.m00, src.m01, src.m02, src.m20, src.m21, src.m22, src.m30, src.m31, src.m32);
		m.m32 = idet * -determinant3x3(src.m00, src.m01, src.m02, src.m10, src.m11, src.m12, src.m30, src.m31, src.m32);
		m.m23 = idet * -determinant3x3(src.m00, src.m01, src.m03, src.m10, src.m11, src.m13, src.m20, src.m21, src.m23);
		return m;
	}
}
