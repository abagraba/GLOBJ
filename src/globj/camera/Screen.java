package globj.camera;


import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL30;

public class Screen implements RenderTarget {
	
	public static final Screen left = new Screen(GL11.GL_BACK_LEFT);
	public static final Screen right = new Screen(GL11.GL_BACK_RIGHT);
	
	private final int target;
	
	private Screen(int target) {
		this.target = target;
	}
	
	@Override
	public void clear() {
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
	}
	
	@Override
	public boolean prepareRender() {
		GL30.glBindFramebuffer(GL30.GL_DRAW_FRAMEBUFFER, 0);
		GL11.glDrawBuffer(target);
		return true;
	}
	
	@Override
	public void finishRender() {
		
	}
	
}
