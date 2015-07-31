package globj.objects.textures.values;


import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.jdt.annotation.Nullable;
import org.lwjgl.opengl.GL11;



@NonNullByDefault
public enum MagnifyFilter {
	
	NEAREST("Nearest Filter", GL11.GL_NEAREST), LINEAR("Linear Filter", GL11.GL_LINEAR);
	
	private final String name;
	private final int value;
	
	
	private MagnifyFilter(String name, int value) {
		this.name = name;
		this.value = value;
	}
	
	/**
	 * @param glInt
	 *            the GLint representing a magnification filter
	 * @return the MagnifyFilter object represented by glInt
	 */
	@Nullable
	public static MagnifyFilter get(int i) {
		for (MagnifyFilter filter : values())
			if (filter.value == i)
				return filter;
		return null;
	}
	
	/**
	 * @return the name of this magnification filter
	 */
	public String filterName() {
		return name;
	}
	
	/**
	 * @return the GLint representing this magnification filter
	 */
	public int value() {
		return value;
	}
	
	@Override
	public String toString() {
		return name;
	}
	
}
