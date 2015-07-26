package globj.objects.arrays;

import globj.core.DataType;

public class VBOFormat {
	
	public final static VBOFormat FLOAT_1_1_0 = new VBOFormat(1, DataType.FLOAT, DataType.FLOAT.size, 0);
	public final static VBOFormat FLOAT_2_2_0 = new VBOFormat(2, DataType.FLOAT, DataType.FLOAT.size * 2, 0);
	public final static VBOFormat FLOAT_3_3_0 = new VBOFormat(3, DataType.FLOAT, DataType.FLOAT.size * 3, 0);
	public final static VBOFormat FLOAT_4_4_0 = new VBOFormat(4, DataType.FLOAT, DataType.FLOAT.size * 4, 0);
	public final static VBOFormat FLOAT_2_4_0 = new VBOFormat(2, DataType.FLOAT, DataType.FLOAT.size * 4, 0);
	public final static VBOFormat FLOAT_2_4_2 = new VBOFormat(2, DataType.FLOAT, DataType.FLOAT.size * 4, DataType.FLOAT.size * 2);
	public final static VBOFormat FLOAT_2_6_0 = new VBOFormat(2, DataType.FLOAT, DataType.FLOAT.size * 6, 0);
	public final static VBOFormat FLOAT_2_6_2 = new VBOFormat(2, DataType.FLOAT, DataType.FLOAT.size * 6, DataType.FLOAT.size * 2);
	public final static VBOFormat FLOAT_2_6_4 = new VBOFormat(2, DataType.FLOAT, DataType.FLOAT.size * 6, DataType.FLOAT.size * 4);
	
	public final int components;
	public final DataType type;;
	public final int stride;
	public final int offset;
	
	public VBOFormat(int components, DataType type, int stride, int offset) {
		this.components = components;
		this.type = type;
		this.stride = stride;
		this.offset = offset;
	}
}
