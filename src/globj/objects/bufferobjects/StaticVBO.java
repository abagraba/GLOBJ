package globj.objects.bufferobjects;


import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.nio.ShortBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL44;

import globj.core.DataType;
import globj.core.Window;
import globj.objects.bufferobjects.values.VBOTarget;
import globj.objects.bufferobjects.values.VBOUsage;



/**
 * Static VBOs are initialized with a set of values that will never change. If you require VBOs to have modifiable data,
 * see FixedVBO or Dynamic*VBO.
 *
 */
public class StaticVBO extends VBO {
	
	/**
	 * Don't forget to flip the buffer when you finish putting in values.
	 */
	private StaticVBO(String name, VBOTarget target, FloatBuffer buffer) {
		super(name, target, DataType.FLOAT, VBOUsage.STATIC_DRAW);
		bind();
		if (Window.versionCheck(4, 4))
			GL44.glBufferStorage(target.value(), buffer, 0);
		else
			GL15.glBufferData(target.value(), buffer, usage().value());
		undobind();
	}
	
	private StaticVBO(String name, VBOTarget target, IntBuffer buffer) {
		super(name, target, DataType.UINT, VBOUsage.STATIC_DRAW);
		bind();
		if (Window.versionCheck(4, 4))
			GL44.glBufferStorage(target.value(), buffer, 0);
		else
			GL15.glBufferData(target.value(), buffer, usage().value());
		undobind();
	}
	
	private StaticVBO(String name, VBOTarget target, ShortBuffer buffer) {
		super(name, target, DataType.USHORT, VBOUsage.STATIC_DRAW);
		bind();
		if (Window.versionCheck(4, 4))
			GL44.glBufferStorage(target.value(), buffer, 0);
		else
			GL15.glBufferData(target.value(), buffer, usage().value());
		undobind();
	}
	
	private StaticVBO(String name, VBOTarget target, ByteBuffer buffer) {
		super(name, target, DataType.UBYTE, VBOUsage.STATIC_DRAW);
		bind();
		if (Window.versionCheck(4, 4))
			GL44.glBufferStorage(target.value(), buffer, 0);
		else
			GL15.glBufferData(target.value(), buffer, usage().value());
		undobind();
	}
	
	public static StaticVBO create(String name, VBOTarget target, FloatBuffer buffer) {
		StaticVBO vbo = new StaticVBO(name, target, buffer);
		VBOs.registerVBO(vbo);
		return vbo;
	}
	
	public static StaticVBO create(String name, VBOTarget target, float[] buffer) {
		FloatBuffer buff = BufferUtils.createFloatBuffer(buffer.length);
		buff.put(buffer).flip();
		return create(name, target, buff);
	}
	
	public static StaticVBO create(String name, VBOTarget target, IntBuffer buffer) {
		StaticVBO vbo = new StaticVBO(name, target, buffer);
		VBOs.registerVBO(vbo);
		return vbo;
	}
	
	public static StaticVBO create(String name, VBOTarget target, int[] buffer) {
		IntBuffer buff = BufferUtils.createIntBuffer(buffer.length);
		buff.put(buffer).flip();
		return create(name, target, buff);
	}
	
	public static StaticVBO create(String name, VBOTarget target, ShortBuffer buffer) {
		StaticVBO vbo = new StaticVBO(name, target, buffer);
		VBOs.registerVBO(vbo);
		return vbo;
	}
	
	public static StaticVBO create(String name, VBOTarget target, short[] buffer) {
		ShortBuffer buff = BufferUtils.createShortBuffer(buffer.length);
		buff.put(buffer).flip();
		return create(name, target, buff);
	}
	
	public static StaticVBO create(String name, VBOTarget target, ByteBuffer buffer) {
		StaticVBO vbo = new StaticVBO(name, target, buffer);
		VBOs.registerVBO(vbo);
		return vbo;
	}
	
	public static StaticVBO create(String name, VBOTarget target, byte[] buffer) {
		ByteBuffer buff = BufferUtils.createByteBuffer(buffer.length);
		buff.put(buffer).flip();
		return create(name, target, buff);
	}
	
}
