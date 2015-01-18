package lwjgl.core.objects.framebuffers;

import lwjgl.core.objects.framebuffers.values.FBOAttachment;

public interface FBOAttachable {
	
	public void attachToFBO(FBOAttachment attachment, int level, int layer);
	
}
