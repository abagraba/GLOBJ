package lwjgl.debug;


import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GLContext;
import org.skx.logger.AbstractLogger;

import globj.core.GLException;
import globj.objects.GLObject;



public class GLDebug {
	
	private static AbstractLogger logger = new AbstractLogger(System.out);
	
	public static final String ATTRIB = "%-24s\t";
	
	public static final String	ATTRIB_STRING	= "%-24s\t%s";
	public static final String	ATTRIB_INT		= "%-24s\t%d";
	public static final String	ATTRIB_HEX_BYTE	= "%-24s\t0x%02X";
	public static final String	ATTRIB_HEX_INT	= "%-24s\t0x%08X";
	public static final String	ATTRIB_FLOAT_8	= "%-24s\t%8f";
	
	public static final String ATTRIB_L = "%-48s\t";
	
	public static final String	ATTRIB_L_STRING		= "%-48s\t%s";
	public static final String	ATTRIB_L_INT		= "%-48s\t%d";
	public static final String	ATTRIB_L_HEX_BYTE	= "%-48s\t0x%02X";
	public static final String	ATTRIB_L_HEX_INT	= "%-48s\t0x%08X";
	public static final String	ATTRIB_L_FLOAT_8	= "%-48s\t%8f";
	
	private GLDebug() {
	}
	
	public static void indent() {
		logger.indent();
	}
	
	public static void unindent() {
		logger.unindent();
	}
	
	public static void indent(int i) {
		logger.indent(i);
	}
	
	public static void unindent(int i) {
		logger.unindent(i);
	}
	
	public static void setIndent(int i) {
		logger.setIndent(i);
	}
	
	public static void unsetIndent() {
		logger.unsetIndent();
	}
	
	public static void write(String string) {
		logger.write(string);
	}
	
	public static void writef(String format, Object... args) {
		logger.writef(format, args);
	}
	
	public static void write(Object o) {
		logger.write(o);
	}
	
	public static void write(Object[] o) {
		logger.write(o);
	}
	
	public static void write(Iterable<?> o) {
		logger.write(o);
	}
	
	public static void separator() {
		logger.separator();
	}
	
	public static void logException(Exception e) {
		logger.logException(e);
	}
	
	public static void glWarning(String warning) {
		setIndent(0);
		write("WARNING:");
		indent();
		write(warning);
		unsetIndent();
	}
	
	public static void glError(String error) {
		glError(error, null);
	}
	
	public static void glError(String error, GLObject obj) {
		GLException e = new GLException(error, obj);
		setIndent(0);
		separator();
		write("ERROR:");
		indent();
		write(error);
		indent();
		if (obj != null)
			obj.debugQuery();
		unindent();
		separator();
		logException(e);
		separator();
		unsetIndent();
	}
	
	public static void debug(GLObject obj) {
		if (obj != null)
			obj.debugQuery();
		else
			System.out.println("NULL Object");
	}
	
	public static void logInfo(String[] info) {
		if (info != null)
			for (String stat : info)
				System.out.println(stat);
		else
			System.out.println("No Info");
	}
	
	public static void glObjError(Class<? extends GLObject> obj, String name, String error, String message) {
		glError(String.format("%s %s [%s]: %s.", error, obj, name, message), null);
	}
	
	public static void glObjError(GLObject obj, String error, String message) {
		glObjError(obj.getClass(), obj.name(), error, message);
	}
	
	public static int nextError() {
		return GL11.glGetError();
	}
	
	public static void flushErrors() {
		int err = GL11.glGetError();
		while (err != GL11.GL_NO_ERROR) {
			write(GLContext.translateGLErrorString(err));
			err = GL11.glGetError();
		}
	}
	
	public static void flushErrorsV() {
		int err = GL11.glGetError();
		if (err == GL11.GL_NO_ERROR)
			write("No Error");
		while (err != GL11.GL_NO_ERROR) {
			write(GLContext.translateGLErrorString(err));
			err = GL11.glGetError();
		}
	}
	
}
