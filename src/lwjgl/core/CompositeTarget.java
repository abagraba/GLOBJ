package lwjgl.core;

import java.util.concurrent.ConcurrentLinkedQueue;

public final class CompositeTarget extends RenderTarget {

	private final ConcurrentLinkedQueue<RenderTarget> targets = new ConcurrentLinkedQueue<RenderTarget>();

	public void addTarget(RenderTarget target) {
		targets.add(target);
		target.init();
	}

	public void removeTarget(RenderTarget target) {
		target.uninit();
		targets.remove(target);
	}

	@Override
	public final void render() {
		for (RenderTarget target : targets)
			target.render();
	}

	@Override
	public void init() {
		for (RenderTarget target : targets)
			target.init();
	}

	@Override
	public void uninit() {
		for (RenderTarget target : targets)
			target.uninit();
	}

	@Override
	public void input() {
		for (RenderTarget target : targets)
			target.input();
	}

}
