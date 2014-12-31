package lwjgl.core.texture.values;

import org.lwjgl.opengl.GL11;

public enum MagnifyFilter {

	NEAREST("Nearest Filter", GL11.GL_NEAREST),
	LINEAR("Linear Filter", GL11.GL_LINEAR);
	
	public final String name;
	public final int value;
	
	private MagnifyFilter(String name, int value) {
		this.name = name;
		this.value = value;
	}
	
	public static MagnifyFilter get(int i){
		for (MagnifyFilter filter : values()) 
			if (filter.value == i)
				return filter;
		return null;
	}
	
	public String toString(){
		return name;
	}
	
}
