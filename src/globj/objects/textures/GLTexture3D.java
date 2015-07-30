package globj.objects.textures;


import globj.objects.textures.values.TextureFormat;
import globj.objects.textures.values.TextureTarget;
import globj.objects.textures.values.TextureWrap;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;



@NonNullByDefault
public abstract class GLTexture3D extends GLTexture {
	protected TextureWrap	sWrap	= TextureWrap.REPEAT;
	protected TextureWrap	tWrap	= TextureWrap.REPEAT;
	protected TextureWrap	rWrap	= TextureWrap.REPEAT;
	
	
	protected GLTexture3D(String name, TextureFormat texformat, TextureTarget target) {
		super(name, texformat, target);
	}
	
	/**
	 * Sets the wrap mode for each axis of the texture.
	 * 
	 * @param s
	 *            texture edge wrap mode on the s-axis.
	 * @param t
	 *            texture edge wrap mode on the t-axis.
	 * @param r
	 *            texture edge wrap mode on the r-axis.
	 */
	public void setWrap(TextureWrap s, TextureWrap t, TextureWrap r) {
		bind();
		GL11.glTexParameteri(target.value, GL11.GL_TEXTURE_WRAP_S, (sWrap = s).value);
		GL11.glTexParameteri(target.value, GL11.GL_TEXTURE_WRAP_T, (tWrap = t).value);
		GL11.glTexParameteri(target.value, GL12.GL_TEXTURE_WRAP_R, (rWrap = r).value);
		undobind();
	}
	
}
