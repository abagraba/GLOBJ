package globj.objects.textures.values;

import org.lwjgl.opengl.GL13;

public enum CubemapTarget {
	X_POSITIVE("Positive X", GL13.GL_TEXTURE_CUBE_MAP_POSITIVE_X, 0),
	X_NEGATIVE("Negative X", GL13.GL_TEXTURE_CUBE_MAP_NEGATIVE_X, 1),
	Y_POSITIVE("Positive Y", GL13.GL_TEXTURE_CUBE_MAP_POSITIVE_Y, 2),
	Y_NEGATIVE("Negative Y", GL13.GL_TEXTURE_CUBE_MAP_NEGATIVE_Y, 3),
	Z_POSITIVE("Positive Z", GL13.GL_TEXTURE_CUBE_MAP_POSITIVE_Z, 4),
	Z_NEGATIVE("Negative Z", GL13.GL_TEXTURE_CUBE_MAP_NEGATIVE_Z, 5);
	
	public final int value;
	public final int layer;
	public final String name;
	
	private CubemapTarget(String name, int value, int layer) {
		this.name = name;
		this.value = value;
		this.layer = layer;
	}
	
	public static CubemapTarget get(int i){
		for (CubemapTarget target : values()) 
			if (target.value == i)
				return target;
		return null;
	}
	
	@Override
	public String toString() {
		return name;
	}
}
