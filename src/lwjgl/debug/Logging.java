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
			System.err.println("ERROR:      " + error);
			e.printStackTrace();
		}
	}
	
}
