package lwjgl.core.values;


import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;
import org.lwjgl.opengl.GL30;

public enum DataType {
	
	UBYTE("Unsigned Byte", GL11.GL_UNSIGNED_BYTE),
	BYTE("Byte", GL11.GL_BYTE),
	USHORT("Unsigned Short", GL11.GL_UNSIGNED_SHORT),
	SHORT("Short", GL11.GL_SHORT),
	UINT("Unsigned Integer", GL11.GL_UNSIGNED_INT),
	INT("Integer", GL11.GL_INT),
	FLOAT("Float", GL11.GL_FLOAT),

	UBYTE_332("Unsigned Byte 3 3 2", GL12.GL_UNSIGNED_BYTE_3_3_2),
	UBYTE_233R("Unsigned Byte 2 3 3 Reversed", GL12.GL_UNSIGNED_BYTE_2_3_3_REV),
	
	USHORT_565("Unsigned Short 5 6 5", GL12.GL_UNSIGNED_SHORT_5_6_5),
	USHORT_565R("Unsigned Short 5 6 5 Reversed", GL12.GL_UNSIGNED_SHORT_5_6_5_REV),
	USHORT_5551("Unsigned Short 5 5 5 1", GL12.GL_UNSIGNED_SHORT_5_5_5_1),
	USHORT_1555R("Unsigned Short 1 5 5 5 Reversed", GL12.GL_UNSIGNED_SHORT_1_5_5_5_REV),
	USHORT_4444("Unsigned Short 4 4 4 4", GL12.GL_UNSIGNED_SHORT_4_4_4_4),
	USHORT_4444R("Unsigned Short 4 4 4 4 Reversed", GL12.GL_UNSIGNED_SHORT_4_4_4_4_REV),
	
	UINT_24_8("Unsigned Integer 24 8", GL30.GL_UNSIGNED_INT_24_8),
	UINT_8888("Unsigned Integer 8 8 8 8", GL12.GL_UNSIGNED_INT_8_8_8_8),
	UINT_8888R("Unsigned Integer 8 8 8 8 Reversed", GL12.GL_UNSIGNED_INT_8_8_8_8_REV),
	UINT_1010102("Unsigned Integer 10 10 10 2", GL12.GL_UNSIGNED_INT_10_10_10_2),
	UINT_2101010R("Unsigned Integer 2 10 10 10 Reversed", GL12.GL_UNSIGNED_INT_2_10_10_10_REV);
	
	public final String name;
	public final int value;
	
	private DataType(String name, int value) {
		this.name = name;
		this.value = value;
	}
	
	public static DataType get(int i){
		for (DataType type : values()) 
			if (type.value == i)
				return type;
		return null;
	}
	
	public String toString(){
		return name;
	}
	
}
