package globj.objects.textures.values;


import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.jdt.annotation.Nullable;
import org.lwjgl.opengl.GL11;



@NonNullByDefault
public enum MinifyFilter {
	
	NEAREST("Nearest Filter", GL11.GL_NEAREST, false),
	LINEAR("Linear Filter", GL11.GL_LINEAR, false),
	NEAREST_MIPMAP_NEAREST("Nearest Filter / Nearest Mipmap Filter", GL11.GL_NEAREST_MIPMAP_NEAREST, true),
	NEAREST_MIPMAP_LINEAR("Nearest Filter / Linear Mipmap Filter", GL11.GL_NEAREST_MIPMAP_LINEAR, true),
	LINEAR_MIPMAP_NEAREST("Linear Filter / Nearest Mipmap Filter", GL11.GL_LINEAR_MIPMAP_NEAREST, true),
	LINEAR_MIPMAP_LINEAR("Linear Filter / Linear Mipmap Filter", GL11.GL_LINEAR_MIPMAP_LINEAR, true);
	
	private final String name;
	private final int value;
	private final boolean mipmaps;
	
	
	private MinifyFilter(String name, int value, boolean mipmaps) {
		this.name = name;
		this.value = value;
		this.mipmaps = mipmaps;
	}
	
	/**
	 * @param glInt
	 *            the GLint representing a minification filter
	 * @return the MinifyFilter object represented by glInt
	 */
	@Nullable
	public static MinifyFilter get(int glInt) {
		for (MinifyFilter filter : values())
			if (filter.value == glInt)
				return filter;
		return null;
	}
	
	/**
	 * @return the name of this minification filter
	 */
	public String filterName() {
		return name;
	}
	
	/**
	 * @return the GLint representing this minification filter
	 */
	public int value() {
		return value;
	}
	
	/**
	 * @return whether this minification filter supports mipmaps
	 */
	public boolean mipmaps() {
		return mipmaps;
	}
	
	@Override
	public String toString() {
		return name;
	}
	
}
