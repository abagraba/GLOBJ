package lwjgl.core;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.nio.ShortBuffer;
import java.util.HashMap;

import lwjgl.core.objects.BindableGLObject;
import lwjgl.debug.Logging;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL15;

public class VBO extends BindableGLObject{
	
	protected static final HashMap<String, VBO> vboname = new HashMap<String, VBO>();
	protected static final HashMap<Integer, VBO> vboid = new HashMap<Integer, VBO>();
	protected static final HashMap<VBOTarget, Integer> current = new HashMap<VBOTarget, Integer>();
	protected static final HashMap<VBOTarget, Integer> last = new HashMap<VBOTarget, Integer>();
	
	protected final VBOTarget target;
	
	private VBO(String name, VBOTarget target) {
		super(name, GL15.glGenBuffers());
		this.target = target;
	}
	
	public static VBO create(String name, VBOTarget target) {
		if (vboname.containsKey(name)) {
			Logging.glError("Cannot create VBO. VBO [" + name + "] already exists.", null);
			return null;
		}
		VBO vbo = new VBO(name, target);
		if (vbo.id == 0) {
			Logging.glError("Cannot create VBO. No ID could be allocated for VBO [" + name + "].", null);
			return null;
		}
		vboname.put(vbo.name, vbo);
		vboid.put(vbo.id, vbo);
		return vbo;
	}
	
	public static VBO get(String name) {
		return vboname.get(name);
	}
	
	protected static VBO get(int id) {
		return vboid.get(id);
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

	public static void bind(String name) {
		VBO t = get(name);
		if (t == null) {
			Logging.glError("Cannot bind VBO [" + name + "]. Does not exist.", null);
			return;
		}
		t.bind();
	}
	
	@Override
	public void bindNone() {
		// TODO Auto-generated method stub
		
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
	
	public void destroy() {
		if (current.get(target) == id)
			bind(0, target);
		GL15.glDeleteBuffers(id);
		vboname.remove(name);
		vboid.remove(id);
	}
	
	public static void destroy(String name) {
		VBO vbo = get(name);
		if (vbo != null)
			vbo.destroy();
		else
			Logging.glWarning("Cannot delete VBO. VBO [" + name + "] does not exist.");
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