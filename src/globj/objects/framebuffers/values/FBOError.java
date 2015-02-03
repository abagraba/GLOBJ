package globj.objects.framebuffers.values;

import org.lwjgl.opengl.GL30;

public enum FBOError {
	NONE("None", GL30.GL_FRAMEBUFFER_COMPLETE),
	INCOMPLETE_ATTACHMENT("Incomplete Attachment", GL30.GL_FRAMEBUFFER_INCOMPLETE_ATTACHMENT),
	MISSING_ATTACHMENT("Missing Attachment", GL30.GL_FRAMEBUFFER_INCOMPLETE_MISSING_ATTACHMENT),
	INCOMPLETE_DRAW("Incomplete Draw Buffer", GL30.GL_FRAMEBUFFER_INCOMPLETE_DRAW_BUFFER),
	INCOMPLETE_READ("Incomplete Read Buffer", GL30.GL_FRAMEBUFFER_INCOMPLETE_READ_BUFFER),
	INCOMPLETE_MULTISAMPLE("Incomplete Multisample", GL30.GL_FRAMEBUFFER_INCOMPLETE_MULTISAMPLE),
	UNSUPPORTED("Unsupported Combination", GL30.GL_FRAMEBUFFER_UNSUPPORTED);
	
	public final String name;
	public final int value;
	
	private FBOError(String name, int value) {
		this.name = name;
		this.value = value;
	}
	
	/**
	 * GL screen buffers backleft back right and so on multisample
	 */
	
	public static FBOError get(int i) {
		for (FBOError error : values())
			if (error.value == i)
				return error;
		return null;
	}
	
	public String toString() {
		return name;
	}
}
