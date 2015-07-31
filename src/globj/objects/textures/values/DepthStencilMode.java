package globj.objects.textures.values;


import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.jdt.annotation.Nullable;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL43;



@NonNullByDefault
public enum DepthStencilMode {
	DEPTH("Depth Mode", GL11.GL_DEPTH_COMPONENT), STENCIL("Stencil Mode", GL43.GL_STENCIL_COMPONENTS);
	
	private final String name;
	private final int value;
	
	
	private DepthStencilMode(String name, int value) {
		this.name = name;
		this.value = value;
	}
	
	/**
	 * @param glInt
	 *            the GLint representing a depth/stencil mode
	 * @return the DepthStencilMode object represented by glInt
	 */
	@Nullable
	public static DepthStencilMode get(int glInt) {
		for (DepthStencilMode mode : values())
			if (mode.value == glInt)
				return mode;
		return null;
	}
	
	/**
	 * @return the name of this depth/stencil mode
	 */
	public String modeName() {
		return name;
	}
	
	/**
	 * @return the GLint representing this depth/stencil mode
	 */
	public int value() {
		return value;
	}
	
	@Override
	public String toString() {
		return name;
	}
	
}
