package globj.core;

import globj.camera.Camera;
import globj.camera.RenderTarget;
import globj.camera.Scene;

public abstract class SceneCommand extends RenderCommand implements Scene {

	protected Camera camera;
	private RenderTarget target;
	
	public SceneCommand(Camera camera, RenderTarget target){
		this.camera = camera;
		this.target = target;
	}
	/**
	 * Initialization of OpenGL states should happen here. This will be run
	 * right before the first time this renders.
	 */
	public void init(){
		load();
	}
	
	/**
	 * Uninitialization of OpenGL states should happen here. This will be run
	 * right after the last time this renders.
	 */
	public void uninit(){
		unload();
	}

	/**
	 * Input should be handled here. This will occur immediately before a render call.
	 */
	public abstract void input();

	//TODO Handle actions before render. Interruptible actions to maintain framerate? Minimum work done.
	/**
	 * Rendering code should be put here. This will be run every frame.
	 */
	public void render(){
		camera.renderTo(target, this, true);
	}

}
