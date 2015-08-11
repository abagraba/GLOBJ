package lwjgl.test.ogldev;


import control.ControlManager;
import globj.core.RenderCommand;
import globj.core.Window;



public class Tutorial01 extends RenderCommand {
	
	@Override
	public void init() {
		//
	}
	
	@Override
	public void uninit() {
		// No uninitialization necessary.
	}
	
	@Override
	public void render() {
		// Static image.
	}
	
	public static void main(String[] args) {
		Window w = new Window();
		w.setTarget(new Tutorial01());
		w.start();
		ControlManager.select(w, new TutorialControlSet());
	}
	
	@Override
	public void input() {
		if (TutorialControlSet.FULLSCR.state())
			Window.toggleFS();
		if (TutorialControlSet.ESC.state())
			Window.close();
	}
	
}
