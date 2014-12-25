package lwjgl.test.misc;

import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.GL11;

import lwjgl.core.GL;
import lwjgl.core.RBO;
import lwjgl.core.RenderTarget;

public class RBOTests extends RenderTarget {

	boolean r = true;

	public static void main(String[] args) {
		GL.setTarget(new RBOTests());
		try {
			GL.startGL();
		} catch (LWJGLException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void init() {
		// TODO Auto-generated method stub

	}

	@Override
	public void uninit() {
		// TODO Auto-generated method stub

	}

	@Override
	public void input() {
		// TODO Auto-generated method stub

	}

	@Override
	public void render() {
		if (r) {
			System.out.println(RBO.create("Test RBO", 100, 800, GL11.GL_DEPTH_COMPONENT)
					.status());
			System.out.println();
			System.out.println(RBO.create("Rocks", 1000, 800, GL11.GL_RGBA)
					.status());
			System.out.println();
			System.out.println(RBO.create("Coconut", 18000, 800, GL11.GL_RGBA));
			System.out.println();
			System.out.println(RBO.create("Test RBO", 100, 800, GL11.GL_DEPTH_COMPONENT));
		}
		r = false;
	}

}
