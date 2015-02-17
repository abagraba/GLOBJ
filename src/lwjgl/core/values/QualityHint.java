package lwjgl.core.values;

import org.lwjgl.opengl.GL11;

public enum QualityHint {
	NICEST("Nicest", GL11.GL_NICEST), FASTEST("Fastest", GL11.GL_FASTEST), DONT_CARE("Don't Care", GL11.GL_DONT_CARE);
	
	public final String name;
	public final int value;
	
	private QualityHint(String name, int value) {
		this.name = name;
		this.value = value;
	}
	
	public static QualityHint get(int i) {
		for (QualityHint hint : values())
			if (hint.value == i)
				return hint;
		return null;
	}
	
	@Override
	public String toString() {
		return name;
	}
	
}
