package globj.objects.framebuffers.values;


import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.jdt.annotation.Nullable;
import org.lwjgl.opengl.GL30;



@NonNullByDefault
public enum FBOAttachment {
	
	COLOR0("Color Attachment #0", GL30.GL_COLOR_ATTACHMENT0, 0),
	COLOR1("Color Attachment #1", GL30.GL_COLOR_ATTACHMENT1, 1),
	COLOR2("Color Attachment #2", GL30.GL_COLOR_ATTACHMENT2, 2),
	COLOR3("Color Attachment #3", GL30.GL_COLOR_ATTACHMENT3, 3),
	COLOR4("Color Attachment #4", GL30.GL_COLOR_ATTACHMENT4, 4),
	COLOR5("Color Attachment #5", GL30.GL_COLOR_ATTACHMENT5, 5),
	COLOR6("Color Attachment #6", GL30.GL_COLOR_ATTACHMENT6, 6),
	COLOR7("Color Attachment #7", GL30.GL_COLOR_ATTACHMENT7, 7),
	COLOR8("Color Attachment #8", GL30.GL_COLOR_ATTACHMENT8, 8),
	COLOR9("Color Attachment #9", GL30.GL_COLOR_ATTACHMENT9, 9),
	COLOR10("Color Attachment #10", GL30.GL_COLOR_ATTACHMENT10, 10),
	COLOR11("Color Attachment #11", GL30.GL_COLOR_ATTACHMENT11, 11),
	COLOR12("Color Attachment #12", GL30.GL_COLOR_ATTACHMENT12, 12),
	COLOR13("Color Attachment #13", GL30.GL_COLOR_ATTACHMENT13, 13),
	COLOR14("Color Attachment #14", GL30.GL_COLOR_ATTACHMENT14, 14),
	COLOR15("Color Attachment #15", GL30.GL_COLOR_ATTACHMENT15, 15),
	DEPTH("Depth Attachment", GL30.GL_DEPTH_ATTACHMENT),
	STENCIL("Stencil Attachment", GL30.GL_STENCIL_ATTACHMENT),
	DEPTH_STENCIL("Depth & Stencil Attachment", GL30.GL_DEPTH_STENCIL_ATTACHMENT);
	
	private final String name;
	private final int value;
	private final int colorIndex;
	
	
	private FBOAttachment(String name, int value) {
		this(name, value, -1);
	}
	
	private FBOAttachment(String name, int value, int colorindex) {
		this.name = name;
		this.value = value;
		this.colorIndex = colorindex;
	}
	
	/**
	 * @param glInt
	 *            the GLint representing a FBO attachment
	 * @return the FBOAttachment object represented by glInt
	 */
	@Nullable
	public static FBOAttachment get(int glInt) {
		for (FBOAttachment attachment : values())
			if (attachment.value == glInt)
				return attachment;
		return null;
	}
	
	/**
	 * @return the name of this FBO attachment
	 */
	public String attachmentName() {
		return name;
	}
	
	/**
	 * @return the GLint representing this FBO attachment
	 */
	public int value() {
		return value;
	}
	
	/**
	 * @return the index of the color attachment if it exists. -1 otherwise.
	 */
	public int colorIndex() {
		return colorIndex;
	}
	
	@Override
	public String toString() {
		return name;
	}
	
}
