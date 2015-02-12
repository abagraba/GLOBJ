package globj.objects.bufferobjects;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.nio.ShortBuffer;

import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL44;

import globj.core.DataType;
import globj.core.GL;
import globj.core.utils.LWJGLBuffers;
import globj.objects.bufferobjects.values.VBOUsage;
/**
 * Static VBOs are initialized with a set of values that will never change. If you require VBOs to have modifiable data, see FixedVBO or DynamicVBO 
 *
 */
public class StaticVBO extends VBO {
	
	/**
	 * Don't forget to flip the buffer when you finish putting in values.
	 */
	public StaticVBO(String name, VBOTarget target, FloatBuffer buffer){
		super(name, target, DataType.FLOAT, VBOUsage.STATIC_DRAW);
		bind();
		if (GL.versionCheck(4, 4))
			GL44.glBufferStorage(target.value, buffer, 0);
		else
			GL15.glBufferData(target.value, buffer, usage.value);
		undobind();
	}

	public StaticVBO(String name, VBOTarget target, IntBuffer buffer){
		super(name, target, DataType.UINT, VBOUsage.STATIC_DRAW);
		bind();
		if (GL.versionCheck(4, 4))
			GL44.glBufferStorage(target.value, buffer, 0);
		else
			GL15.glBufferData(target.value, buffer, usage.value);
		undobind();
	}

	public StaticVBO(String name, VBOTarget target, ShortBuffer buffer){
		super(name, target, DataType.USHORT, VBOUsage.STATIC_DRAW);
		bind();
		if (GL.versionCheck(4, 4))
			GL44.glBufferStorage(target.value, buffer, 0);
		else
			GL15.glBufferData(target.value, buffer, usage.value);
		undobind();
	}

	public StaticVBO(String name, VBOTarget target, ByteBuffer buffer){
		super(name, target, DataType.UBYTE, VBOUsage.STATIC_DRAW);
		bind();
		if (GL.versionCheck(4, 4))
			GL44.glBufferStorage(target.value, buffer, 0);
		else
			GL15.glBufferData(target.value, buffer, usage.value);
		undobind();
	}

	public StaticVBO(String name, VBOTarget target, float[] buffer){
		this(name, target, LWJGLBuffers.floatBuffer(buffer));
	}

	public StaticVBO(String name, VBOTarget target, int[] buffer){
		this(name, target, LWJGLBuffers.intBuffer(buffer));
	}

	public StaticVBO(String name, VBOTarget target, short[] buffer){
		this(name, target, LWJGLBuffers.shortBuffer(buffer));
	}

	public StaticVBO(String name, VBOTarget target, byte[] buffer){
		this(name, target, LWJGLBuffers.byteBuffer(buffer));
	}

}
