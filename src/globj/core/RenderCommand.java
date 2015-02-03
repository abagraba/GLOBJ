package globj.core;

public abstract class RenderCommand {
	
	/**
	 * Initialization of OpenGL states should happen here. This will be run
	 * right before the first time this renders.
	 */
	public abstract void init();
	
	/**
	 * Uninitialization of OpenGL states should happen here. This will be run
	 * right after the last time this renders.
	 */
	public abstract void uninit();
	
	/**
	 * Input should be handled here. This will occur immediately before a render
	 * call.
	 */
	public abstract void input();
	
	// TODO Handle actions before render. Interruptible actions to maintain
	// framerate? Minimum work done.
	/**
	 * Rendering code should be put here. This will be run every frame.
	 */
	public abstract void render();
	
}
