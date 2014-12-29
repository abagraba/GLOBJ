package lwjgl.core.texture;

import org.lwjgl.opengl.GL11;

public enum Texture1DTarget {
	
	TEXTURE_1D("1D Texture", GL11.GL_TEXTURE_1D);
	
	public final String name;
	public final int value;
	
	private Texture1DTarget(String name, int value) {
		this.name = name;
		this.value = value;
	}
	
	public static Texture1DTarget get(int i){
		for (Texture1DTarget target : values()) 
			if (target.value == i)
				return target;
		return null;
	}
	
	public String toString(){
		return name;
	}
	
}
