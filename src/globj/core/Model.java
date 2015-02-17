package globj.core;

import globj.objects.bufferobjects.VBO;

import java.util.HashMap;

public class Model {
	
	public HashMap<String, Attribute> attributes = new HashMap<String, Attribute>();
	
	public void setAttribute(String name, VBO vbo, int stride, int offset) {
		attributes.put(name, new Attribute(vbo, stride, offset));
	}
	
	
}
