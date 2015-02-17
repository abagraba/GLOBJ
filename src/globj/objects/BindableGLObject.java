package globj.objects;

public abstract class BindableGLObject extends GLObject {
	
	public BindableGLObject(String name, int id) {
		super(name, id);
	}

	protected abstract BindTracker bindingTracker();
	protected abstract void bindOP(int id);
	protected abstract void destroyOP();
	
	public void bind() {
		bindingTracker().update(id);
		if (!bindingTracker().changed())
			return;
		bindOP(id);
	}
	
	public void bindNone() {
		bindingTracker().update(0);
		if (!bindingTracker().changed())
			return;
		bindOP(0);
	}
	
	public void undobind() {
		if (bindingTracker().changed())
			bindOP(bindingTracker().revert());
	}
	
	public void destroy() {
		if (bindingTracker().value() == id)
			bindingTracker().clear();
		destroyOP();
	}
	
}
