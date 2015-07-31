package lwjgl.test.ogldev;


import lwjgl.debug.GLDebug;
import globj.core.DataType;
import globj.core.GL;
import globj.core.RenderCommand;
import globj.objects.arrays.VAO;
import globj.objects.arrays.VBOFormat;
import globj.objects.bufferobjects.StaticVBO;
import globj.objects.bufferobjects.VBO;
import globj.objects.bufferobjects.VBOTarget;

import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;

import control.ControlManager;



public class Tutorial02 extends RenderCommand {
	
	private VBO	vbo;
	
	
	@Override
	public void init() {
		ControlManager.select(new TutorialControlSet());
		
		vbo = StaticVBO.create("Test VBO", VBOTarget.ARRAY, new float[] { 0, 0, 0 });
	}
	
	@Override
	public void uninit() {
		vbo.destroy();
	}
	
	@Override
	public void render() {
		GL11.glClearColor(0, 0, 0, 0);
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT);
		
		VAO.defaultVAO.attachBuffer(0, vbo, new VBOFormat(3, DataType.FLOAT, 0, 0));
		
		GL20.glEnableVertexAttribArray(0);
		GL11.glDrawArrays(GL11.GL_POINTS, 0, 1);
		GL20.glDisableVertexAttribArray(0);
	}
	
	public static void main(String[] args) {
		GL.setTarget(new Tutorial02());
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