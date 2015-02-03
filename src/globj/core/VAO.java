package globj.core;

import org.lwjgl.opengl.GL30;

public class VAO {

	private int vao = 0;

	public VAO() {
	}

	public void use() {
		if (vao == 0)
			vao = GL30.glGenVertexArrays();
		GL30.glBindVertexArray(vao);
	}

	public static void useNone() {
		GL30.glBindVertexArray(0);
	}
	
	public void delete() {
		GL30.glDeleteVertexArrays(vao);
	}

	public boolean exists() {
		return vao != 0;
	}

}