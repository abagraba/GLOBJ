package lwjgl.core.texture;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL14;
import org.lwjgl.opengl.GL21;
import org.lwjgl.opengl.GL30;
import org.lwjgl.opengl.GL31;
import org.lwjgl.opengl.GL33;
import org.lwjgl.opengl.GL42;

public enum TextureFormat {
	R("R", GL11.GL_R),
	RG("RG", GL30.GL_RG),
	RGB("RGB", GL11.GL_RGB),
	RGBA("RGBA", GL11.GL_RGBA),
	SRGB("sRGB", GL21.GL_SRGB),
	SRGBA("sRGBA", GL21.GL_SRGB_ALPHA),
	
	COMPRESSED_R("Compressed R", GL30.GL_COMPRESSED_RED),
	COMPRESSED_RG("Compressed RG", GL30.GL_COMPRESSED_RG),
	
	RGBA2("RGBA 2 bit depth: Unsigned normalized integer", GL11.GL_RGBA2),
	RGB4("RGB 4 bit depth: Unsigned normalized integer", GL11.GL_RGB4),
	RGBA4("RGBA 4 bit depth: Unsigned normalized integer", GL11.GL_RGBA4),
	RGB5("RGB 5 bit depth: Unsigned normalized integer", GL11.GL_RGB5),
	R8("R 8 bit depth: Unsigned normalized integer", GL30.GL_R8),
	RG8("RG 8 bit depth: Unsigned normalized integer", GL30.GL_RG8),
	RGB8("RGB 8 bit depth: Unsigned normalized integer", GL11.GL_RGB8),
	RGBA8("RGBA 8 bit depth: Unsigned normalized integer", GL11.GL_RGBA8),
	RGB10("RGB 10 bit depth: Unsigned normalized integer", GL11.GL_RGB10),
	RGB12("RGB 12 bit depth: Unsigned normalized integer", GL11.GL_RGB12),
	RGBA12("RGBA 12 bit depth: Unsigned normalized integer", GL11.GL_RGBA12),
	R16("R 16 bit depth: Unsigned normalized integer", GL30.GL_R16),
	RG16("RG 16 bit depth: Unsigned normalized integer", GL30.GL_RG16),
	RGB16("RGB 16 bit depth: Unsigned normalized integer", GL11.GL_RGB16),
	RGBA16("RGBA 16 bit depth: Unsigned normalized integer", GL11.GL_RGBA16),
	R8_SNORM("R 8 bit depth: Signed normalized integer", GL31.GL_R8_SNORM),
	RG8_SNORM("RG 8 bit depth: Signed normalized integer", GL31.GL_RG8_SNORM),
	RGB8_SNORM("RGB 8 bit depth: Signed normalized integer", GL31.GL_RGB8_SNORM),
	RGBA8_SNORM("RGBA 8 bit depth: Signed normalized integer", GL31.GL_RGBA8_SNORM),
	R16_SNORM("R 16 bit depth: Signed normalized integer", GL31.GL_R16_SNORM),
	RG16_SNORM("RG 16 bit depth: Signed normalized integer", GL31.GL_RG16_SNORM),
	RGB16_SNORM("RGB 16 bit depth: Signed normalized integer", GL31.GL_RGB16_SNORM),
	RGBA16_SNORM("RGBA 16 bit depth: Signed normalized integer", GL31.GL_RGBA16_SNORM),
	R8_UI("R 8 bit depth: Unsigned integer", GL30.GL_R8UI),
	RG8_UI("RG 8 bit depth: Unsigned integer", GL30.GL_RG8UI),
	RGB8_UI("RGB 8 bit depth: Unsigned integer", GL30.GL_RGB8UI),
	RGBA8_UI("RGBA 8 bit depth: Unsigned integer", GL30.GL_RGBA8UI),
	R16_UI("R 16 bit depth: Unsigned integer", GL30.GL_R16UI),
	RG16_UI("RG 16 bit depth: Unsigned integer", GL30.GL_RG16UI),
	RGB16_UI("RGB 16 bit depth: Unsigned integer", GL30.GL_RGB16UI),
	RGBA16_UI("RGBA 16 bit depth: Unsigned integer", GL30.GL_RGBA16UI),
	R32_UI("R 16 bit depth: Unsigned integer", GL30.GL_R32UI),
	RG32_UI("RG 16 bit depth: Unsigned integer", GL30.GL_RG32UI),
	RGB32_UI("RGB 16 bit depth: Unsigned integer", GL30.GL_RGB32UI),
	RGBA32_UI("RGBA 16 bit depth: Unsigned integer", GL30.GL_RGBA32UI),
	R8_I("R 8 bit depth: Signed integer", GL30.GL_R8I),
	RG8_I("RG 8 bit depth: Signed integer", GL30.GL_RG8I),
	RGB8_I("RGB 8 bit depth: Signed integer", GL30.GL_RGB8I),
	RGBA8_I("RGBA 8 bit depth: Signed integer", GL30.GL_RGBA8I),
	R16_I("R 16 bit depth: Signed integer", GL30.GL_R16I),
	RG16_I("RG 16 bit depth: Signed integer", GL30.GL_RG16I),
	RGB16_I("RGB 16 bit depth: Signed integer", GL30.GL_RGB16I),
	RGBA16_I("RGBA 16 bit depth: Signed integer", GL30.GL_RGBA16I),
	R32_I("R 32 bit depth: Signed integer", GL30.GL_R32I),
	RG32_I("RG 32 bit depth: Signed integer", GL30.GL_RG32I),
	RGB32_I("RGB 32 bit depth: Signed integer", GL30.GL_RGB32I),
	RGBA32_I("RGBA 32 bit depth: Signed integer", GL30.GL_RGBA32I),
	R16_F("R 16 bit depth: Float", GL30.GL_R16F),
	RG16_F("RG 16 bit depth: Float", GL30.GL_RG16F),
	RGB16_F("RGB 16 bit depth: Float", GL30.GL_RGB16F),
	RGBA16_F("RGBA 16 bit depth: Float", GL30.GL_RGBA16F),
	R32_F("R 32 bit depth: Float", GL30.GL_R32F),
	RG32_F("RG 32 bit depth: Float", GL30.GL_RG32F),
	RGB32_F("RGB 32 bit depth: Float", GL30.GL_RGB32F),
	RGBA32_F("RGBA 32 bit depth: Float", GL30.GL_RGBA32F),
	RG3B2("RGB 3 3 2 bit depth: Unsigned normalized integer", GL11.GL_R3_G3_B2),
	RGB5A1("RGBA 5 5 5 1 bit depth: Unsigned normalized integer", GL11.GL_RGB5_A1),
	RGB10A2("RGBA 10 10 10 2 bit depth: Unsigned normalized integer", GL11.GL_RGB10_A2),
	RGB10A2_UI("RGBA 10 10 10 2 bit depth: Unsigned integer", GL33.GL_RGB10_A2UI),
	RG11B10_F("RGB 11 11 10 bit depth: Float", GL30.GL_R11F_G11F_B10F),
	RGB9E5("RGBE 9 9 9 5 bit depth: Exponent", GL30.GL_RGB9_E5),
	SRGB8("sRGB 8 bit depth", GL21.GL_SRGB8),
	SRGBA8("sRGB 8 bit depth: Linear 8 bit alpha", GL21.GL_SRGB8_ALPHA8),
	COMPRESSED_RGTC1("1 Component RGTC Compressed: Unsigned normalized integer", GL30.GL_COMPRESSED_RED_RGTC1),
	COMPRESSED_RGTC1_SIGNED("1 Component RGTC Compressed: Signed normalized integer", GL30.GL_COMPRESSED_SIGNED_RED_RGTC1),
	COMPRESSED_RGTC2("2 Component RGTC Compressed: Unsigned normalized integer", GL30.GL_COMPRESSED_RG_RGTC2),
	COMPRESSED_RGTC2_SIGNED("2 Component RGTC Compressed: Signed normalized integer", GL30.GL_COMPRESSED_SIGNED_RG_RGTC2),
	COMPRESSED_RGBA_BTPC_UNORM("RGBA BPTC Compressed: Unsigned normalized integer", GL42.GL_COMPRESSED_RGBA_BPTC_UNORM),
	COMPRESSED_SRGBA_BTPC_UNORM("sRGB BPTC Compressed: Unsigned normalized integer", GL42.GL_COMPRESSED_SRGB_ALPHA_BPTC_UNORM),
	COMPRESSED_RGB_BTPC_F("RGB BPTC Compressed: Signed float", GL42.GL_COMPRESSED_RGB_BPTC_SIGNED_FLOAT),
	COMPRESSED_RGB_BTPC_F_UNSIGNED("RGB BPTC Compressed: Unsigned float", GL42.GL_COMPRESSED_RGB_BPTC_UNSIGNED_FLOAT),
	
