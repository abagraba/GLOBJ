package globj.core;


import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.jdt.annotation.Nullable;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL30;



@NonNullByDefault
public enum DataType {
	
	UBYTE("Unsigned Byte", GL11.GL_UNSIGNED_BYTE, 1),
	BYTE("Byte", GL11.GL_BYTE, 1),
	USHORT("Unsigned Short", GL11.GL_UNSIGNED_SHORT, 2),
	SHORT("Short", GL11.GL_SHORT, 2),
	UINT("Unsigned Integer", GL11.GL_UNSIGNED_INT, 4),
	INT("Integer", GL11.GL_INT, 4),
	HALF_FLOAT("Float", GL30.GL_HALF_FLOAT, 2),
	FLOAT("Float", GL11.GL_FLOAT, 4),
	DOUBLE("Double", GL11.GL_DOUBLE, 8);
	
	private final String	name;
	private final int		value;
	private final int		size;
	
	
	private DataType(String name, int value, int size) {
		this.name = name;
		this.value = value;
		this.size = size;
	}
	
	/**
	 * @param glInt
	 *            the GLint representing a shader type
	 * @return the DataType object represented by glInt
	 */
	@Nullable
	public static DataType get(int glInt) {
		for (DataType type : values())
			if (type.value == glInt)
				return type;
		return null;
	}
	
	/**
	 * @return the name of this data type
	 */
	public String typeName() {
		return name;
	}
	
	/**
	 * @return the GLint representing this shader type
	 */
	public int value() {
		return value;
	}
	
	/**
	 * @return the size of this data type in bytes
	 */
	public int size() {
		return size;
	}
	
	@Override
	public String toString() {
		return name;
	}
	
}
