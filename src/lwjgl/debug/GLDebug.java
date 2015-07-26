package lwjgl.debug;

import globj.core.GLException;
import globj.objects.GLObject;

public class GLDebug {
	
	private static int indent = 0;
	private static int lastindent = 0;
	private static int pad = 24;
	private static int lastpad = 24;
	
	public static void indent() {
		indent(1);
	}
	
	public static void unindent() {
		indent(-1);
	}
	
	public static void indent(int i) {
		indent = Math.max(0, indent + i);
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
	
	public static void write(String string) {
		String s = "";
		for (int i = 0; i < indent; i++)
			s += '\t';
		System.out.println(s + string);
	}
	
	public static void write(Object o) {
		write(o.toString());
	}
	
	private static String separator = "_____________________________________________________________________________________________";
	
	public static void separator() {
		write(separator);
	}
	
	public static void glWarning(String error) {
		setIndent(0);
		write("WARNING:");
		indent();
		write(error);
		unsetIndent();
	}
	
	public static void glError(String error, GLObject obj) {
		GLException e = new GLException(error, obj);
		setIndent(0);
		write("ERROR:");
		indent();
		write(error);
		indent();
		if (obj != null)
			obj.debugQuery();
		unsetIndent();
		e.printStackTrace();
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
	
	public static String fixedString(String s) {
		return fixedString(s, pad);
	}
	
	public static String fixedString(String s, int length) {
		if (s.length() >= length)
			return s;
		return String.format("%-" + length + "s", s);
	}
	
	public static String logMessage(String message, int indent) {
		String f = "            \t";
		for (int i = 0; i < indent; i++)
			f += '\t';
		f += "%s";
		return String.format(f, message);
	}
	
	public static String logMessage(String pre, String message, int indent) {
		String f = "%-12s\t";
		for (int i = 0; i < indent; i++)
			f += '\t';
		f += "%s";
		return String.format(f, pre, message);
	}
	
	public static void globjError(Class<? extends GLObject> obj, String name, String error, String message) {
		glError(String.format("%s %s [%s]: %s.", error, obj, name, message), null);
	}
	
	public static void globjError(GLObject obj, String error, String message) {
		globjError(obj.getClass(), obj.name, error, message);
	}
	
}
