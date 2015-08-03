package globj.objects.bufferobjects;


import org.lwjgl.opengl.GL15;

import globj.core.DataType;
import globj.objects.bufferobjects.values.VBOTarget;
import globj.objects.bufferobjects.values.VBOUsage;



public abstract class DynamicVBO<T> extends VBO {
	
	/**
	 * Don't forget to flip the buffer when you finish putting in values.
	 */
	public DynamicVBO(String name, DataType dataType, VBOTarget target) {
		super(name, target, dataType, VBOUsage.DYNAMIC_DRAW);
		bind();
		GL15.glBufferData(target().value(), 0, usage().value());
		undobind();
	}
	
	protected final void orphan() {
		GL15.glBufferData(target().value(), 0, usage().value());
	}
	
	public abstract void write(T data);
	
	public abstract void update(T data, int off, int len, long start);
	
}
