package lwjgl.core.texture;

import org.lwjgl.opengl.GL11;

public enum MinifyFilter {

	NEAREST("Nearest Filter", GL11.GL_NEAREST),
	LINEAR("Linear Filter", GL11.GL_LINEAR),
	NEAREST_MIPMAP_NEAREST("Nearest Filter / Nearest Mipmap Filter", GL11.GL_NEAREST_MIPMAP_NEAREST),
	NEAREST_MIPMAP_LINEAR("Nearest Filter / Linear Mipmap Filter", GL11.GL_NEAREST_MIPMAP_LINEAR),
	LINEAR_MIPMAP_NEAREST("Linear Filter / Nearest Mipmap Filter", GL11.GL_LINEAR_MIPMAP_NEAREST),
	LINEAR_MIPMAP_LINEAR("Linear Filter / Linear Mipmap Filter", GL11.GL_LINEAR_MIPMAP_LINEAR);
	
	public final String name;
	public final int value;
	
	private MinifyFilter(String name, int value) {
		this.name = name;
		this.value = value;
	}
	
	public static MinifyFilter get(int i){
		for (MinifyFilter filter : values()) 
			if (filter.value == i)
				return filter;
		return null;
	}

	public String toString(){
		return name;
	}
	
}
