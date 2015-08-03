package globj.objects.bufferobjects.values;


import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.jdt.annotation.Nullable;
import org.lwjgl.opengl.GL15;



@NonNullByDefault
public enum VBOTarget {
	ARRAY("Array Buffer", GL15.GL_ARRAY_BUFFER),
	ELEMENT_ARRAY("Element Array Buffer", GL15.GL_ELEMENT_ARRAY_BUFFER);
	
	private final String name;
	private final int value;
	
	private VBOTarget(String name, int value) {
		this.name = name;
		this.value = value;
	}
	
	/**
	 * @param glInt
	 *            the GLint representing a VBO target
	 * @return the VBOTarget object represented by glInt
	 */
	@Nullable
	public static VBOTarget get(int glInt) {
		for (VBOTarget target : values())
			if (target.value == glInt)
				return target;
		return null;
	}
	
	/**
	 * @return the name of this VBO target
	 */
	public String usageName() {
		return name;
	}
	
	/**
	 * @return the GLint representing this VBO target
	 */
	public int value() {
		return value;
	}
	
	@Override
	public String toString() {
		return name;
	}
}
