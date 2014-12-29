package lwjgl.core.texture;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL43;

public enum DepthStencilMode {
	DEPTH("Depth Mode", GL11.GL_DEPTH_COMPONENT),
	STENCIL("Stencil Mode", GL43.GL_STENCIL_COMPONENTS);
	
	public final String name;
	public final int value;
	
	private DepthStencilMode(String name, int value) {
		this.name = name;
		this.value = value;
	}

	public static DepthStencilMode get(int i){
		for (DepthStencilMode mode : values()) 
			if (mode.value == i)
				return mode;
		return null;
	}
	
	public String toString() {
		return name;
	}
	
}
