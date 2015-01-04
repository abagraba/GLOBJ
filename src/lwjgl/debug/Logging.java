package lwjgl.debug;

import lwjgl.core.GLException;
import lwjgl.core.GLObject;

public class Logging {
	
	public static void glWarning(String error) {
		System.err.println("WARNING:    " + error);
	}
	
	public static void glError(String error, GLObject obj) {
		try {
			throw new GLException(error, obj);
		} catch (GLException e) {
			System.err.println(Logging.logText("ERROR:", error, 0));
			if (obj != null)
				for (String err : obj.status())
					System.err.println(err);
			e.printStackTrace();
		}
	}
	
	public static void logObject(GLObject obj) {
		if (obj != null)
			for (String stat : obj.status())
				System.out.println(stat);
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
	
	public static String logText(String message, int indent) {
		String f = "            \t";
		for (int i = 0; i < indent; i++)
			f += '\t';
		f += "%s";
		return String.format(f, message);
	}

	public static String logText(String pre, String message, int indent) {
		String f = "%-12s\t";
		for (int i = 0; i < indent; i++)
			f += '\t';
		f += "%s";
		return String.format(f, pre, message);
	}
	
	public static void globjError(Class<? extends GLObject> obj, String name, String error, String message){
		glError(String.format("%s %s [%s]: %s.", error, obj, name, message), null);
	}
}
