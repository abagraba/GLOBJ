package globj.camera;

import globj.core.V4f;

import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Matrix4f;

import lwjgl.core.utils.Transform;

public abstract class Camera {
	// TODO make protected
	public Transform transform;
	private V4f background;
	
	public Camera(Transform transform, V4f background) {
		this.transform = transform;
		setBackground(background);
	}
	
	public void setTransform(Transform t) {
		this.transform = new Transform(t);
	}
	
	public void setBackground(V4f background) {
		this.background = background != null ? background : new V4f();
	}
	
	public void renderTo(RenderTarget target, Scene scene, boolean clear) {
		if (!target.prepareRender())
			return;
		if (clear)
			target.clear();
		if (prepareRender()) {
			GL11.glClearColor(background.x, background.y, background.z, background.w);
			scene.draw(viewMatrix(), projectionMatrix());
			finishRender();
			target.finishRender();
		}
	}
	
	public abstract boolean prepareRender();
	
	public abstract void finishRender();
	
	public abstract Matrix4f viewMatrix();
	
	public abstract Matrix4f projectionMatrix();
	
}
