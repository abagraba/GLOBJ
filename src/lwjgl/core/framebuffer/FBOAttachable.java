package lwjgl.core.framebuffer;

import lwjgl.core.framebuffer.values.FBOAttachment;

public interface FBOAttachable {
	
	public void attachToFBO(FBOAttachment attachment, int level, int layer);
	
}
