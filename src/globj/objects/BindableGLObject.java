package globj.objects;

public abstract class BindableGLObject extends GLObject {
	
	public BindableGLObject(String name, int id) {
		super(name, id);
	}

	protected abstract BindTracker bindingTracker();
	protected abstract void bindOP(int id);
	protected abstract void destroyOP();
	
	protected void bind() {
		bindingTracker().update(id);
		if (!bindingTracker().changed())
			return;
		bindOP(id);
	}
	
	protected void bindNone() {
		bindingTracker().update(0);
		if (!bindingTracker().changed())
			return;
		bindOP(0);
	}
	
	protected void undobind() {
		if (bindingTracker().changed())
			bindOP(bindingTracker().revert());
	}
	
	protected void destroy() {
		if (bindingTracker().value() == id)
			bindingTracker().clear();
		destroyOP();
	}
	
}
