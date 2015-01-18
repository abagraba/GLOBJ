package lwjgl.test.ogldev;

import org.lwjgl.LWJGLException;
import org.lwjgl.input.Keyboard;

import lwjgl.core.GL;
import lwjgl.core.RenderCommand;
import lwjgl.core.objects.framebuffers.FBO;
import lwjgl.debug.Logging;

public class Tutorial2 extends RenderCommand {
	
	@Override
	public void init() {
		Logging.logInfo(FBO.constants());
	}
	
	@Override
	public void uninit() {
	}
	
	@Override
	public void render() {
	}
	
	public static void main(String[] args) {
		GL.setTarget(new Tutorial2());
		try {
			GL.startGL();
		} catch (LWJGLException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void input() {
		while (Keyboard.next()) {
			switch (Keyboard.getEventKey()) {
				case Keyboard.KEY_ESCAPE:
					GL.close();
					break;
				case Keyboard.KEY_F11:
					if (!Keyboard.getEventKeyState())
						GL.toggleFS();
					break;
			}
		}
	}
	
}
