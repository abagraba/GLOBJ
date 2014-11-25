package lwjgl.core;

public class GLIdentifier {

	private static int vboID = 1;
	private static int texID = 1;
	
	public static int vboID(){
		return vboID++;
	}
	
	public static int texID(){
		return texID++;
	}
	
}
