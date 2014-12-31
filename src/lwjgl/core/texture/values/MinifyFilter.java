package lwjgl.core.texture.values;

import org.lwjgl.opengl.GL11;

public enum MinifyFilter {

	NEAREST("Nearest Filter", GL11.GL_NEAREST, false),
	LINEAR("Linear Filter", GL11.GL_LINEAR, false),
	NEAREST_MIPMAP_NEAREST("Nearest Filter / Nearest Mipmap Filter", GL11.GL_NEAREST_MIPMAP_NEAREST, true),
	NEAREST_MIPMAP_LINEAR("Nearest Filter / Linear Mipmap Filter", GL11.GL_NEAREST_MIPMAP_LINEAR, true),
	LINEAR_MIPMAP_NEAREST("Linear Filter / Nearest Mipmap Filter", GL11.GL_LINEAR_MIPMAP_NEAREST, true),
	LINEAR_MIPMAP_LINEAR("Linear Filter / Linear Mipmap Filter", GL11.GL_LINEAR_MIPMAP_LINEAR, true);
	
	public final String name;
	public final int value;
	public final boolean mipmaps;
	
	private MinifyFilter(String name, int value, boolean mipmaps) {
		this.name = name;
		this.value = value;
		this.mipmaps = mipmaps;
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
