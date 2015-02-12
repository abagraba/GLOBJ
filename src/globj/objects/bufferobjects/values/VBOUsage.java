package globj.objects.bufferobjects.values;

import org.lwjgl.opengl.GL15;

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
	
	public final String name;
	public final int value;
	
	private VBOUsage(String name, int value) {
		this.name = name;
		this.value = value;
	}
	
	public static VBOUsage get(int i) {
		for (VBOUsage hint : values())
			if (hint.value == i)
				return hint;
		return null;
	}
	
	public String toString() {
		return name;
	}
}
