package lwjgl.test.misc;


import globj.core.GL;
import globj.core.RenderCommand;
import globj.objects.framebuffers.RBO;

import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.GL11;

import lwjgl.debug.GLDebug;



public class RBOTests extends RenderCommand {
	
	boolean	r	= true;
	
	
	public static void main(String[] args) {
		GL.setTarget(new RBOTests());
		try {
			GL.startGL();
		}
		catch (LWJGLException e) {
			GLDebug.logException(e);
		}
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
