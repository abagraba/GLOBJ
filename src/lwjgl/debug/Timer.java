package lwjgl.debug;

public class Timer {

	public final static Timer debug = new Timer();
	
	private long time = System.nanoTime();
	
	public void mark(){
		time = System.nanoTime();
	}
	
	public void measure(String message){
		long t = System.nanoTime();
		System.out.format("%s\t%.3f ms\n", message, (t - time)*0.000001f);
		time = t;
	}
	
	
	
}
