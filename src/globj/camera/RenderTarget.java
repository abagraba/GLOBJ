package globj.camera;

/**
 * A target which can be rendered to. Can be a FBO with textures or directly to the default framebuffer.
 * @author Abagraba
 */
public interface RenderTarget {
	
	public void clear();

	public boolean prepareRender();

	public void finishRender();
	
}
