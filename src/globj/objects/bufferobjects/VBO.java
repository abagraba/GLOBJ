package globj.objects.bufferobjects;

import globj.core.DataType;
import globj.objects.BindTracker;
import globj.objects.BindableGLObject;
import globj.objects.bufferobjects.values.VBOUsage;

import java.util.HashMap;

import org.lwjgl.opengl.GL15;

public abstract class VBO extends BindableGLObject{
	
	public final VBOTarget target;
	public final DataType dataType;
	public final VBOUsage usage;
	
	protected VBO(String name, VBOTarget target, DataType dataType, VBOUsage usage) {
		super(name, GL15.glGenBuffers());
		this.target = target;
		this.dataType = dataType;
		this.usage = usage;
	}
	
	public void update() {
		bind();
		resolveStates();
		undobind();
	}
	
	protected void resolveStates() {
	}

	
//	protected static VBO create(String name, VBOTarget target, DataType dataType, VBOUsage usage) {
//		VBO vbo = new VBO(name, target, dataType, usage);
//		if (vbo.id == 0) {
//			GLDebug.globjError(vbo, "Could not create", "No ID could be allocated");
//			return null;
//		}
//		return vbo;
//	}
	
	/**************************************************/
	/********************** Bind **********************/
	/**************************************************/
	
	protected static final HashMap<VBOTarget, BindTracker> trackers = new HashMap<VBOTarget, BindTracker>();
	static {
		for (VBOTarget target : VBOTarget.values()) {
			trackers.put(target, new BindTracker());
		}
	}
	@Override
	protected final BindTracker bindingTracker() {
		return trackers.get(target);
	}
	
	@Override
	protected final void bindOP(int id) {
		GL15.glBindBuffer(target.value, id);
	}
	
	@Override
	protected final void destroyOP() {		
		GL15.glDeleteBuffers(id);
	}

	@Override
	public final void destroy() {		
		super.destroy();
		VBOs.unregisterVBO(this);
	}

	/**************************************************/
	
	/**
	 * TODO: BUFFER SUBRANGES
	 * TODO: BUFFER STORAGE
	 */
	
	@Override
	public void debug() {
		// TODO Auto-generated method stub
	}


	
}