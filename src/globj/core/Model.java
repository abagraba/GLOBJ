package globj.core;

import globj.objects.bufferobjects.VBO;

import java.util.HashMap;
import java.util.Map;

public class Model {
	
	public Map<String, Attribute> attributes = new HashMap<String, Attribute>();
	
	public void setAttribute(String name, VBO vbo, int stride, int offset) {
		attributes.put(name, new Attribute(vbo, stride, offset));
	}
	
	
}
