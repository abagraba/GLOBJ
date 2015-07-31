package globj.objects.textures.values;


import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.jdt.annotation.Nullable;
import org.lwjgl.opengl.GL13;



@NonNullByDefault
public enum CubemapTarget {
	X_POSITIVE("Positive X", GL13.GL_TEXTURE_CUBE_MAP_POSITIVE_X, 0),
	X_NEGATIVE("Negative X", GL13.GL_TEXTURE_CUBE_MAP_NEGATIVE_X, 1),
	Y_POSITIVE("Positive Y", GL13.GL_TEXTURE_CUBE_MAP_POSITIVE_Y, 2),
	Y_NEGATIVE("Negative Y", GL13.GL_TEXTURE_CUBE_MAP_NEGATIVE_Y, 3),
	Z_POSITIVE("Positive Z", GL13.GL_TEXTURE_CUBE_MAP_POSITIVE_Z, 4),
	Z_NEGATIVE("Negative Z", GL13.GL_TEXTURE_CUBE_MAP_NEGATIVE_Z, 5);
	
	private final String name;
	private final int value;
	private final int layer;
	
	
	private CubemapTarget(String name, int value, int layer) {
		this.name = name;
		this.value = value;
		this.layer = layer;
	}
	
	/**
	 * @param glInt
	 *            the GLint representing a cubemap target
	 * @return the CubemapTarget object represented by glInt
	 */
	@Nullable
	public static CubemapTarget get(int glInt) {
		for (CubemapTarget target : values())
			if (target.value == glInt)
				return target;
		return null;
	}
	
	/**
	 * @return the name of this cubemap target
	 */
	public String targetName() {
		return name;
	}
	
	/**
	 * @return the GLint representing this cubemap target
	 */
	public int value() {
		return value;
	}
	
	/**
	 * @return the layer index of this cubemap target
	 */
	public int layer() {
		return layer;
	}
	
	@Override
	public String toString() {
		return name;
	}
}
