package globj.objects.bufferobjects;

import globj.objects.BindableGLObject;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.nio.ShortBuffer;
import java.util.HashMap;

import lwjgl.debug.Logging;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL15;

public class VBO extends BindableGLObject{
	
	
	protected static final HashMap<VBOTarget, Integer> current = new HashMap<VBOTarget, Integer>();
	protected static final HashMap<VBOTarget, Integer> last = new HashMap<VBOTarget, Integer>();
	
	protected final VBOTarget target;
	
	private VBO(String name, VBOTarget target) {
		super(name, GL15.glGenBuffers());
		this.target = target;
	}
	
	public static VBO create(String name, VBOTarget target) {
		VBO vbo = new VBO(name, target);
		if (vbo.id == 0) {
			Logging.glError("Cannot create VBO. No ID could be allocated for VBO [" + name + "].", null);
			return null;
		}
		return vbo;
	}
	
	private static void bind(int vbo, VBOTarget target) {
		int c = current.containsKey(target) ? current.get(target) : 0;
		if (vbo == c) {
			last.put(target, c);
			return;
		}
		GL15.glBindBuffer(target.value, vbo);
		last.put(target, c);
		current.put(target, vbo);
	}

	
	@Override
	public void bindNone() {
		bind(0, target);
	}
	public static void unbind(VBOTarget target){
		bind(0, target);
	}
	
	public void bind() {
		bind(id, target);
	}

	protected void undobind(){
		int l = last.containsKey(target) ? last.get(target) : 0;
		bind(l, target);
	}
	
	protected void destroy() {
		if (current.get(target) == id)
			bind(0, target);
		GL15.glDeleteBuffers(id);
	}
	
	/**
	 * TODO: BUFFER SUBRANGES
	 */
	public void bufferData(ShortBuffer data) {
		bind();
		GL15.glBufferData(target.value, data, GL15.GL_DYNAMIC_DRAW);
		undobind();
	}
	
	public void bufferData(short[] data) {
		ShortBuffer buffer = BufferUtils.createShortBuffer(data.length);
		buffer.put(data);
		buffer.flip();
		bufferData(buffer);
	}
	
	public void bufferData(IntBuffer data) {
		bind();
		GL15.glBufferData(target.value, data, GL15.GL_DYNAMIC_DRAW);
		undobind();
	}
	
	public void bufferData(int[] data) {
		IntBuffer buffer = BufferUtils.createIntBuffer(data.length);
		buffer.put(data);
		buffer.flip();
		bufferData(buffer);
	}
	
	public void bufferData(FloatBuffer data) {
		bind();
		GL15.glBufferData(target.value, data, GL15.GL_DYNAMIC_DRAW);
		undobind();
	}
	
	public void bufferData(float[] data) {
		FloatBuffer buffer = BufferUtils.createFloatBuffer(data.length);
		buffer.put(data);
		buffer.flip();
		bufferData(buffer);
	}
	
	@Override
	public void debug() {
		// TODO Auto-generated method stub
	}


	
}