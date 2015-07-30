package globj.objects.framebuffers;


import org.eclipse.jdt.annotation.NonNullByDefault;

import globj.objects.framebuffers.values.FBOAttachment;



@NonNullByDefault
public interface FBOAttachable {
	
	public void attachToFBO(FBOAttachment attachment, int level, int layer);
	
}
