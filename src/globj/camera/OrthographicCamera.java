package globj.camera;

import globj.core.V4f;
import globj.core.utils.Transform;
import globj.core.utils.UnitQuaternion;
import globj.core.utils.V3f;

import org.lwjgl.util.vector.Matrix4f;

public class OrthographicCamera extends Camera {
	
	private float near, far, w, h;
	
	public OrthographicCamera(Transform transform, V4f background, float w, float h, float near, float far) {
		super(transform, background);
		this.w = w;
		this.h = h;
		this.near = near;
		this.far = far;
	}
	
	public OrthographicCamera(V4f background, float w, float h, float near, float far) {
		this(new Transform(new V3f(0, 0, -10f), new UnitQuaternion(0, 0, 0, 1)), background, w, h, near, far);
	}
	
	public OrthographicCamera(Transform transform, float w, float h, float near, float far) {
		this(transform, new V4f(), w, h, near, far);
	}
	
	public OrthographicCamera(float w, float h, float near, float far) {
		this(new V4f(), w, h, near, far);
	}
	
	@Override
	public boolean prepareRender() {
		return true;
	}
	
	@Override
	public void finishRender() {
		
	}
	
	@Override
	public Matrix4f viewMatrix() {
		return transform.getViewMatrix();
	}
	
	@Override
	public Matrix4f projectionMatrix() {
		float id = near == far ? 0 : (1 / (near - far));
		float w2 = 2 / w;
		float h2 = 2 / h;
		float d2 = 2 * id;
		float r = (far + near) * id;
		Matrix4f projection = new Matrix4f();
		projection.m00 = w2;
		projection.m11 = h2;
		projection.m22 = d2;
		projection.m03 = -1;
		projection.m13 = -1;
		projection.m23 = r;
		projection.m33 = 1;
		return projection;
	}
	
}
