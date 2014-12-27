package lwjgl.core.texture;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL30;
import org.lwjgl.opengl.GL31;
import org.lwjgl.opengl.GL32;
import org.lwjgl.opengl.GL40;

public enum Texture2DTarget {
	
	TEXTURE_2D("2D Texture", GL11.GL_TEXTURE_2D),
	ARRAY_1D("1D Texture Array", GL30.GL_TEXTURE_1D_ARRAY),
	RECTANGLE("Rectangle Texture", GL31.GL_TEXTURE_RECTANGLE),
	CUBE_MAP_XP("Cube Map Texture X+", GL13.GL_TEXTURE_CUBE_MAP_POSITIVE_X),
	CUBE_MAP_XN("Cube Map Texture X-", GL13.GL_TEXTURE_CUBE_MAP_NEGATIVE_X),
	CUBE_MAP_YP("Cube Map Texture Y+", GL13.GL_TEXTURE_CUBE_MAP_POSITIVE_Y),
	CUBE_MAP_YN("Cube Map Texture Y-", GL13.GL_TEXTURE_CUBE_MAP_NEGATIVE_Y),
	CUBE_MAP_ZP("Cube Map Texture Z+", GL13.GL_TEXTURE_CUBE_MAP_POSITIVE_Z),
	CUBE_MAP_ZN("Cube Map Texture Z-", GL13.GL_TEXTURE_CUBE_MAP_NEGATIVE_Z);
	
	public final String name;
	public final int value;
	
	private Texture2DTarget(String name, int value) {
		this.name = name;
		this.value = value;
	}
	
	public static Texture2DTarget get(int i){
		for (Texture2DTarget target : values()) 
			if (target.value == i)
				return target;
		return null;
	}
	
	public String toString(){
		return name;
	}
	
}
