package globj.objects.textures.values;


import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.jdt.annotation.Nullable;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL14;
import org.lwjgl.opengl.GL21;
import org.lwjgl.opengl.GL30;
import org.lwjgl.opengl.GL31;
import org.lwjgl.opengl.GL33;
import org.lwjgl.opengl.GL42;



@NonNullByDefault
public enum TextureFormat {
	RGBA2("RGBA 2 bit depth: Unsigned normalized integer", GL11.GL_RGBA2, GL11.GL_RGBA),
	RGB4("RGB 4 bit depth: Unsigned normalized integer", GL11.GL_RGB4, GL11.GL_RGB),
	RGBA4("RGBA 4 bit depth: Unsigned normalized integer", GL11.GL_RGBA4, GL11.GL_RGBA),
	RGB5("RGB 5 bit depth: Unsigned normalized integer", GL11.GL_RGB5, GL11.GL_RGB),
	R8("R 8 bit depth: Unsigned normalized integer", GL30.GL_R8, GL11.GL_RED),
	RG8("RG 8 bit depth: Unsigned normalized integer", GL30.GL_RG8, GL30.GL_RG),
	RGB8("RGB 8 bit depth: Unsigned normalized integer", GL11.GL_RGB8, GL11.GL_RGB),
	RGBA8("RGBA 8 bit depth: Unsigned normalized integer", GL11.GL_RGBA8, GL11.GL_RGBA),
	RGB10("RGB 10 bit depth: Unsigned normalized integer", GL11.GL_RGB10, GL11.GL_RGB),
	RGB12("RGB 12 bit depth: Unsigned normalized integer", GL11.GL_RGB12, GL11.GL_RGB),
	RGBA12("RGBA 12 bit depth: Unsigned normalized integer", GL11.GL_RGBA12, GL11.GL_RGBA),
	R16("R 16 bit depth: Unsigned normalized integer", GL30.GL_R16, GL11.GL_RED),
	RG16("RG 16 bit depth: Unsigned normalized integer", GL30.GL_RG16, GL30.GL_RG),
	RGB16("RGB 16 bit depth: Unsigned normalized integer", GL11.GL_RGB16, GL11.GL_RGB),
	RGBA16("RGBA 16 bit depth: Unsigned normalized integer", GL11.GL_RGBA16, GL11.GL_RGBA),
	R8_SNORM("R 8 bit depth: Signed normalized integer", GL31.GL_R8_SNORM, GL11.GL_RED),
	RG8_SNORM("RG 8 bit depth: Signed normalized integer", GL31.GL_RG8_SNORM, GL30.GL_RG),
	RGB8_SNORM("RGB 8 bit depth: Signed normalized integer", GL31.GL_RGB8_SNORM, GL11.GL_RGB),
	RGBA8_SNORM("RGBA 8 bit depth: Signed normalized integer", GL31.GL_RGBA8_SNORM, GL11.GL_RGBA),
	R16_SNORM("R 16 bit depth: Signed normalized integer", GL31.GL_R16_SNORM, GL11.GL_RED),
	RG16_SNORM("RG 16 bit depth: Signed normalized integer", GL31.GL_RG16_SNORM, GL30.GL_RG),
	RGB16_SNORM("RGB 16 bit depth: Signed normalized integer", GL31.GL_RGB16_SNORM, GL11.GL_RGB),
	RGBA16_SNORM("RGBA 16 bit depth: Signed normalized integer", GL31.GL_RGBA16_SNORM, GL11.GL_RGBA),
	R8_UI("R 8 bit depth: Unsigned integer", GL30.GL_R8UI, GL30.GL_RED_INTEGER),
	RG8_UI("RG 8 bit depth: Unsigned integer", GL30.GL_RG8UI, GL30.GL_RG_INTEGER),
	RGB8_UI("RGB 8 bit depth: Unsigned integer", GL30.GL_RGB8UI, GL30.GL_RGB_INTEGER),
	RGBA8_UI("RGBA 8 bit depth: Unsigned integer", GL30.GL_RGBA8UI, GL30.GL_RGBA_INTEGER),
	R16_UI("R 16 bit depth: Unsigned integer", GL30.GL_R16UI, GL30.GL_RED_INTEGER),
	RG16_UI("RG 16 bit depth: Unsigned integer", GL30.GL_RG16UI, GL30.GL_RG_INTEGER),
	RGB16_UI("RGB 16 bit depth: Unsigned integer", GL30.GL_RGB16UI, GL30.GL_RGB_INTEGER),
	RGBA16_UI("RGBA 16 bit depth: Unsigned integer", GL30.GL_RGBA16UI, GL30.GL_RGBA_INTEGER),
	R32_UI("R 32 bit depth: Unsigned integer", GL30.GL_R32UI, GL30.GL_RED_INTEGER),
	RG32_UI("RG 32 bit depth: Unsigned integer", GL30.GL_RG32UI, GL30.GL_RG_INTEGER),
	RGB32_UI("RGB 32 bit depth: Unsigned integer", GL30.GL_RGB32UI, GL30.GL_RGB_INTEGER),
	RGBA32_UI("RGBA 32 bit depth: Unsigned integer", GL30.GL_RGBA32UI, GL30.GL_RGBA_INTEGER),
	R8_I("R 8 bit depth: Signed integer", GL30.GL_R8I, GL30.GL_RED_INTEGER),
	RG8_I("RG 8 bit depth: Signed integer", GL30.GL_RG8I, GL30.GL_RG_INTEGER),
	RGB8_I("RGB 8 bit depth: Signed integer", GL30.GL_RGB8I, GL30.GL_RGB_INTEGER),
	RGBA8_I("RGBA 8 bit depth: Signed integer", GL30.GL_RGBA8I, GL30.GL_RGBA_INTEGER),
	R16_I("R 16 bit depth: Signed integer", GL30.GL_R16I, GL30.GL_RED_INTEGER),
	RG16_I("RG 16 bit depth: Signed integer", GL30.GL_RG16I, GL30.GL_RG_INTEGER),
	RGB16_I("RGB 16 bit depth: Signed integer", GL30.GL_RGB16I, GL30.GL_RGB_INTEGER),
	RGBA16_I("RGBA 16 bit depth: Signed integer", GL30.GL_RGBA16I, GL30.GL_RGBA_INTEGER),
	R32_I("R 32 bit depth: Signed integer", GL30.GL_R32I, GL30.GL_RED_INTEGER),
	RG32_I("RG 32 bit depth: Signed integer", GL30.GL_RG32I, GL30.GL_RG_INTEGER),
	RGB32_I("RGB 32 bit depth: Signed integer", GL30.GL_RGB32I, GL30.GL_RGB_INTEGER),
	RGBA32_I("RGBA 32 bit depth: Signed integer", GL30.GL_RGBA32I, GL30.GL_RGBA_INTEGER),
	R16_F("R 16 bit depth: Float", GL30.GL_R16F, GL11.GL_RED),
	RG16_F("RG 16 bit depth: Float", GL30.GL_RG16F, GL30.GL_RG),
	RGB16_F("RGB 16 bit depth: Float", GL30.GL_RGB16F, GL11.GL_RGB),
	RGBA16_F("RGBA 16 bit depth: Float", GL30.GL_RGBA16F, GL11.GL_RGBA),
	R32_F("R 32 bit depth: Float", GL30.GL_R32F, GL11.GL_RED),
	RG32_F("RG 32 bit depth: Float", GL30.GL_RG32F, GL30.GL_RG),
	RGB32_F("RGB 32 bit depth: Float", GL30.GL_RGB32F, GL11.GL_RGB),
	RGBA32_F("RGBA 32 bit depth: Float", GL30.GL_RGBA32F, GL11.GL_RGBA),
	RG3B2("RGB 3 3 2 bit depth: Unsigned normalized integer", GL11.GL_R3_G3_B2, GL11.GL_RGB),
	RGB5A1("RGBA 5 5 5 1 bit depth: Unsigned normalized integer", GL11.GL_RGB5_A1, GL11.GL_RGBA),
	RGB10A2("RGBA 10 10 10 2 bit depth: Unsigned normalized integer", GL11.GL_RGB10_A2, GL11.GL_RGBA),
	RGB10A2_UI("RGBA 10 10 10 2 bit depth: Unsigned integer", GL33.GL_RGB10_A2UI, GL30.GL_RGBA_INTEGER),
	RG11B10_F("RGB 11 11 10 bit depth: Float", GL30.GL_R11F_G11F_B10F, GL11.GL_RGB),
	RGB9E5("RGBE 9 9 9 5 bit depth: Exponent", GL30.GL_RGB9_E5, GL11.GL_RGB),
	SRGB8("sRGB 8 bit depth", GL21.GL_SRGB8, GL11.GL_RGB),
	SRGBA8("sRGB 8 bit depth: Linear 8 bit alpha", GL21.GL_SRGB8_ALPHA8, GL11.GL_RGBA),
	
