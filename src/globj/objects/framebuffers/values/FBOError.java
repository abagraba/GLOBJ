package globj.objects.framebuffers.values;


import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.jdt.annotation.Nullable;
import org.lwjgl.opengl.GL30;



@NonNullByDefault
public enum FBOError {
	NONE("None", GL30.GL_FRAMEBUFFER_COMPLETE),
	INCOMPLETE_ATTACHMENT("Incomplete Attachment", GL30.GL_FRAMEBUFFER_INCOMPLETE_ATTACHMENT),
	MISSING_ATTACHMENT("Missing Attachment", GL30.GL_FRAMEBUFFER_INCOMPLETE_MISSING_ATTACHMENT),
	INCOMPLETE_DRAW("Incomplete Draw Buffer", GL30.GL_FRAMEBUFFER_INCOMPLETE_DRAW_BUFFER),
	INCOMPLETE_READ("Incomplete Read Buffer", GL30.GL_FRAMEBUFFER_INCOMPLETE_READ_BUFFER),
	INCOMPLETE_MULTISAMPLE("Incomplete Multisample", GL30.GL_FRAMEBUFFER_INCOMPLETE_MULTISAMPLE),
	UNSUPPORTED("Unsupported Combination", GL30.GL_FRAMEBUFFER_UNSUPPORTED);
	
	private final String name;
	private final int value;
	
	private FBOError(String name, int value) {
		this.name = name;
		this.value = value;
	}
	
	/**
	 * @param glInt
	 *            the GLint representing a FBO error
	 * @return the FBOError object represented by glInt
	 */
	@Nullable
	public static FBOError get(int glInt) {
		for (FBOError error : values())
			if (error.value == glInt)
				return error;
		return null;
	}
	
	/**
	 * @return the name of this FBO error
	 */
	public String errorName() {
		return name;
	}
	
	/**
	 * @return the glInt representing this FBO Error
	 */
	public int value() {
		return value;
	}
	
	@Override
	public String toString() {
		return name;
	}
}
