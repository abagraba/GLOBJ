package globj.objects.textures.values;


import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.jdt.annotation.Nullable;
import org.lwjgl.opengl.GL11;

import annotations.GLVersion;



@GLVersion({ 3, 3 })
@NonNullByDefault
public enum Swizzle {
	
	@GLVersion({ 3, 3 }) R("Red", GL11.GL_RED),
	@GLVersion({ 3, 3 }) G("Green", GL11.GL_GREEN),
	@GLVersion({ 3, 3 }) B("Blue", GL11.GL_BLUE),
	@GLVersion({ 3, 3 }) A("Alpha", GL11.GL_ALPHA),
	@GLVersion({ 3, 3 }) ZERO("Zero", GL11.GL_ZERO),
	@GLVersion({ 3, 3 }) ONE("One", GL11.GL_ONE);
	
	private final String name;
	private final int value;
	
	private Swizzle(String name, int value) {
		this.name = name;
		this.value = value;
	}
	
	/**
	 * @param glInt
	 *            the GLint representing a shader type
	 * @return the ShaderType object represented by glInt
	 */
	@Nullable
	public static Swizzle get(int glInt) {
		for (Swizzle swizzle : values())
			if (swizzle.value == glInt)
				return swizzle;
		return null;
	}
	
	/**
	 * @return the name of this swizzle
	 */
	public String swizzleName() {
		return name;
	}
	
	/**
	 * @return the GLint representing this swizzle
	 */
	public int value() {
		return value;
	}
	
	@Override
	public String toString() {
		return name;
	}
	
}
