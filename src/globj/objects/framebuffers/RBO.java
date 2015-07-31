package globj.objects.framebuffers;


import globj.core.Context;
import globj.objects.GLObject;
import globj.objects.framebuffers.values.FBOAttachment;
import globj.objects.textures.values.TextureFormat;

import static lwjgl.debug.GLDebug.*;

import java.util.HashMap;
import java.util.Map;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL30;



public class RBO extends GLObject implements FBOAttachable {
	
	protected static final Map<String, RBO>		rboname		= new HashMap<String, RBO>();
	protected static final Map<Integer, RBO>	rboid		= new HashMap<Integer, RBO>();
	protected static int						currentRBO	= 0;
	private static int							lastRBO		= 0;
	
	private RBO(String name) {
		super(name, GL30.glGenRenderbuffers());
	}
	
	public static RBO create(String name, int w, int h, int format) {
		if (rboname.containsKey(name)) {
			glError("Cannot create Renderbuffer Object. Renderbuffer Object [" + name + "] already exists.", null);
			return null;
		}
		RBO rbo = new RBO(name);
		if (rbo.id == 0) {
			glError("Cannot create Renderbuffer Object. No ID could be allocated for Renderbuffer Object [" + name + "].", null);
			return null;
		}
		
		flushErrors();
		rbo.bind();
		GL30.glRenderbufferStorage(GL30.GL_RENDERBUFFER, format, w, h);
		rbo.undobind();
		if (checkError())
			return null;
			
		rboname.put(rbo.name, rbo);
		rboid.put(rbo.id, rbo);
		return rbo;
	}
	
	private static boolean checkError() {
		int err = nextError();
		while (err != GL11.GL_NO_ERROR) {
			switch (err) {
				case GL11.GL_INVALID_ENUM:
					glError("Cannot create Renderbuffer Object. Invalid format.", null);
					return true;
				case GL11.GL_INVALID_VALUE:
					glError("Cannot create Renderbuffer Object. Renderbuffer Object too large. Max Size is "+ Context.intConst(GL30.GL_MAX_RENDERBUFFER_SIZE)
							+ " pixels.", null);
					return true;
				case GL11.GL_OUT_OF_MEMORY:
					glError("Cannot create Renderbuffer Object. Out of Memory.", null);
					return true;
				default:
			}
			err = nextError();
		}
		return false;
	}
	
	public static void destroy(String name) {
		if (!rboname.containsKey(name)) {
			glWarning("Cannot delete Renderbuffer Object. Renderbuffer Object [" + name + "] does not exist.");
			return;
		}
		RBO rbo = rboname.get(name);
		if (currentRBO == rbo.id)
			bind(0);
		GL30.glDeleteRenderbuffers(rbo.id);
		rboname.remove(rbo.name);
		rboid.remove(rbo.id);
	}
	
	public static RBO get(String name) {
		return rboname.get(name);
	}
	
	protected static RBO get(int id) {
		return rboid.get(id);
	}
	
	protected static void bind(int rbo) {
		if (rbo == currentRBO) {
			lastRBO = rbo;
			return;
		}
		GL30.glBindRenderbuffer(GL30.GL_RENDERBUFFER, rbo);
		lastRBO = currentRBO;
		currentRBO = rbo;
	}
	
	public static void bind(String name) {
		if (name == null) {
			bind(0);
			return;
		}
		RBO r = get(name);
		if (r == null) {
			glError("Cannot bind RBO [" + name + "]. Does not exist.", null);
			return;
		}
		r.bind();
	}
	
	public void bind() {
		bind(id);
	}
	
	protected void undobind() {
		bind(lastRBO);
	}
	
	/**************************************************/
	/****************** FBOAttachable *****************/
	
	/**************************************************/
	/**
	 * @param level
	 *            mipmap level.
	 * @param layer
	 *            z offset.
	 */
	@Override
	public void attachToFBO(FBOAttachment attachment, int level, int layer) {
		GL30.glFramebufferRenderbuffer(GL30.GL_DRAW_FRAMEBUFFER, attachment.value(), GL30.GL_RENDERBUFFER, id);
	}
	
	/**************************************************/
	
	@Override
	public void debugQuery() {
		flushErrors();
		bind();
		//#formatter:off
		writef(ATTRIB_STRING, "RBO:", name);
		indent();
			writef(ATTRIB + "%d x %d", "Size:" , 
			       GL30.glGetRenderbufferParameteri(GL30.GL_RENDERBUFFER, GL30.GL_RENDERBUFFER_WIDTH),
			       GL30.glGetRenderbufferParameteri(GL30.GL_RENDERBUFFER, GL30.GL_RENDERBUFFER_HEIGHT));
			writef(ATTRIB + "[%d, %d, %d, %d] bits", "RGBA Size:" , 
			       GL30.glGetRenderbufferParameteri(GL30.GL_RENDERBUFFER, GL30.GL_RENDERBUFFER_RED_SIZE),
			       GL30.glGetRenderbufferParameteri(GL30.GL_RENDERBUFFER, GL30.GL_RENDERBUFFER_GREEN_SIZE),
			       GL30.glGetRenderbufferParameteri(GL30.GL_RENDERBUFFER, GL30.GL_RENDERBUFFER_BLUE_SIZE),
			       GL30.glGetRenderbufferParameteri(GL30.GL_RENDERBUFFER, GL30.GL_RENDERBUFFER_ALPHA_SIZE));
			writef(ATTRIB_INT, "Depth Size:", GL30.glGetRenderbufferParameteri(GL30.GL_RENDERBUFFER, GL30.GL_RENDERBUFFER_DEPTH_SIZE)); 
			writef(ATTRIB_INT, "Stencil Size:", GL30.glGetRenderbufferParameteri(GL30.GL_RENDERBUFFER, GL30.GL_RENDERBUFFER_STENCIL_SIZE));
			writef(ATTRIB_INT, "Samples:", GL30.glGetRenderbufferParameteri(GL30.GL_RENDERBUFFER, GL30.GL_RENDERBUFFER_SAMPLES));
			writef(ATTRIB_STRING, "Format:", TextureFormat.get(GL30.glGetRenderbufferParameteri(GL30.GL_RENDERBUFFER, GL30.GL_RENDERBUFFER_INTERNAL_FORMAT)));
		unindent();
		//#formatter:on
		undobind();
		flushErrors();
	}
}
