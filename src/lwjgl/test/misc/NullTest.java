package lwjgl.test.misc;


import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.jdt.annotation.Nullable;



@NonNullByDefault
public class NullTest {
	
	@Nullable
	String[] strings;
	
	
	public void print() {
		if (strings != null) {
			for (String string : strings) {
				System.out.println(string);
			}
		}
	}
	
}
