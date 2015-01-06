package lwjgl.core.framebuffer.values;

import org.lwjgl.opengl.GL30;

public enum FBOAttachment {
	
	COLOR0("Color Attachment #0", GL30.GL_COLOR_ATTACHMENT0, 0), COLOR1("Color Attachment #1", GL30.GL_COLOR_ATTACHMENT1, 1), COLOR2("Color Attachment #2",
			GL30.GL_COLOR_ATTACHMENT2, 2), COLOR3("Color Attachment #3", GL30.GL_COLOR_ATTACHMENT3, 3), COLOR4("Color Attachment #4",
			GL30.GL_COLOR_ATTACHMENT4, 4), COLOR5("Color Attachment #5", GL30.GL_COLOR_ATTACHMENT5, 5), COLOR6("Color Attachment #6",
			GL30.GL_COLOR_ATTACHMENT6, 6), COLOR7("Color Attachment #7", GL30.GL_COLOR_ATTACHMENT7, 7), COLOR8("Color Attachment #8",
			GL30.GL_COLOR_ATTACHMENT8, 8), COLOR9("Color Attachment #9", GL30.GL_COLOR_ATTACHMENT9, 9), COLOR10("Color Attachment #10",
			GL30.GL_COLOR_ATTACHMENT10, 10), COLOR11("Color Attachment #11", GL30.GL_COLOR_ATTACHMENT11, 11), COLOR12("Color Attachment #12",
			GL30.GL_COLOR_ATTACHMENT12, 12), COLOR13("Color Attachment #13", GL30.GL_COLOR_ATTACHMENT13, 13), COLOR14("Color Attachment #14",
			GL30.GL_COLOR_ATTACHMENT14, 14), COLOR15("Color Attachment #15", GL30.GL_COLOR_ATTACHMENT15, 15), DEPTH("Depth Attachment",
			GL30.GL_DEPTH_ATTACHMENT), STENCIL("Stencil Attachment", GL30.GL_STENCIL_ATTACHMENT), DEPTH_STENCIL("Depth & Stencil Attachment",
			GL30.GL_DEPTH_STENCIL_ATTACHMENT);
	
	public final String name;
	public final int value;
	public final int colorindex;
	
	private FBOAttachment(String name, int value) {
		this(name, value, -1);
	}
	
	private FBOAttachment(String name, int value, int colorindex) {
		this.name = name;
		this.value = value;
		this.colorindex = colorindex;
	}
	
	public static FBOAttachment get(int i) {
		for (FBOAttachment attachment : values())
			if (attachment.value == i)
				return attachment;
		return null;
	}
	
	public String toString() {
		return name;
	}
	
}
