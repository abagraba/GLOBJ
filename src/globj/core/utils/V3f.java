package globj.core.utils;

public class V3f {
	
	public float x, y, z;
	
	public V3f(V3f v) {
		this(v.x, v.y, v.z);
	}
	
	public V3f(float x, float y, float z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	public V3f() {
		this(0, 0, 0);
	}

	public V3f add(V3f v) {
		x += v.x;
		y += v.y;
		z += v.z;
		return this;
	}
	
	public V3f multiply(float f) {
		x *= f;
		y *= f;
		z *= f;
		return this;
	}
	
	public float norm() {
		return (float) Math.sqrt(x * x + y * y + z * z);
	}
	
	public float inorm() {
		float inorm = norm();
		if (inorm != 0)
			inorm = 1 / inorm;
		return inorm;
	}
	
	public V3f unit() {
		return new V3f(this).multiply(inorm());
	}
	
}
