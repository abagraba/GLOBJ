package globj.core;


import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;



/**
 * A Render Command that chains together multiple Render Commands. Render order is determined by insertion order.
 */

public final class CompositeCommand extends RenderCommand {
	
	private final Queue<RenderCommand> targets = new ConcurrentLinkedQueue<RenderCommand>();
	
	public void addTarget(RenderCommand target) {
		targets.add(target);
		target.init();
	}
	
	public void removeTarget(RenderCommand target) {
		target.uninit();
		targets.remove(target);
	}
	
	@Override
	public final void render() {
		for (RenderCommand target : targets)
			target.render();
	}
	
	@Override
	public void init() {
		for (RenderCommand target : targets)
			target.init();
	}
	
	@Override
	public void uninit() {
		for (RenderCommand target : targets)
			target.uninit();
	}
	
	@Override
	public void input() {
		for (RenderCommand target : targets)
			target.input();
	}
	
}
