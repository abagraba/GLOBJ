package lwjgl.core.texture;

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

public class ImageUtil {
	
	private static Color clear = new Color(0f, 0f, 0f, 0f);
	
	private static ColorModel model = new ComponentColorModel(ColorSpace.getInstance(ColorSpace.CS_sRGB), new int[] { 8, 8, 8, 0 }, false, false,
			ComponentColorModel.OPAQUE, DataBuffer.TYPE_BYTE);
	
	private static ColorModel modelA = new ComponentColorModel(ColorSpace.getInstance(ColorSpace.CS_sRGB), new int[] { 8, 8, 8, 8 }, true, false,
			ComponentColorModel.TRANSLUCENT, DataBuffer.TYPE_BYTE);
	
	public static ByteBuffer imageRGBData(BufferedImage image) {
		WritableRaster raster = Raster.createInterleavedRaster(DataBuffer.TYPE_BYTE, image.getWidth(), image.getHeight(), 3, null);
		@SuppressWarnings("rawtypes")
		BufferedImage formatted = new BufferedImage(model, raster, false, new Hashtable());
		
		Graphics g = formatted.getGraphics();
		g.setColor(clear);
		g.fillRect(0, 0, image.getWidth(), image.getHeight());
		g.drawImage(image, 0, 0, null);
		
		byte[] data = ((DataBufferByte) formatted.getRaster().getDataBuffer()).getData();
		ByteBuffer buffer = ByteBuffer.allocateDirect(data.length);
		buffer.order(ByteOrder.nativeOrder());
		buffer.put(data, 0, data.length).flip();
		return buffer;
	}

	public static ByteBuffer imageRGBAData(BufferedImage image) {
		WritableRaster raster = Raster.createInterleavedRaster(DataBuffer.TYPE_BYTE, image.getWidth(), image.getHeight(), 4, null);
		@SuppressWarnings("rawtypes")
		BufferedImage formatted = new BufferedImage(modelA, raster, false, new Hashtable());
		
		Graphics g = formatted.getGraphics();
		g.setColor(clear);
		g.fillRect(0, 0, image.getWidth(), image.getHeight());
		g.drawImage(image, 0, 0, null);
		
		byte[] data = ((DataBufferByte) formatted.getRaster().getDataBuffer()).getData();
		ByteBuffer buffer = ByteBuffer.allocateDirect(data.length);
		buffer.order(ByteOrder.nativeOrder());
		buffer.put(data, 0, data.length).flip();
		return buffer;
	}

	
}
