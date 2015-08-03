package globj.objects.shaders;


import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.jdt.annotation.Nullable;



@NonNullByDefault
public class ShaderAttribute {
	private final String	name;
	private final int		location;
	
	public ShaderAttribute(String name, int location) {
		this.name = name;
		this.location = location;
	}
	
	/**
	 * @return the name of this attribute
	 */
	public String name() {
		return name;
	}
	
	/**
	 * @return the location of this attribute
	 */
	public int location() {
		return location;
	}
	
	@Override
	@Nullable
	public String toString() {
		return String.format("[%s] attribute @ location %d.", name, location);
	}
}
