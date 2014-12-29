package lwjgl.core.texture;

import org.lwjgl.opengl.GL11;

public enum Texture1DDataTarget {
	
	TEXTURE_1D("1D Texture", GL11.GL_TEXTURE_1D, Texture1DTarget.TEXTURE_1D),
	PROXY_TEXTURE_1D("1D Texture Proxy", GL11.GL_PROXY_TEXTURE_1D,Texture1DTarget.TEXTURE_1D);
	
	public final String name;
	public final int value;
	public final Texture1DTarget parent;
	
	private Texture1DDataTarget(String name, int value, Texture1DTarget parent) {
		this.name = name;
		this.value = value;
		this.parent = parent;
	}
	
	public static Texture1DDataTarget get(int i){
		for (Texture1DDataTarget target : values()) 
			if (target.value == i)
				return target;
		return null;
	}
	
	public String toString(){
		return name;
	}
	
}