	// Compressed Formats cannot be used in RECTANGLE or ARRAY_1D targets.
	COMPRESSED_RGTC1("1 Component RGTC Compressed: Unsigned normalized integer", GL30.GL_COMPRESSED_RED_RGTC1, GL11.GL_RED),
	COMPRESSED_RGTC1_SIGNED("1 Component RGTC Compressed: Signed normalized integer", GL30.GL_COMPRESSED_SIGNED_RED_RGTC1, GL11.GL_RED),
	COMPRESSED_RGTC2("2 Component RGTC Compressed: Unsigned normalized integer", GL30.GL_COMPRESSED_RG_RGTC2, GL30.GL_RG),
	COMPRESSED_RGTC2_SIGNED("2 Component RGTC Compressed: Signed normalized integer", GL30.GL_COMPRESSED_SIGNED_RG_RGTC2, GL30.GL_RG),
	COMPRESSED_RGBA_BTPC_UNORM("RGBA BPTC Compressed: Unsigned normalized integer", GL42.GL_COMPRESSED_RGBA_BPTC_UNORM, GL11.GL_RGBA),
	COMPRESSED_SRGBA_BTPC_UNORM("sRGB BPTC Compressed: Unsigned normalized integer", GL42.GL_COMPRESSED_SRGB_ALPHA_BPTC_UNORM, GL11.GL_RGBA),
	COMPRESSED_RGB_BTPC_F("RGB BPTC Compressed: Signed float", GL42.GL_COMPRESSED_RGB_BPTC_SIGNED_FLOAT, GL11.GL_RGB),
	COMPRESSED_RGB_BTPC_F_UNSIGNED("RGB BPTC Compressed: Unsigned float", GL42.GL_COMPRESSED_RGB_BPTC_UNSIGNED_FLOAT, GL11.GL_RGB),
	
