package globj.objects.textures.values;


import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.jdt.annotation.Nullable;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL14;
import org.lwjgl.opengl.GL44;

import annotations.GLVersion;



@NonNullByDefault
public enum TextureWrapMode {
	CLAMP_EDGE("Clamp to Edge", GL12.GL_CLAMP_TO_EDGE),
	CLAMP_BORDER("Clamp to Border", GL13.GL_CLAMP_TO_BORDER),
	REPEAT("Repeat", GL11.GL_REPEAT),
	MIRRORED_REPEAT("Mirrored Repeat", GL14.GL_MIRRORED_REPEAT),
	@GLVersion({ 4, 4 }) MIRRORED_CLAMP_TO_EDGE("Mirrored Clamp to Edge", GL44.GL_MIRROR_CLAMP_TO_EDGE);
	
	private final String name;
	private final int value;
	
	private TextureWrapMode(String name, int value) {
		this.name = name;
		this.value = value;
	}
	
	/**
	 * @param glInt
	 *            the GLint representing a texture wrap mode
	 * @return the TextureWrapMode object represented by glInt
	 */
	@Nullable
	public static TextureWrapMode get(int glInt) {
		for (TextureWrapMode wrap : values())
			if (wrap.value == glInt)
				return wrap;
		return null;
	}
	
	/**
	 * @return the name of this texture wrap mode
	 */
	public String modeName() {
		return name;
	}
	
	/**
	 * @return the GLint representing this texture wrap mode
	 */
	public int value() {
		return value;
	}
	
	@Override
	public String toString() {
		return name;
	}
	
}
