package globj.objects.textures;


import globj.objects.textures.values.TextureFormat;
import globj.objects.textures.values.TextureTarget;
import globj.objects.textures.values.TextureWrap;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.lwjgl.opengl.GL11;



@NonNullByDefault
public abstract class GLTexture2D extends GLTexture {
	
	protected TextureWrap	sWrap	= TextureWrap.REPEAT;
	protected TextureWrap	tWrap	= TextureWrap.REPEAT;
	
	
	protected GLTexture2D(String name, TextureFormat texformat, TextureTarget target) {
		super(name, texformat, target);
	}
	
	/**
	 * Sets the wrap mode for each axis of the texture.
	 * 
	 * @param s
	 *            texture edge wrap mode on the s-axis.
	 * @param t
	 *            texture edge wrap mode on the t-axis.
	 */
	public void setWrap(TextureWrap s, TextureWrap t) {
		bind();
		GL11.glTexParameteri(target.value, GL11.GL_TEXTURE_WRAP_S, (sWrap = s).value);
		GL11.glTexParameteri(target.value, GL11.GL_TEXTURE_WRAP_T, (tWrap = t).value);
		undobind();
	}
	
}
