package lwjgl.core.texture.values;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL30;
import org.lwjgl.opengl.GL31;

public enum Texture2DTarget {
	
	TEXTURE_2D("2D Texture", GL11.GL_TEXTURE_2D, GL11.GL_PROXY_TEXTURE_2D), ARRAY_1D("1D Texture Array", GL30.GL_TEXTURE_1D_ARRAY,
			GL30.GL_PROXY_TEXTURE_1D_ARRAY), RECTANGLE("Rectangle Texture", GL31.GL_TEXTURE_RECTANGLE, GL31.GL_PROXY_TEXTURE_RECTANGLE);
	
	public final String name;
	public final int value;
	public final int proxy;
	
	private Texture2DTarget(String name, int value, int proxy) {
		this.name = name;
		this.value = value;
		this.proxy = proxy;
	}
	
	public static Texture2DTarget get(int i) {
		for (Texture2DTarget target : values())
			if (target.value == i)
				return target;
		return null;
	}
	
	public String toString() {
		return name;
	}
	
}
