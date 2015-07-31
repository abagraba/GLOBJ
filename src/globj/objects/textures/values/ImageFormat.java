package globj.objects.textures.values;


import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.jdt.annotation.Nullable;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;
import org.lwjgl.opengl.GL30;



@NonNullByDefault
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
	
	private final String name;
	private final int value;
	
	
	private ImageFormat(String name, int value) {
		this.name = name;
		this.value = value;
	}
	
	/**
	 * @param glInt
	 *            the GLint representing an image format
	 * @return the ImageFormat object represented by glInt
	 */
	@Nullable
	public static ImageFormat get(int glInt) {
		for (ImageFormat format : values())
			if (format.value == glInt)
				return format;
		return null;
	}
	
	/**
	 * @return the name of this image format
	 */
	public String formatName() {
		return name;
	}
	
	/**
	 * @return the GLint representing this image format
	 */
	public int value() {
		return value;
	}
	
	@Override
	public String toString() {
		return name;
	}
	
}
