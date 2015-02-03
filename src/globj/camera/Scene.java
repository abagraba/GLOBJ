package globj.camera;

import org.lwjgl.util.vector.Matrix4f;

/**
 * A Scene contains RenderableObjects and can control what is rendered when a Camera performs {@link Camera#renderTo(Scene, RenderTarget)}.
 * @author Abagraba
 *
 */
public interface Scene {

	boolean load();

	void unload();

	void draw(Matrix4f viewMatrix, Matrix4f projectionMatrix);
	
}
