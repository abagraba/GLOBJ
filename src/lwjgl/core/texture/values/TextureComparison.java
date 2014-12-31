package lwjgl.core.texture;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL30;

public enum TextureComparison {
	
	LEQUAL("Less Than or Equal To", GL30.GL_COMPARE_REF_TO_TEXTURE, GL11.GL_LEQUAL), GEQUAL("Greater Than or Equal To",
			GL30.GL_COMPARE_REF_TO_TEXTURE, GL11.GL_GEQUAL), LESS("Less Than", GL30.GL_COMPARE_REF_TO_TEXTURE,
			GL11.GL_LESS), GREATER("Greater Than", GL30.GL_COMPARE_REF_TO_TEXTURE, GL11.GL_GREATER), EQUAL("Equal To",
			GL30.GL_COMPARE_REF_TO_TEXTURE, GL11.GL_EQUAL), NOTEQUAL("Not Equal To", GL30.GL_COMPARE_REF_TO_TEXTURE,
			GL11.GL_NOTEQUAL), ALWAYS("Always", GL30.GL_COMPARE_REF_TO_TEXTURE, GL11.GL_ALWAYS), NEVER("Never",
			GL30.GL_COMPARE_REF_TO_TEXTURE, GL11.GL_NEVER), NONE("None", GL11.GL_NONE, GL11.GL_NEVER);
	
	public final String name;
	public final int mode;
	public final int func;
	
	private TextureComparison(String name, int mode, int func) {
		this.name = name;
		this.mode = mode;
		this.func = func;
	}
	
	public static TextureComparison get(int i) {
		for (TextureComparison target : values())
			if (target.func == i)
				return target;
		return null;
	}
	
	public String toString() {
		return name;
	}
	
}
