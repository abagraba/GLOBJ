package lwjgl.test.ogldev;


import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;

import control.ControlManager;
import globj.core.DataType;
import globj.core.RenderCommand;
import globj.core.Window;
import globj.objects.arrays.VAO;
import globj.objects.arrays.VBOFormat;
import globj.objects.bufferobjects.StaticVBO;
import globj.objects.bufferobjects.VBO;
import globj.objects.bufferobjects.values.VBOTarget;



public class Tutorial03 extends RenderCommand {
	
	private VBO vbo;
	
	@Override
	public void init() {
		float[] vertices = new float[] { -1, -1, 0, 1, -1, 0, 0, 1, 0 };
		
		vbo = StaticVBO.create("Test VBO", VBOTarget.ARRAY, vertices);
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
		GL11.glDrawArrays(GL11.GL_TRIANGLES, 0, 3);
		GL20.glDisableVertexAttribArray(0);
	}
	
	public static void main(String[] args) {
		Window w = new Window();
		w.setTarget(new Tutorial03());
		w.start();
		ControlManager.attach(w, new TutorialControlSet());
		
	}
	
	@Override
	public void input() {
		if (TutorialControlSet.FULLSCR.state())
			Window.toggleFS();
		if (TutorialControlSet.ESC.state())
			Window.close();
	}
	
}
