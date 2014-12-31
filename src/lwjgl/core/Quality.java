package lwjgl.core;

import lwjgl.core.texture.QualityHint;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL14;
import org.lwjgl.opengl.GL20;

public class Quality {

	public static void genMipmapQuality(QualityHint hint) {
		GL11.glHint(GL14.GL_GENERATE_MIPMAP_HINT, hint.value);
	}

	public static void pointSmoothQuality(QualityHint hint) {
		GL11.glHint(GL11.GL_POINT_SMOOTH_HINT, hint.value);
	}

	public static void lineSmoothQuality(QualityHint hint) {
		GL11.glHint(GL11.GL_LINE_SMOOTH_HINT, hint.value);
	}
	
	public static void polygonSmoothQuality(QualityHint hint) {
		GL11.glHint(GL11.GL_POLYGON_SMOOTH_HINT, hint.value);
	}

	public static void perspectiveCorrectionQuality(QualityHint hint) {
		GL11.glHint(GL11.GL_PERSPECTIVE_CORRECTION_HINT, hint.value);
	}

	public static void fogQuality(QualityHint hint) {
		GL11.glHint(GL11.GL_FOG_HINT, hint.value);
	}

	public static void textureCompressionQuality(QualityHint hint) {
		GL11.glHint(GL13.GL_TEXTURE_COMPRESSION_HINT, hint.value);
	}

	public static void fragDerivativeQuality(QualityHint hint) {
		GL11.glHint(GL20.GL_FRAGMENT_SHADER_DERIVATIVE_HINT, hint.value);
	}

}
