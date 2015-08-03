package globj.math;

/**
 * 
 * Quaternion q = a + bi + cj + dk.
 *
 */
public class UnitQuaternion {
	
	public float s, i, j, k;
	
	public UnitQuaternion(float s, float i, float j, float k) {
		this.s = s;
		this.i = i;
		this.j = j;
		this.k = k;
		normalize();
	}
	
	public UnitQuaternion(UnitQuaternion q) {
		this(q.s, q.i, q.j, q.k);
	}
	
	public UnitQuaternion() {
		this(1, 0, 0, 0);
	}
	
	public UnitQuaternion inverse() {
		return new UnitQuaternion(s, -i, -j, -k);
	}
	
	public UnitQuaternion product(UnitQuaternion q) {
		return new UnitQuaternion(s * q.s- i * q.i - j * q.j - k * q.k, s * q.i + i * q.s + j * q.k - k * q.j, s * q.j + j * q.s + k * q.i - i * q.k,
									s * q.k + k * q.s + i * q.j - j * q.i).normalize();
	}
	
	public float dot(UnitQuaternion q) {
		return s * q.s + i * q.i + j * q.j + k * q.k;
	}
	
	public float angle(UnitQuaternion q) {
		return (float) Math.acos(dot(q));
	}
	
	public static UnitQuaternion rotation(Vector3f axis, float theta) {
		float s = (float) Math.sin(theta / 2);
		float c = (float) Math.cos(theta / 2);
		axis.normalize();
		return new UnitQuaternion(c, s * axis.x, s * axis.y, s * axis.z).normalize();
	}
	
	public UnitQuaternion rotate(UnitQuaternion rotation) {
		return rotation.product(this);
	}
	
	public UnitQuaternion slerp(UnitQuaternion target, float factor) {
		float angle = angle(target);
		float f = Math.max(0, Math.min(1, factor));
		if (angle == 0 || f <= 0)
			return new UnitQuaternion(this);
		if (f >= 1)
			return new UnitQuaternion(target);
		float sa = (float) Math.sin(angle);
		float s1 = (float) Math.sin(angle * (1 - f));
		float s2 = (float) Math.sin(angle * f);
		return new UnitQuaternion(this).multiply(s1 / sa).add(new UnitQuaternion(target).multiply(s2 / sa)).normalize();
	}
	
	private UnitQuaternion add(UnitQuaternion q) {
		s += q.s;
		i += q.i;
		j += q.j;
		k += q.k;
		return this;
	}
	
	private UnitQuaternion multiply(float f) {
		s *= f;
		i *= f;
		j *= f;
		k *= f;
		return this;
	}
	
	private UnitQuaternion normalize() {
		float norm2 = s * s + i * i + j * j + k * k;
		float inorm = norm2 == 0 ? 0 : (float) Math.sqrt(1 / norm2);
		return multiply(inorm);
	}
	
	public void set(UnitQuaternion q) {
		s = q.s;
		i = q.i;
		j = q.j;
		k = q.k;
		normalize();
	}
	
	@Override
	public String toString() {
		return String.format("(%.2f, <%.2f, %.2f, %.2f>)", s, i, j, k);
	}
	
}
