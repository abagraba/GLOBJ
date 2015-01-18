package lwjgl.core.objects.textures.values;

import org.lwjgl.opengl.GL11;

public enum Swizzle {

	R("Red", GL11.GL_RED),
	G("Green", GL11.GL_GREEN),
	B("Blue", GL11.GL_BLUE),
	A("Alpha", GL11.GL_ALPHA),
	ZERO("Zero", GL11.GL_ZERO),
	ONE("One", GL11.GL_ONE);
	
	public final String name;
	public final int value;
	
	private Swizzle(String name, int value) {
		this.name = name;
		this.value = value;
	}
	
	public static Swizzle get(int i){
		for (Swizzle swizzle : values()) 
			if (swizzle.value == i)
				return swizzle;
		return null;
	}

	public String toString(){
		return name;
	}
	
}
