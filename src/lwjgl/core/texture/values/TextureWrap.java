package lwjgl.core.texture.values;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL14;
import org.lwjgl.opengl.GL44;

public enum TextureWrap {
	CLAMP_EDGE("Clamp to Edge", GL12.GL_CLAMP_TO_EDGE),
	CLAMP_BORDER("Clamp to Border", GL13.GL_CLAMP_TO_BORDER),
	REPEAT("Repeat", GL11.GL_REPEAT),
	MIRRORED_REPEAT("Mirrored Repeat", GL14.GL_MIRRORED_REPEAT),
	MIRRORED_CLAMP_TO_EDGE("Mirrored Clamp to Edge", GL44.GL_MIRROR_CLAMP_TO_EDGE);
	
	public final String name;
	public final int value;
	
	private TextureWrap(String name, int value) {
		this.name = name;
		this.value = value;
	}

	public static TextureWrap get(int i){
		for (TextureWrap wrap : values()) 
			if (wrap.value == i)
				return wrap;
		return null;
	}
	
	public String toString() {
		return name;
	}
	
}
