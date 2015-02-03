package globj.objects.bufferobjects.values;

import org.lwjgl.opengl.GL13;

public enum VertexAttribute {
	VERTEX("Positive X", 0);
	
	public final String name;
	
	private VertexAttribute(String name, int index) {
		this.name = name;
	}
	/*
	public static VBOFormat get(int i){
		for (VBOFormat target : values()) 
			if (target.value == i)
				return target;
		return null;
	}
	*/
	public String toString() {
		return name;
	}
}
