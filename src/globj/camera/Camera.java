package globj.camera;


import java.awt.Color;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Matrix4f;

import globj.math.Matrix4x4f;
import globj.math.Transform;



@NonNullByDefault
public abstract class Camera {
	
	private Transform	transform;
	private Color		background;
	
	public Camera(Transform transform, Color background) {
		this.transform = transform;
		this.background = background;
	}
	
	public Transform transform() {
		return transform;
	}
	
	public void setTransform(Transform t) {
		this.transform = new Transform(t);
	}
	
	public void setBackground(Color background) {
		this.background = background;
	}
	
	public void renderTo(RenderTarget target, Scene scene, boolean clear) {
		if (!target.prepareRender())
			return;
		if (clear)
			target.clear();
		if (prepareRender()) {
			GL11.glClearColor(background.getRed(), background.getGreen(), background.getBlue(), background.getAlpha());
			scene.draw(viewMatrix(), projectionMatrix());
			finishRender();
			target.finishRender();
		}
	}
	
	public abstract boolean prepareRender();
	
	public abstract void finishRender();
	
	public abstract Matrix4x4f viewMatrix();
	
	public abstract Matrix4x4f projectionMatrix();
	
}
