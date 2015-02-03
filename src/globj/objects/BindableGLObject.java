package globj.objects;

public abstract class BindableGLObject extends GLObject {
	
	public BindableGLObject(String name, int id) {
		super(name, id);
	}
	
	public abstract void bind();
	
	public abstract void bindNone();
	
	protected abstract void undobind();
	
}
