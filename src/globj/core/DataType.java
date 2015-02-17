package globj.core;

import org.lwjgl.opengl.GL11;

public enum DataType {
	
	UBYTE("Unsigned Byte", GL11.GL_UNSIGNED_BYTE, 1),
	BYTE("Byte", GL11.GL_BYTE, 1),
	USHORT("Unsigned Short", GL11.GL_UNSIGNED_SHORT, 2),
	SHORT("Short", GL11.GL_SHORT, 2),
	UINT("Unsigned Integer", GL11.GL_UNSIGNED_INT, 4),
	INT("Integer", GL11.GL_INT, 4),
	FLOAT("Float", GL11.GL_FLOAT, 4);
	
	public final String name;
	public final int value;
	public final int bytes;
	
	private DataType(String name, int value, int bytes) {
		this.name = name;
		this.value = value;
		this.bytes = bytes;
	}
	
	public static DataType get(int i) {
		for (DataType type : values())
			if (type.value == i)
				return type;
		return null;
	}
	
	@Override
	public String toString() {
		return name;
	}
	
}
