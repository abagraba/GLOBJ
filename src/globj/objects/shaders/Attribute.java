package globj.objects.shaders;

public class Attribute {
	public final String name;
	public final int location;
	
	protected Attribute(String name, int location){
		this.name = name;
		this.location = location;
	}
	
	@Override
	public String toString(){
		return String.format("[%s] attribute @ location %d.", name, location);
	}
	
}
