package globj.objects.bufferobjects.values;


import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.jdt.annotation.Nullable;
import org.lwjgl.opengl.GL15;



@NonNullByDefault
public enum VBOUsage {
	DYNAMIC_DRAW("Dynamic Draw", GL15.GL_DYNAMIC_DRAW),
	DYNAMIC_READ("Dynamic Read", GL15.GL_DYNAMIC_READ),
	DYNAMIC_COPY("Dynamic Copy", GL15.GL_DYNAMIC_COPY),
	STATIC_DRAW("Static Draw", GL15.GL_STATIC_DRAW),
	STATIC_READ("Static Read", GL15.GL_STATIC_READ),
	STATIC_COPY("Static Copy", GL15.GL_STATIC_COPY),
	STREAM_DRAW("Stream Draw", GL15.GL_STREAM_DRAW),
	STREAM_READ("Stream Read", GL15.GL_STREAM_READ),
	STREAM_COPY("Stream Copy", GL15.GL_STREAM_COPY);
	
	private final String name;
	private final int value;
	
	private VBOUsage(String name, int value) {
		this.name = name;
		this.value = value;
	}
	
	/**
	 * @param glInt
	 *            the GLint representing a VBO usage type
	 * @return the VBOUsage object represented by glInt
	 */
	@Nullable
	public static VBOUsage get(int glInt) {
		for (VBOUsage hint : values())
			if (hint.value == glInt)
				return hint;
		return null;
	}
	
	/**
	 * @return the name of this VBO usage type
	 */
	public String usageName() {
		return name;
	}
	
	/**
	 * @return the GLint representing this VBO usage type
	 */
	public int value() {
		return value;
	}
	
	@Override
	public String toString() {
		return name;
	}
}
