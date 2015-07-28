package globj.core;

import globj.objects.bufferobjects.VBO;

public class Attribute {
	
	protected VBO buffer;
	protected int shaderloc;
	
	protected int stride;
	protected int offset;

	public Attribute(VBO buffer, int stride, int offset){
		this.buffer = buffer;
		this.stride = stride;
		this.offset = offset;
	}

	public Attribute(VBO buffer){
		this(buffer, 0, 0);
	}
	
	
}
