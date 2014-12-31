package lwjgl.core;

import org.lwjgl.opengl.GL15;

public enum VBOTarget {
	ARRAY("Array Buffer", GL15.GL_ARRAY_BUFFER),
	ELEMENT_ARRAY("Element Array Buffer", GL15.GL_ELEMENT_ARRAY_BUFFER),
	;
	
	public final String name;
	public final int value;
	
	private VBOTarget(String name, int value) {
		this.name = name;
		this.value = value;
	}
}
