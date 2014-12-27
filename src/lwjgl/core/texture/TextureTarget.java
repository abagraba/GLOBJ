package lwjgl.core.texture;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL30;
import org.lwjgl.opengl.GL31;
import org.lwjgl.opengl.GL32;
import org.lwjgl.opengl.GL40;

public enum TextureTarget {
	
	TEXTURE_1D("1D Texture", GL11.GL_TEXTURE_1D),
	TEXTURE_2D("2D Texture", GL11.GL_TEXTURE_2D),
	TEXTURE_3D("3D Texture", GL12.GL_TEXTURE_3D),
	ARRAY_1D("1D Texture Array", GL30.GL_TEXTURE_1D_ARRAY),
	ARRAY_2D("2D Texture Array", GL30.GL_TEXTURE_2D_ARRAY),
	RECTANGLE("Rectangle Texture", GL31.GL_TEXTURE_RECTANGLE),
	CUBE_MAP("Cube Map Texture", GL13.GL_TEXTURE_CUBE_MAP),
	ARRAY_CUBE_MAP("Cube Map Texture Array", GL40.GL_TEXTURE_CUBE_MAP_ARRAY),
	BUFFER("Buffer Texture", GL31.GL_TEXTURE_BUFFER),
	MULTISAMPLE_2D("2D Multisample Texture", GL32.GL_TEXTURE_2D_MULTISAMPLE),
	ARRAY_MULTISAMPLE_2D("2D Multisample Texture Array", GL32.GL_TEXTURE_2D_MULTISAMPLE_ARRAY);
	
	public final String name;
	public final int value;
	
	private TextureTarget(String name, int value) {
		this.name = name;
		this.value = value;
	}
	
	public static TextureTarget get(int i){
		for (TextureTarget target : values()) 
			if (target.value == i)
				return target;
		return null;
	}
	
	public String toString(){
		return name;
	}
	
}
