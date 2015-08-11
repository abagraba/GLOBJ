package globj.camera;


import globj.math.Matrix4x4f;



/**
 * A Scene contains RenderableObjects and can control what is rendered when a Camera performs
 * {@link Camera#renderTo(Scene, RenderTarget)}.
 * 
 */
public interface Scene {
	
	boolean load();
	
	void unload();
	
	void draw(Matrix4x4f viewMatrix, Matrix4x4f projectionMatrix);
	
}
