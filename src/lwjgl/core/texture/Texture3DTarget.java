package lwjgl.core.texture;

import org.lwjgl.opengl.GL12;
import org.lwjgl.opengl.GL30;

public enum Texture3DTarget {
	
	TEXTURE_3D("3D Texture", GL12.GL_TEXTURE_3D),
	ARRAY_2D("2D Texture Array", GL30.GL_TEXTURE_2D_ARRAY);
	
	public final String name;
	public final int value;
	
	private Texture3DTarget(String name, int value) {
		this.name = name;
		this.value = value;
	}
	
	public static Texture3DTarget get(int i){
		for (Texture3DTarget target : values()) 
			if (target.value == i)
				return target;
		return null;
	}
	
	public String toString(){
		return name;
	}
	
}
