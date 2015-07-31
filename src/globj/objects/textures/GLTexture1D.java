package globj.objects.textures;


import globj.objects.textures.values.TextureFormat;
import globj.objects.textures.values.TextureTarget;
import globj.objects.textures.values.TextureWrapMode;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.lwjgl.opengl.GL11;



@NonNullByDefault
public abstract class GLTexture1D extends GLTexture {
	
	protected TextureWrapMode	sWrap	= TextureWrapMode.REPEAT;
	
	
	protected GLTexture1D(String name, TextureFormat texformat, TextureTarget target) {
		super(name, texformat, target);
	}
	
	/**
	 * Sets the wrap mode for each axis of the texture.
	 * 
	 * @param s
	 *            texture edge wrap mode on the s-axis.
	 */
	public void setWrap(TextureWrapMode s) {
		bind();
		GL11.glTexParameteri(target.value(), GL11.GL_TEXTURE_WRAP_S, (sWrap = s).value());
		undobind();
	}
	
}
