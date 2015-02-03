package globj.objects.arrays;

import globj.core.DataType;

public class VBOFormat {
	
	public final int components;
	public final DataType type;;
	public final int stride;
	public final int offset;
	
	private VBOFormat(int components, DataType type, int stride, int offset) {
		this.components = components;
		this.type = type;
		this.stride = stride;
		this.offset = offset;
	}
}
