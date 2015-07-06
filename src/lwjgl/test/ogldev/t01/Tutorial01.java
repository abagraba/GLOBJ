package lwjgl.test.ogldev.t01;

import globj.core.GL;
import globj.core.RenderCommand;

import org.lwjgl.LWJGLException;
import org.lwjgl.input.Keyboard;

public class Tutorial01 extends RenderCommand {
	
	@Override
	public void init() {
	}
	
	@Override
	public void uninit() {
	}
	
	@Override
	public void render() {
	}
	
	public static void main(String[] args) {
		GL.setTarget(new Tutorial01());
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
