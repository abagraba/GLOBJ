package globj.core;


import org.lwjgl.opengl.GL11;



public enum QualityHint {
	NICEST("Nicest", GL11.GL_NICEST),
	FASTEST("Fastest", GL11.GL_FASTEST),
	DONT_CARE("Don't Care", GL11.GL_DONT_CARE);
	
	private final String name;
	private final int value;
	
	private QualityHint(String name, int value) {
		this.name = name;
		this.value = value;
	}
	
	/**
	 * @param glInt
	 *            the GLint representing a quality hint
	 * @return the QualityHint object represented by glInt
	 */
	public static QualityHint get(int i) {
		for (QualityHint hint : values())
			if (hint.value == i)
				return hint;
		return null;
	}
	
	/**
	 * @return the name of this quality hint
	 */
	public String hintName() {
		return name;
	}
	
	/**
	 * @return the GLint representing this quality hint
	 */
	public int value() {
		return value;
	}
	
	@Override
	public String toString() {
		return name;
	}
	
}
