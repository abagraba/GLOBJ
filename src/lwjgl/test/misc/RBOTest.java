package lwjgl.test.misc;


import org.lwjgl.opengl.GL11;

import globj.core.RenderCommand;
import globj.core.Window;
import globj.objects.framebuffers.RBO;
import lwjgl.debug.GLDebug;



public class RBOTest extends RenderCommand {
	
	boolean r = true;
	
	public static void main(String[] args) {
		Window w = new Window();
		w.setTarget(new RBOTest());
		w.start();
	}
	
	@Override
	public void init() {
		//
	}
	
	@Override
	public void uninit() {
		//
	}
	
	@Override
	public void input() {
		//
	}
	
	@Override
	public void render() {
		if (r) {
			GLDebug.debug(RBO.create("Test RBO", 100, 800, GL11.GL_DEPTH_COMPONENT));
			GLDebug.debug(RBO.create("Rocks", 1000, 800, GL11.GL_RGBA));
			GLDebug.debug(RBO.create("Coconut", 18000, 800, GL11.GL_RGBA));
			GLDebug.debug(RBO.create("Test RBO", 100, 800, GL11.GL_DEPTH_COMPONENT));
		}
		r = false;
	}
	
}
