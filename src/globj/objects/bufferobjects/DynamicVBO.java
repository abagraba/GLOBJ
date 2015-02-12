package globj.objects.bufferobjects;

import globj.core.DataType;
import globj.objects.bufferobjects.values.VBOUsage;

import java.nio.ByteBuffer;

import org.lwjgl.opengl.GL15;

public class DynamicVBO extends VBO {
	
	/**
	 * Don't forget to flip the buffer when you finish putting in values.
	 */
	public DynamicVBO(String name, VBOTarget target) {
		super(name, target, DataType.FLOAT, VBOUsage.DYNAMIC_DRAW);
		bind();
		GL15.glBufferData(target.value, (ByteBuffer) null, usage.value);
		undobind();
	}
	
}
