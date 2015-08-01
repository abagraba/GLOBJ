package globj.camera;


import globj.math.Matrix4x4f;
import globj.math.Transform;
import globj.math.UnitQuaternion;
import globj.math.Vector3f;
import globj.math.Vector4f;

import java.awt.Color;

import org.lwjgl.util.vector.Matrix4f;



public class OrthographicCamera extends Camera {
	
	private float near, far, w, h;
	
	public OrthographicCamera(Transform transform, Color background, float w, float h, float near, float far) {
		super(transform, background);
		this.w = w;
		this.h = h;
		this.near = near;
		this.far = far;
	}
	
	public OrthographicCamera(Color background, float w, float h, float near, float far) {
		this(new Transform(new Vector3f(0, 0, -10f), new UnitQuaternion(0, 0, 0, 1)), background, w, h, near, far);
	}
	
	public OrthographicCamera(Transform transform, float w, float h, float near, float far) {
		this(transform, new Color(0, 0, 0, 0), w, h, near, far);
	}
	
	public OrthographicCamera(float w, float h, float near, float far) {
		this(new Color(0, 0, 0, 0), w, h, near, far);
	}
	
	@Override
	public boolean prepareRender() {
		return true;
	}
	
	@Override
	public void finishRender() {
	
	}
	
	@Override
	public Matrix4x4f viewMatrix() {
		return transform().getViewMatrix();
	}
	
	@Override
	public Matrix4x4f projectionMatrix() {
		float id = near == far ? 0 : (1 / (near - far));
		float w2 = 2 / w;
		float h2 = 2 / h;
		float d2 = 2 * id;
		float r = (far + near) * id;
		Matrix4x4f projection = new Matrix4x4f();
		projection.m00 = w2;
		projection.m11 = h2;
		projection.m22 = d2;
		projection.m30 = -1;
		projection.m31 = -1;
		projection.m32 = r;
		projection.m33 = 1;
		return projection;
	}
	
}
