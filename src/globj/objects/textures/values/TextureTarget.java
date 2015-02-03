package globj.objects.textures.values;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL30;
import org.lwjgl.opengl.GL31;
import org.lwjgl.opengl.GL40;

public enum TextureTarget {
	TEXTURE_1D("Texture 1D", GL11.GL_TEXTURE_1D), TEXTURE_2D("Texture 2D", GL11.GL_TEXTURE_2D), TEXTURE_3D("Texture 3D", GL12.GL_TEXTURE_3D), TEXTURE_1D_ARRAY(
			"Texture 1D Array", GL30.GL_TEXTURE_1D_ARRAY), TEXTURE_2D_ARRAY("Texture 2D Array", GL30.GL_TEXTURE_2D_ARRAY), TEXTURE_RECTANGLE(
			"Texture Rectangle", GL31.GL_TEXTURE_RECTANGLE), TEXTURE_CUBEMAP("Texture Cubemap", GL13.GL_TEXTURE_CUBE_MAP), TEXTURE_CUBEMAP_ARRAY(
			"Texture Cubemap Array", GL40.GL_TEXTURE_CUBE_MAP_ARRAY), ;
	
	public final String name;
	public final int value;
	
	private TextureTarget(String name, int value) {
		this.name = name;
		this.value = value;
	}
	
	public static TextureTarget get(int i) {
		for (TextureTarget target : values())
			if (target.value == i)
				return target;
		return null;
	}
	
	public String toString() {
		return name;
	}
	
}
