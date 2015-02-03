package lwjgl.core.states;

import lwjgl.debug.GLDebug;

public class State<T> {
	
	public final String name;
	private T state;
	private T resolvedstate;
	
	/**
	 * Creates a new state with initial value. If resolved is true, then the
	 * state is considered resolved. If state is null the state is also
	 * considered resolved.
	 * 
	 * @param name
	 * @param state
	 * @param resolved
	 */
	public State(String name, T state, boolean resolved) {
		this.name = name;
		this.state = state;
		if (resolved)
			resolvedstate = state;
	}
	
	public State(String name, T state) {
		this(name, state, true);
	}
	
	/**
	 * @return the last resolved value of this state.
	 */
	public T value() {
		return resolvedstate;
	}
	
	/**
	 * @return the current value of this state. Same as {@link #value()} if the
	 *         state has been resolved.
	 */
	public T state() {
		return state;
	}
	
	public boolean resolved() {
		if (state == null)
			return resolvedstate == null;
		return state.equals(resolvedstate);
	}
	
	public void setState(T state) {
		resolvedstate = state;
		this.state = state;
	}
	
	public void resolve() {
		resolvedstate = state;
	}
	
	public void setStateAndResolve(T state) {
		this.resolvedstate = this.state = state;
	}
	
	public String toString() {
		String s = GLDebug.fixedString(name + ':') + value();
		if (!resolved())
			s += "\tUnresolved:\t" + state();
		return s;
	}
}
