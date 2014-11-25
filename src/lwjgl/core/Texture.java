package lwjgl.core;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.color.ColorSpace;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.ComponentColorModel;
import java.awt.image.DataBuffer;
import java.awt.image.DataBufferByte;
import java.awt.image.Raster;
import java.awt.image.WritableRaster;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Hashtable;

import org.lwjgl.opengl.GL11;

public class Texture {

	private int texture;

	private static Color clear = new Color(0f, 0f, 0f, 0f);
	@SuppressWarnings("unused")
	private static ColorModel model = new ComponentColorModel(
			ColorSpace.getInstance(ColorSpace.CS_sRGB),
			new int[] { 8, 8, 8, 0 }, false, false, ComponentColorModel.OPAQUE,
			DataBuffer.TYPE_BYTE);

	private static ColorModel modelA = new ComponentColorModel(
			ColorSpace.getInstance(ColorSpace.CS_sRGB),
			new int[] { 8, 8, 8, 8 }, true, false,
			ComponentColorModel.TRANSLUCENT, DataBuffer.TYPE_BYTE);

	public Texture(BufferedImage src) {
		setData(src);
	}

	public Texture() {
	}

	public void setData(BufferedImage src) {
		if (texture == 0) {
			texture = GL11.glGenTextures();
		}
		use();
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER,
				GL11.GL_NEAREST);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER,
				GL11.GL_NEAREST);

		GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGBA, src.getWidth(),
				src.getHeight(), 0, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE,
				data(src));
	}

	public void use() {
		if (texture != 0)
			GL11.glBindTexture(GL11.GL_TEXTURE_2D, texture);
		else
			System.err.println("Cannot use texture. Texture contains no data or has been destroyed.");
	}

	public static void useNone() {
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, 0);
	}

	public void destroy() {
		if (texture != 0)
			GL11.glDeleteTextures(texture);
		else
			System.err.println("Cannot delete texture. Does not exist.");
		texture = 0;
	}

	public static ByteBuffer data(BufferedImage image) {
		WritableRaster raster = Raster.createInterleavedRaster(
				DataBuffer.TYPE_BYTE, image.getWidth(), image.getHeight(), 4,
				null);
		@SuppressWarnings("rawtypes")
		BufferedImage formatted = new BufferedImage(modelA, raster, false,
				new Hashtable());

		Graphics g = formatted.getGraphics();
		g.setColor(clear);
		g.fillRect(0, 0, image.getWidth(), image.getHeight());
		g.drawImage(image, 0, 0, null);

		byte[] data = ((DataBufferByte) formatted.getRaster().getDataBuffer())
				.getData();
		ByteBuffer buffer = ByteBuffer.allocateDirect(data.length);
		buffer.order(ByteOrder.nativeOrder());
		buffer.put(data, 0, data.length);
		buffer.flip();
		return buffer;
	}

	public boolean exists() {
		return texture != 0;
	}

}
