package lwjgl.core.objects.textures;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;
import org.lwjgl.opengl.GL30;

public enum ImageFormat {
	
	R("R", GL11.GL_RED),
	RG("RG", GL30.GL_RG),
	RGB("RGB", GL11.GL_RGB),
	RGBA("RGBA", GL11.GL_RGBA),
	BGR("BGR", GL12.GL_BGR),
	BGRA("BGRA", GL12.GL_BGRA),
	R_I("R: Integer", GL30.GL_RED_INTEGER),
	RG_I("RG: Integer", GL30.GL_RG_INTEGER),
	RGB_I("RGB: Integer", GL30.GL_RGB_INTEGER),
	RGBA_I("RGBA: Integer", GL30.GL_RGBA_INTEGER),
	BGR_I("BGR: Integer", GL30.GL_BGR_INTEGER),
	BGRA_I("BGRA: Integer", GL30.GL_BGRA_INTEGER),
	
	D("Depth", GL11.GL_DEPTH_COMPONENT),
	S("Stencil", GL11.GL_STENCIL_INDEX),
	DS("Depth & Stencil", GL30.GL_DEPTH_STENCIL);
	
	public final String name;
	public final int value;
	
	private ImageFormat(String name, int value) {
		this.name = name;
		this.value = value;
	}
	
	public static ImageFormat get(int i){
		for (ImageFormat format : values()) 
			if (format.value == i)
				return format;
		return null;
	}
	
	public String toString(){
		return name;
	}
	
}
