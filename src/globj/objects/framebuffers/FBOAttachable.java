package globj.objects.framebuffers;

import globj.objects.framebuffers.values.FBOAttachment;

public interface FBOAttachable {
	
	public void attachToFBO(FBOAttachment attachment, int level, int layer);
	
}
