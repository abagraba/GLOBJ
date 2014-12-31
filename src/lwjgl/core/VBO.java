package lwjgl.core;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.nio.ShortBuffer;
import java.util.HashMap;

import lwjgl.debug.Logging;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;

public class VBO extends GLObject {
	
	protected static final HashMap<String, VBO> vboname = new HashMap<String, VBO>();
	protected static final HashMap<Integer, VBO> vboid = new HashMap<Integer, VBO>();
	protected static final HashMap<VBOTarget, Integer> current = new HashMap<VBOTarget, Integer>();
	protected static final HashMap<VBOTarget, Integer> last = new HashMap<VBOTarget, Integer>();
	
	private final int vbo;
	private final VBOTarget target;
	
	private VBO(String name, VBOTarget target) {
		super(name);
		vbo = GL15.glGenBuffers();
		this.target = target;
	}
	
	public static VBO create(String name, VBOTarget target) {
		if (vboname.containsKey(name)) {
			Logging.glError("Cannot create VBO. VBO [" + name + "] already exists.", null);
			return null;
		}
		VBO vbo = new VBO(name, target);
		if (vbo.vbo == 0) {
			Logging.glError("Cannot create VBO. No ID could be allocated for VBO [" + name + "].", null);
			return null;
		}
		vboname.put(vbo.name, vbo);
		vboid.put(vbo.vbo, vbo);
		return vbo;
	}
	
	public static VBO get(String name) {
		return vboname.get(name);
	}
	
	protected static VBO get(int id) {
		return vboid.get(id);
	}
	
	protected static void bind(int vbo, VBOTarget target) {
		int c = current.containsKey(target) ? current.get(target) : 0;
		if (vbo == c) {
			last.put(target, c);
			return;
		}
		GL15.glBindBuffer(target.value, vbo);
		last.put(target, c);
		current.put(target, vbo);
	}
	
	public static void bind(String name) {
		VBO t = get(name);
		if (t == null) {
			Logging.glError("Cannot bind VBO [" + name + "]. Does not exist.", null);
			return;
		}
		t.bind();
	}
	
	public void bind() {
		bind(vbo, target);
	}
	
	private static void bindLast(VBOTarget target) {
		int l = last.containsKey(target) ? last.get(target) : 0;
		bind(l, target);
	}
	
	public void destroy() {
		if (current.get(target) == vbo)
			bind(0, target);
		GL15.glDeleteBuffers(vbo);
		vboname.remove(name);
		vboid.remove(vbo);
	}
	
	public static void destroy(String name) {
		VBO vbo = get(name);
		if (vbo != null)
			vbo.destroy();
		else
			Logging.glWarning("Cannot delete VBO. VBO [" + name + "] does not exist.");
	}
	
	public void bufferData(ShortBuffer data) {
		bind();
		GL15.glBufferData(target.value, data, GL15.GL_DYNAMIC_DRAW);
		bindLast(target);
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
		bindLast(target);
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
		bindLast(target);
	}
	
	public void bufferData(float[] data) {
		FloatBuffer buffer = BufferUtils.createFloatBuffer(data.length);
		buffer.put(data);
		buffer.flip();
		bufferData(buffer);
	}
	
	@Override
	public String[] status() {
		// TODO Auto-generated method stub
		return null;
	}
	
}