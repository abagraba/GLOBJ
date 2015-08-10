package globj.objects.textures.values;


import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.jdt.annotation.Nullable;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL30;



@NonNullByDefault
public enum TextureComparison {
	
	LEQUAL("Less Than or Equal To", GL30.GL_COMPARE_REF_TO_TEXTURE, GL11.GL_LEQUAL),
	GEQUAL("Greater Than or Equal To", GL30.GL_COMPARE_REF_TO_TEXTURE, GL11.GL_GEQUAL),
	LESS("Less Than", GL30.GL_COMPARE_REF_TO_TEXTURE, GL11.GL_LESS),
	GREATER("Greater Than", GL30.GL_COMPARE_REF_TO_TEXTURE, GL11.GL_GREATER),
	EQUAL("Equal To", GL30.GL_COMPARE_REF_TO_TEXTURE, GL11.GL_EQUAL),
	NOTEQUAL("Not Equal To", GL30.GL_COMPARE_REF_TO_TEXTURE, GL11.GL_NOTEQUAL),
	ALWAYS("Always", GL30.GL_COMPARE_REF_TO_TEXTURE, GL11.GL_ALWAYS),
	NEVER("Never", GL30.GL_COMPARE_REF_TO_TEXTURE, GL11.GL_NEVER),
	NONE("None", GL11.GL_NONE, GL11.GL_NEVER);
	
	private final String name;
	private final int mode;
	private final int func;
	
	private TextureComparison(String name, int mode, int func) {
		this.name = name;
		this.mode = mode;
		this.func = func;
	}
	
	/**
	 * @param glIntMode
	 *            the GLint representing a texture comparison mode
	 * @param glIntFunc
	 *            the GLint representing a texture comparison mode
	 * @return the TextureComparison object represented by the glInts
	 */
	@Nullable
	public static TextureComparison get(int glIntMode, int glIntFunc) {
		for (TextureComparison target : values())
			if (target.func == glIntFunc && target.mode == glIntMode)
				return target;
		return null;
	}
	
	/**
	 * @return the name of this texture comparison
	 */
	public String comparisonName() {
		return name;
	}
	
	/**
	 * @return the GLint representing the comparison mode of this texture comparison
	 */
	public int mode() {
		return mode;
	}
	
	/**
	 * @return the GLint representing the comparison function of this texture comparison
	 */
	public int func() {
		return func;
	}
	
	@Override
	public String toString() {
		return name;
	}
	
}