	D16("16 bit Depth", GL14.GL_DEPTH_COMPONENT16, true),
	D24("24 bit Depth", GL14.GL_DEPTH_COMPONENT24, true),
	D32("32 bit Depth", GL14.GL_DEPTH_COMPONENT32, true),
	D32_F("32 bit Depth: Float", GL30.GL_DEPTH_COMPONENT32F, true),
	D24S8("24 bit Depth, 8 bit Stencil", GL30.GL_DEPTH24_STENCIL8, true),
//	D32S8_F("32 bit Depth, 8 bit Stencil: Float", GL42.GL_DEPTH32_STENCIL8),
	S1("1 bit Stencil", GL30.GL_STENCIL_INDEX1),
	S4("4 bit Stencil", GL30.GL_STENCIL_INDEX4),
	S8("8 bit Stencil", GL30.GL_STENCIL_INDEX8),
	S16("16 bit Stencil", GL30.GL_STENCIL_INDEX16);
	
	public final String name;
	public final int value;
	public final boolean depth;
	
	private TextureFormat(String name, int value) {
		this(name, value, false);
	}
	
	private TextureFormat(String name, int value, boolean depth) {
		this.name = name;
		this.value = value;
		this.depth = depth;
	}

	public static TextureFormat get(int i){
		for (TextureFormat format : values()) 
			if (format.value == i)
				return format;
		return null;
	}
	
	public String toString() {
		return name;
	}
	
}
