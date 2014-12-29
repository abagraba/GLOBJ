package lwjgl.core.texture;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL30;
import org.lwjgl.opengl.GL31;

public enum Texture2DDataTarget {
	
	TEXTURE_2D("2D Texture", GL11.GL_TEXTURE_2D, Texture2DTarget.TEXTURE_2D),
	PROXY_TEXTURE_2D("2D Texture Proxy", GL11.GL_PROXY_TEXTURE_2D,Texture2DTarget.TEXTURE_2D),
	ARRAY_1D("1D Texture Array", GL30.GL_TEXTURE_1D_ARRAY, Texture2DTarget.ARRAY_1D),
	PROXY_ARRAY_1D("1D Texture Array Proxy", GL30.GL_PROXY_TEXTURE_1D_ARRAY, Texture2DTarget.ARRAY_1D),
	RECTANGLE("Rectangle Texture", GL31.GL_TEXTURE_RECTANGLE, Texture2DTarget.RECTANGLE),
	PROXY_RECTANGLE("Rectangle Texture Proxy", GL31.GL_PROXY_TEXTURE_RECTANGLE, Texture2DTarget.RECTANGLE),
	CUBE_MAP_XP("Cube Map Texture X+", GL13.GL_TEXTURE_CUBE_MAP_POSITIVE_X, Texture2DTarget.CUBE_MAP),
	CUBE_MAP_XN("Cube Map Texture X-", GL13.GL_TEXTURE_CUBE_MAP_NEGATIVE_X, Texture2DTarget.CUBE_MAP),
	CUBE_MAP_YP("Cube Map Texture Y+", GL13.GL_TEXTURE_CUBE_MAP_POSITIVE_Y, Texture2DTarget.CUBE_MAP),
	CUBE_MAP_YN("Cube Map Texture Y-", GL13.GL_TEXTURE_CUBE_MAP_NEGATIVE_Y, Texture2DTarget.CUBE_MAP),
	CUBE_MAP_ZP("Cube Map Texture Z+", GL13.GL_TEXTURE_CUBE_MAP_POSITIVE_Z, Texture2DTarget.CUBE_MAP),
	CUBE_MAP_ZN("Cube Map Texture Z-", GL13.GL_TEXTURE_CUBE_MAP_NEGATIVE_Z, Texture2DTarget.CUBE_MAP),
	PROXY_CUBE_MAP("Cube Map Texture Proxy", GL13.GL_PROXY_TEXTURE_CUBE_MAP, Texture2DTarget.CUBE_MAP);
	
	public final String name;
	public final int value;
	public final Texture2DTarget parent;
	
	private Texture2DDataTarget(String name, int value, Texture2DTarget parent) {
		this.name = name;
		this.value = value;
		this.parent = parent;
	}
	
	public static Texture2DDataTarget get(int i){
		for (Texture2DDataTarget target : values()) 
			if (target.value == i)
				return target;
		return null;
	}
	
	public String toString(){
		return name;
	}
	
}
