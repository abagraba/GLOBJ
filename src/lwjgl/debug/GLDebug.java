package lwjgl.debug;


import java.io.PrintWriter;

import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.GLU;

import globj.core.GL;
import globj.core.GLException;
import globj.objects.GLObject;



public class GLDebug {
	
	private static final boolean	debug				= true;
	
	private static int				indent				= 0;
	private static int				lastindent			= 0;
	private static int				pad					= 24;
	private static int				lastpad				= 24;
	
	public static final String		ATTRIB				= "%-24s:\t";
	
	public static final String		ATTRIB_STRING		= "%-24s:\t%s";
	public static final String		ATTRIB_INT			= "%-24s:\t%d";
	public static final String		ATTRIB_HEX_BYTE		= "%-24s:\t0x%02X";
	public static final String		ATTRIB_HEX_INT		= "%-24s:\t0x%08X";
	public static final String		ATTRIB_FLOAT_8		= "%-24s:\t%8f";
	
	public static final String		ATTRIB_L			= "%-48s:\t";
	
	public static final String		ATTRIB_L_STRING		= "%-48s:\t%s";
	public static final String		ATTRIB_L_INT		= "%-48s:\t%d";
	public static final String		ATTRIB_L_HEX_BYTE	= "%-48s:\t0x%02X";
	public static final String		ATTRIB_L_HEX_INT	= "%-48s:\t0x%08X";
	public static final String		ATTRIB_L_FLOAT_8	= "%-48s:\t%8f";
	
	private static String			separator			= "_____________________________________________________________________________________________";
	
	private static PrintWriter		out					= new PrintWriter(System.out);
	
	
	private GLDebug() {
	}
	
	public static void indent() {
		indent(1);
	}
	
	public static void unindent() {
		indent(-1);
	}
	
	public static void indent(int i) {
		indent = Math.max(0, indent + i);
	}
	
	public static void unindent(int i) {
		indent = Math.max(0, indent - i);
	}
	
	public static void setIndent(int i) {
		lastindent = indent;
		indent = Math.max(0, i);
	}
	
	public static void setPad(int i) {
		lastpad = pad;
		pad = Math.max(0, i);
	}
	
	public static void unsetPad() {
		pad = lastpad;
	}
	
	public static void unsetIndent() {
		indent = lastindent;
	}
	
	private static void writeIndent() {
		String s = "";
		for (int i = 0; i < indent; i++)
			s += '\t';
		out.write(s);
	}
	
	public static void write(String string) {
		writeIndent();
		out.write(string);
		out.write("\n");
		out.flush();
	}
	
	public static void writef(String format, Object... args) {
		writeIndent();
		out.write(String.format(format, args));
		out.write("\n");
		out.flush();
	}
	
	public static void write(Object o) {
		String[] text = o.toString().split("\n");
		for (String string : text)
			write(string);
	}
	
	public static void separator() {
		setIndent(0);
		write(separator);
		unsetIndent();
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
		write(separator);
		write("ERROR:");
		indent();
		write(error);
		indent();
		if (obj != null)
			obj.debugQuery();
		unindent();
		write(separator);
		logException(e);
		write(separator);
		unsetIndent();
	}
	
	public static void logException(Exception e) {
		write(e.getMessage());
		indent();
		for (StackTraceElement element : e.getStackTrace())
			write(element.toString());
		unindent();
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
	
	@Deprecated
	public static String fixedString(String s) {
		return fixedString(s, pad);
	}
	
	@Deprecated
	public static String fixedString(String s, int length) {
		if (s.length() >= length)
			return s;
		return String.format("%-" + length + "s", s);
	}
	
	@Deprecated
	public static String logMessage(String message, int indent) {
		String f = "            \t";
		for (int i = 0; i < indent; i++)
			f += '\t';
		f += "%s";
		return String.format(f, message);
	}
	
	@Deprecated
	public static String logMessage(String pre, String message, int indent) {
		String f = "%-12s\t";
		for (int i = 0; i < indent; i++)
			f += '\t';
		f += "%s";
		return String.format(f, pre, message);
	}
	
	public static void glObjError(Class<? extends GLObject> obj, String name, String error, String message) {
		glError(String.format("%s %s [%s]: %s.", error, obj, name, message), null);
	}
	
	public static void glObjError(GLObject obj, String error, String message) {
		glObjError(obj.getClass(), obj.name, error, message);
	}
	
	public static void flushErrors() {
		int err = GL11.glGetError();
		while (err != GL11.GL_NO_ERROR) {
			if (debug)
				write(GLU.gluErrorString(err));
			err = GL11.glGetError();
		}
	}
	
	public static void flushErrorsV() {
		int err = GL11.glGetError();
		if (err == GL11.GL_NO_ERROR)
			write("No Error");
		while (err != GL11.GL_NO_ERROR) {
			if (debug)
				write(GLU.gluErrorString(err));
			err = GL11.glGetError();
		}
	}
	
}
