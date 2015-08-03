package globj.camera;


import globj.math.Matrix4x4f;
import globj.math.Transform;

import java.awt.Color;

import org.lwjgl.opengl.Display;



public class PerspectiveCamera extends Camera {
	
	private float	near, far;
	private float	ifovtan;
	
	public PerspectiveCamera(Transform transform, Color background, float near, float far, float fov) {
		super(transform, background);
		this.near = near;
		this.far = far;
		setFOV(fov);
	}
	
	public PerspectiveCamera(Color background, float near, float far, float fov) {
		this(new Transform(), background, near, far, fov);
	}
	
	public PerspectiveCamera(Transform transform, float near, float far, float fov) {
		this(transform, new Color(0, 0, 0, 0), near, far, fov);
	}
	
	public PerspectiveCamera(float near, float far, float fov) {
		this(new Color(0, 0, 0, 0), near, far, fov);
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
		//
	}
	
	@Override
	public Matrix4x4f viewMatrix() {
		return transform().getViewMatrix();
	}
	
	@Override
	public Matrix4x4f projectionMatrix() {
		float ar = (float) Display.getWidth() / Display.getHeight();
		float id = near == far ? 0 : (1 / (near - far));
		Matrix4x4f projection = new Matrix4x4f();
		projection.m00 = ifovtan / ar;
		projection.m11 = ifovtan;
		projection.m22 = -(far + near) * id;
		projection.m23 = 1;
		projection.m32 = 2 * far * near * id;
		projection.m33 = 0;
		return projection;
	}
}