	D16("16 bit Depth", GL14.GL_DEPTH_COMPONENT16, GL11.GL_DEPTH_COMPONENT, true, false),
	D24("24 bit Depth", GL14.GL_DEPTH_COMPONENT24, GL11.GL_DEPTH_COMPONENT, true, false),
	D32("32 bit Depth", GL14.GL_DEPTH_COMPONENT32, GL11.GL_DEPTH_COMPONENT, true, false),
	D32_F("32 bit Depth: Float", GL30.GL_DEPTH_COMPONENT32F, GL11.GL_DEPTH_COMPONENT, true, false),
	// D24S8("24 bit Depth, 8 bit Stencil", GL30.GL_DEPTH24_STENCIL8, GL30.GL_DEPTH_STENCIL,
	// true, true),
	// D32S8_F("32 bit Depth, 8 bit Stencil: Float", GL30.GL_DEPTH32F_STENCIL8,
	// GL30.GL_DEPTH_STENCIL, true, true),
	// S1("1 bit Stencil", GL30.GL_STENCIL_INDEX1, GL11.GL_STENCIL_INDEX),
	// S4("4 bit Stencil", GL30.GL_STENCIL_INDEX4, GL11.GL_STENCIL_INDEX),
	S8("8 bit Stencil", GL30.GL_STENCIL_INDEX8, GL11.GL_STENCIL_INDEX, false, true),
	// S16("16 bit Stencil", GL30.GL_STENCIL_INDEX16, GL11.GL_STENCIL_INDEX)
	;
	
	private final String name;
	private final int value;
	private final int base;
	private final boolean depth;
	private final boolean stencil;
	
	
	private TextureFormat(String name, int value, int base) {
		this(name, value, base, false, false);
	}
	
	private TextureFormat(String name, int value, int base, boolean depth, boolean stencil) {
		this.name = name;
		this.value = value;
		this.base = base;
		this.depth = depth;
		this.stencil = stencil;
	}
	
	/**
	 * @param glInt
	 *            the GLint representing a texture format
	 * @return the TextureFormat object represented by glInt
	 */
	@Nullable
	public static TextureFormat get(int glInt) {
		for (TextureFormat format : values())
			if (format.value == glInt)
				return format;
		return null;
	}
	
	/**
	 * @return the name of this texture format
	 */
	public String formatName() {
		return name;
	}
	
	/**
	 * @return the GLint representing this texture format
	 */
	public int value() {
		return value;
	}
	
	/**
	 * @return the GLint representing the base format of this texture format
	 */
	public int base() {
		return base;
	}
	
	/**
	 * @return whether this texture format has a depth component
	 */
	public boolean depth() {
		return depth;
	}
	
	/**
	 * @return whether this texture format has a stencil component
	 */
	public boolean stencil() {
		return stencil;
	}
	
	@Override
	public String toString() {
		return name;
	}
	
}
