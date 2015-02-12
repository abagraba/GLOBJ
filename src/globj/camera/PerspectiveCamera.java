package globj.camera;


import globj.core.V4f;
import globj.core.utils.Transform;
import globj.core.utils.UnitQuaternion;
import globj.core.utils.V3f;

import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Matrix4f;

public class PerspectiveCamera extends Camera {
	
	private float near, far;
	private float ifovtan;
	
	public PerspectiveCamera(Transform transform, V4f background, float near, float far, float fov) {
		super(transform, background);
		this.near = near;
		this.far = far;
		setFOV(fov);
	}
	
	public PerspectiveCamera(V4f background, float near, float far, float fov) {
		this(new Transform(new V3f(0, 0, -10f), new UnitQuaternion(1, 0, 0, 0)), background, near, far, fov);
	}
	
	public PerspectiveCamera(Transform transform, float near, float far, float fov) {
		this(transform, new V4f(), near, far, fov);
	}
	
	public PerspectiveCamera(float near, float far, float fov) {
		this(new V4f(), near, far, fov);
	}
	
	public void setFOV(float fov) {
		ifovtan = (float) Math.tan(Math.toRadians(fov / 2));
		if (ifovtan != 0)
			ifovtan = 1 / ifovtan;
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
		return  transform.getViewMatrix();
	}

	@Override
	public Matrix4f projectionMatrix() {
		float ar = (float) Display.getWidth() / Display.getHeight();
		float id = near == far ? 0 : (1 / (near - far));
		Matrix4f projection = new Matrix4f();
		projection.m00 = ifovtan / ar;
		projection.m11 = ifovtan;
		projection.m22 = -(far + near) * id;
		projection.m32 = 1;
		projection.m23 = 2 * far * near * id;
		return projection;
	}
}
