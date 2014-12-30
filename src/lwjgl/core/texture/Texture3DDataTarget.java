package lwjgl.core.texture;

import org.lwjgl.opengl.GL12;
import org.lwjgl.opengl.GL30;
import org.lwjgl.opengl.GL40;

public enum Texture3DDataTarget {
	
	TEXTURE_3D("3D Texture", GL12.GL_TEXTURE_3D, Texture3DTarget.TEXTURE_3D),
	PROXY_TEXTURE_3D("3D Texture Proxy", GL12.GL_PROXY_TEXTURE_3D,Texture3DTarget.TEXTURE_3D),
	ARRAY_2D("2D Texture Array", GL30.GL_TEXTURE_2D_ARRAY, Texture3DTarget.ARRAY_2D),
	PROXY_ARRAY_2D("2D Texture Array Proxy", GL30.GL_PROXY_TEXTURE_2D_ARRAY, Texture3DTarget.ARRAY_2D),
	PROXY_CUBE_MAP_ARRAY("Cube Map Texture Array Proxy", GL40.GL_PROXY_TEXTURE_CUBE_MAP_ARRAY, Texture3DTarget.ARRAY_CUBE_MAP),
	;
	
	public final String name;
	public final int value;
	public final Texture3DTarget parent;
	
	private Texture3DDataTarget(String name, int value, Texture3DTarget parent) {
		this.name = name;
		this.value = value;
		this.parent = parent;
	}
	
	public static Texture3DDataTarget get(int i){
		for (Texture3DDataTarget target : values()) 
			if (target.value == i)
				return target;
		return null;
	}
	
	public String toString(){
		return name;
	}
	
}
