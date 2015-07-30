package lwjgl.test.ogldev;


import lwjgl.debug.GLDebug;
import globj.core.GL;
import globj.core.RenderCommand;

import org.lwjgl.LWJGLException;

import control.ControlManager;



public class Tutorial01 extends RenderCommand {
	
	@Override
	public void init() {
		ControlManager.select(new TutorialControlSet());
	}
	
	@Override
	public void uninit() {
		// No uninitialization necessary.
	}
	
	@Override
	public void render() {
		// Static image.
	}
	
	public static void main(String[] args) {
		GL.setTarget(new Tutorial01());
		try {
			GL.startGL();
		}
		catch (LWJGLException e) {
			GLDebug.logException(e);
		}
	}
	
	@Override
	public void input() {
		if (TutorialControlSet.FULLSCR.state())
			GL.toggleFS();
		if (TutorialControlSet.ESC.state())
			GL.close();
	}
	
}
